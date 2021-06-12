package restAPI.models.registration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Viewer")
@Setter
@Getter
@NoArgsConstructor
public class Viewer {

    public Viewer(String username) {
        this.username = username;
        registrationList = new ArrayList<>();
    }

    @Id
    private String username;

    @OneToMany
    private List<Registration> registrationList;
}
