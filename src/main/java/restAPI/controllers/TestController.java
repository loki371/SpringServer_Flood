package restAPI.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/volunteer")
	@PreAuthorize("hasRole('VOLUNTEER')")
	public String moderatorAccess() {
		return "Volunteer Board.";
	}

	@GetMapping("/rescuer")
	@PreAuthorize("hasRole('RESCUER')")
	public String adminAccess() {
		return "Rescuer Board.";
	}
}
