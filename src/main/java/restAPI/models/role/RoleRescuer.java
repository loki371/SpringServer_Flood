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
    private Long id;

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private UserInfo user;

    @OneToOne
    private Registration inSaving;

    private Float score;

    @ManyToOne
    private Ward ward;

    public RoleRescuer(UserInfo user) {
        this.user = user;
    }
}