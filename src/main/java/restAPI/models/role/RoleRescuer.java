package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.User;

import javax.persistence.*;

@Entity
@Table(name = "role_rescuer")
@Getter @Setter @NoArgsConstructor
public class RoleRescuer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "users_id")
    private User user;

    public RoleRescuer(User user) {
        this.user = user;
    }
}