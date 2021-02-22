package restAPI.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.role.Role;
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleUser;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
	private String password;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy = "user", optional = true, cascade = CascadeType.ALL)
	private RoleUser roleUser;

	@OneToOne(mappedBy = "user", optional = true, cascade = CascadeType.ALL)
	private RoleRescuer roleRescuer;

	public UserInfo(String username, String firstname, String lastname, String phone, String email, String password) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public UserInfo(String username, String firstname, String lastname, String phone, String email, String password, RoleUser roleUser, RoleRescuer roleRescuer) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.roleUser = roleUser;
		this.roleRescuer = roleRescuer;
	}

	public void showInfo() {
		System.out.println(id + " username: " + username + " first-last: " + firstname + "-" + lastname + "; " + phone + "; " + email + "; pass: " + password);
	}
}
