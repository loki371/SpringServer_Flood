package restAPI.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import restAPI.Constants;
import restAPI.models.role.*;
import restAPI.models.UserInfo;
import restAPI.payload.LoginPayload;
import restAPI.payload.SignupPayload;
import restAPI.payload.JwtPayload;
import restAPI.payload.MessagePayload;
import restAPI.repository.role.*;
import restAPI.repository.UserRepository;
import restAPI.security.jwt.JwtUtils;
import restAPI.security.services.UserDetailsImpl;
import restAPI.services.RoleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleUtils;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RoleUserRepository roleUserRepository;

	@Autowired
	RoleAuthorityRepository roleAuthorityRepository;

	@Autowired
	RoleVolunteerRepository roleVolunteerRepository;

	@Autowired
	RoleRescuerRepository roleRescuerRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginPayload loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtPayload(jwt,
												 userDetails.getUsername(),
												 roles));
	}

	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupPayload signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessagePayload("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessagePayload("Error: Email is already in use!"));
		}

		// Create new user's account
		UserInfo user = new UserInfo(signUpRequest.getUsername(),
							 signUpRequest.getFirstname(),
							 signUpRequest.getLastname(),
							 signUpRequest.getPhone(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		userRepository.save(user);

		// add role
		Set<Role> roles = new HashSet<>();
		signUpRequest.getRole().forEach(
			item -> {
				Role roleAuthen = null;

				if (item.equals(Constants.ROLE_AUTHORITY)) {

					roleAuthen = roleUtils.getRoleByERole(ERole.ROLE_AUTHORITY);

					RoleAuthority role = new RoleAuthority(user);
					roleAuthorityRepository.save(role);
				}
				else if (item.equals(Constants.ROLE_USER)) {

					roleAuthen = roleUtils.getRoleByERole(ERole.ROLE_USER);

					RoleUser role = new RoleUser(user);
					roleUserRepository.save(role);
				}
				else if (item.equals(Constants.ROLE_RESCUER)) {

					roleAuthen = roleUtils.getRoleByERole(ERole.ROLE_RESCUER);

					RoleRescuer role = new RoleRescuer(user);
					roleRescuerRepository.save(role);
				}
				else if (item.equals(Constants.ROLE_VOLUNTEER)) {

					roleAuthen = roleUtils.getRoleByERole(ERole.ROLE_VOLUNTEER);

					RoleVolunteer role = new RoleVolunteer(user);
					roleVolunteerRepository.save(role);
				}
				else if (item.equals(Constants.ROLE_ADMIN)) {
					roleAuthen = roleUtils.getRoleByERole(ERole.ROLE_ADMIN);
				}

				roles.add(roleAuthen);
			}
		);
		user.setRoles(roles);

		// save to DB
		userRepository.save(user);

		return ResponseEntity.ok(new MessagePayload("User registered successfully!"));
	}
}
