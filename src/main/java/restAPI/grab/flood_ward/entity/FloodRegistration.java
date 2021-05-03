package restAPI.grab.flood_ward.entity;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.registration.Registration;

@Getter @Setter
public class FloodRegistration {
    private Registration registration;
    private String rescuerUsername;

    public FloodRegistration(Registration registration, String rescuerUsername) {
        this.registration = registration;
        this.rescuerUsername = rescuerUsername;
    }
}
