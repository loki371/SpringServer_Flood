package restAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import restAPI.models.UserInfo;
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleUser;
import restAPI.repository.UserRepository;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		// Tạo ra đối tượng person
		UserInfo person = new UserInfo(
				"xuanvy98",
				"xuanvy",
				"nguyen",
				"0935624754",
				"vyxuan98@gmail.com",
				"Aa123456");

		RoleUser roleUser = new RoleUser(person);
		RoleRescuer roleRescuer = new RoleRescuer(person);

		person.setRoleRescuer(roleRescuer);
		person.setRoleUser(roleUser);

		// Lưu vào db
//		userRepository.save(person);
	}
}
