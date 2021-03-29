package restAPI.models.locationRegistration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthorityLocationRegistration {
    @Id
    protected String username;

    protected String locationType;

    protected String locationId;

    protected boolean isRejected;

    public AuthorityLocationRegistration(String username, String locationId, String locationType) {
        this.username = username;
        this.locationId = locationId;
        this.locationType = locationType;
        this.isRejected = false;
    }
}

