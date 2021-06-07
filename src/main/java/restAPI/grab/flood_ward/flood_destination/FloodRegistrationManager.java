package restAPI.grab.flood_ward.flood_destination;

import restAPI.grab.FloodWardService;
import restAPI.grab.flood_ward.FloodWard;
import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloodRegistrationManager {
    private Map<Long, FloodRegistration> floodRegistrationMap;

    public FloodRegistrationManager(List<Registration> registrations) {
        this.floodRegistrationMap = new HashMap<>();
        for (Registration registration : registrations) {
            if (registration.getEState() != EState.STATE_EMERGENCY)
                continue;
            System.out.println("    -> create item in flood area: " + registration.getName());
            FloodRegistration floodRegistration = new FloodRegistration(registration, null);
            this.floodRegistrationMap.put(registration.getId(), floodRegistration);
        }
        System.out.println("after create Destination, size = " + floodRegistrationMap.values().size());
    }

    public List<FloodRegistration> getListRegistration() {
        return new ArrayList<>(this.floodRegistrationMap.values());
    }

    public FloodRegistration getRegistration(long regisId) {
        return floodRegistrationMap.get(regisId);
    }

    public void remove(long regisId) {
        floodRegistrationMap.remove(regisId);
        System.out.println("- destinationMgr remove regisId = " + regisId);
    }

    public void addRegistration(Registration registration) {
        FloodRegistration floodRegistration = new FloodRegistration(registration, null);
        this.floodRegistrationMap.put(registration.getId(), floodRegistration);
    }
}
