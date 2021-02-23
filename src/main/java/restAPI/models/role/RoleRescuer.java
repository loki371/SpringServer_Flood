package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;

import javax.persistence.*;

@Entity
@Table(name = "role_rescuer")
@Getter @Setter @NoArgsConstructor
public class RoleRescuer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private UserInfo userInfo;

    @OneToOne
    private Registration inSaving;

    private Float score;

    @ManyToOne
    private Ward ward;

    public RoleRescuer(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}