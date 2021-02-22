package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;

import javax.persistence.*;

@Entity
@Table(name = "role_volunteer")
@Getter
@Setter
@NoArgsConstructor
public class RoleVolunteer {
    @Id
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserInfo user;

    @ManyToOne
    private Ward ward;

    private Float score;
}
