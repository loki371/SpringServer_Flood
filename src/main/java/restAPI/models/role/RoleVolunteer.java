package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role_volunteer")
@Getter
@Setter
@NoArgsConstructor
public class RoleVolunteer {
    @Id
    @Size(max = 20)
    private String username;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserInfo userInfo;

    @ManyToOne
    private Ward ward;

    private Float score;
}
