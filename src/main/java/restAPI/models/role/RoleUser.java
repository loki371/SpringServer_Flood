package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "role_user")
@Getter @Setter @NoArgsConstructor
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "users_id")
    private User user;

    public RoleUser(User user) {
        this.user = user;
    }
}
