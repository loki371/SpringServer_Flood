package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role_rescuer")
@Getter @Setter @NoArgsConstructor
public class RoleRescuer {
    @Id
    @Size(max = 20)
    private String username;

    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private UserInfo userInfo;

    @OneToOne
    private Registration inSaving;

    private Float score;

    @ManyToOne
    private Ward ward;

    public RoleRescuer(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.userInfo = userInfo;
        this.score = (float) 0;
        this.ward = null;
    }
}