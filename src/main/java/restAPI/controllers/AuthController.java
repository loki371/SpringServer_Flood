package restAPI.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import restAPI.Constants;
import restAPI.models.role.*;
import restAPI.models.UserInfo;
import restAPI.payload.LoginPayload;
import restAPI.payload.SignupPayload;
import restAPI.payload.JwtPayload;
import restAPI.payload.MessagePayload;
import restAPI.repository.role.RoleRepository;
import restAPI.repository.UserRepository;
import restAPI.security.jwt.JwtUtils;
import restAPI.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

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

		// add role
		Set<Role> roles = new HashSet<>();
		String roleNotFound = "Error: Role is not found.";
		signUpRequest.getRole().forEach(
			item -> {
				Role roleAuthen = null;

				if (item.equals(Constants.ROLE_AUTHORITY)) {

					roleAuthen = roleRepository.findByName(ERole.ROLE_AUTHORITY)
							.orElseThrow(() -> new RuntimeException(roleNotFound));

					RoleAuthority role = new RoleAuthority(user);
					user.setRoleAuthority(role);
				}
				else if (item.equals(Constants.ROLE_USER)) {

					roleAuthen = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(roleNotFound));

					RoleUser role = new RoleUser(user);
					user.setRoleUser(role);
				}
				else if (item.equals(Constants.ROLE_RESCUER)) {

					roleAuthen = roleRepository.findByName(ERole.ROLE_RESCUER)
							.orElseThrow(() -> new RuntimeException(roleNotFound));

					RoleRescuer role = new RoleRescuer(user);
					user.setRoleRescuer(role);
				}
				else if (item.equals(Constants.ROLE_VOLUNTEER)) {

					roleAuthen = roleRepository.findByName(ERole.ROLE_VOLUNTEER)
							.orElseThrow(() -> new RuntimeException(roleNotFound));

					RoleVolunteer role = new RoleVolunteer(user);
					user.setRoleVolunteer(role);
				}
				else if (item.equals(Constants.ROLE_ADMIN)) {
					roleAuthen = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(roleNotFound));
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
