package restAPI.models.locationRegistration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.registration.EState;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthorityLocationRegistration {
    protected Long id;

    @Id
    protected String username;

    protected String locationType;

    protected String locationId;

    protected EState state;

    public AuthorityLocationRegistration(String username, String locationId, String locationType) {
        this.id = UUID.nameUUIDFromBytes(username.getBytes()).getMostSignificantBits();
        this.username = username;
        this.locationId = locationId;
        this.locationType = locationType;
        this.state = EState.STATE_UNAUTHENTICATED;
    }
}

