package restAPI.models.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role_authority")
@Setter @Getter @NoArgsConstructor
public class RoleAuthority {
    @Id
    @Size(max = 20)
    private String username;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne
    private Ward ward;

    @ManyToOne
    @JsonIgnore
    private UserInfo farther;

    public RoleAuthority(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.userInfo = userInfo;
        this.ward = null;
        this.farther = null;
    }
}
