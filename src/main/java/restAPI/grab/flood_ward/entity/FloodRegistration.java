package restAPI.grab.flood_ward.entity;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.registration.Registration;

@Getter @Setter
public class FloodRegistration {
    private final Registration registration;
    private String rescuerUsername;
    private int order;

    private double distance2Rescuer = Double.MAX_VALUE;

    public FloodRegistration(Registration registration, String rescuerUsername, int order) {
        this.registration = registration;
        this.rescuerUsername = rescuerUsername;
        this.order = order;
    }
}
