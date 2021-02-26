package restAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.role.*;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
@Getter @Setter @NoArgsConstructor
public class UserInfo {
	@Id
	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	private String firstname;

	@NotBlank
	@Size(max = 50)
	private String lastname;

	@NotBlank
	@Size(max = 11)
	private String phone;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonIgnore
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_username"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

//	@OneToOne(mappedBy = "userInfo", optional = true, cascade = CascadeType.ALL)
////	@JsonIgnore
//	private RoleUser roleUser;
//
//	@OneToOne(mappedBy = "userInfo", optional = true, cascade = CascadeType.ALL)
////	@JsonIgnore
//	private RoleRescuer roleRescuer;
//
//	@OneToOne(mappedBy = "userInfo", optional = true, cascade = CascadeType.ALL)
////	@JsonIgnore
//	private RoleVolunteer roleVolunteer;
//
//	@OneToOne(mappedBy = "userInfo", optional = true, cascade = CascadeType.ALL)
////	@JsonIgnore
//	private RoleAuthority roleAuthority;

	public UserInfo(String username, String firstname, String lastname, String phone, String email, String password) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
		this.password = password;

//		this.roleUser = null;
//		this.roleRescuer = null;
//		this.roleVolunteer = null;
//		this.roleAuthority = null;
	}

	public void showInfo() {
		System.out.println(" username: " + username + " first-last: " + firstname + "-" + lastname + "; " + phone + "; " + email + "; pass: " + password);
	}
}
