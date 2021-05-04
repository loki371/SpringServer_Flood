package restAPI.grab.flood_ward.flood_destination;

import restAPI.grab.FloodWardService;
import restAPI.grab.flood_ward.FloodWard;
import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
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
            FloodRegistration floodRegistration = new FloodRegistration(registration, null);
            this.floodRegistrationMap.put(registration.getId(), floodRegistration);
        }
    }

    public List<FloodRegistration> getListRegistration() {
        return new ArrayList<>(this.floodRegistrationMap.values());
    }

    public List<FloodRegistration> getRegistrationsOfRescuer(String rescuerUsername) {
        System.out.println("FloodRegistrationManager.getRegistrationsOfRescuer");
        List<FloodRegistration> result = new ArrayList<>();
        for (FloodRegistration item : floodRegistrationMap.values()) {
            if (item.getRescuerUsername().equals(rescuerUsername)
                || item.getRescuerUsername() == null) {
                result.add(item);
            }
        }
        return result;
    }

    public FloodRegistration getRegistration(long regisId) {
        return floodRegistrationMap.get(regisId);
    }

    public void remove(long regisId) {
        floodRegistrationMap.remove(regisId);
    }

    public void setNullRescuerToDestination(long regisId) {
        FloodRegistration floodRegistration = floodRegistrationMap.get(regisId);
        floodRegistration.setRescuerUsername(null);
    }

    public void addRegistration(Registration registration) {
        FloodRegistration floodRegistration = new FloodRegistration(registration, null);
        this.floodRegistrationMap.put(registration.getId(), floodRegistration);
    }
}
