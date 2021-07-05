package restAPI.grab.flood_ward.entity;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.registration.Registration;

@Getter @Setter
public class FloodRegistration {
    private Registration registration;
    private String rescuerUsername;
    private float distance2Rescuer;
    private int order;

    public FloodRegistration(Registration registration, String rescuerUsername, int order) {
        this.registration = registration;
        this.rescuerUsername = rescuerUsername;
        this.order = order;
    }
}
