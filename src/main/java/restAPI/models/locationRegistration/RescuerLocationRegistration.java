package restAPI.models.locationRegistration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.registration.EState;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor
public class RescuerLocationRegistration {
    @Id
    protected String username;

    protected String locationType;

    protected String locationId;

    protected EState estate;

    public RescuerLocationRegistration(String username, String locationId, String locationType) {
        this.username = username;
        this.locationId = locationId;
        this.locationType = locationType;
        this.estate = EState.STATE_UNAUTHENTICATED;
    }
}
