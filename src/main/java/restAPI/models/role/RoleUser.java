package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "role_user")
@Getter @Setter @NoArgsConstructor
public class RoleUser {
    @Id
    @JoinColumn(name = "users", referencedColumnName = "id")
    private Long id;

    public RoleUser(Long id) {
        this.id = id;
    }
}
