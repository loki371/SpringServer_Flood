package restAPI.grab.flood_ward.flood_rescuer;

import restAPI.grab.flood_ward.FloodWard;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.models.registration.Registration;

import java.util.*;

public class FloodRescuerManager {
    private Map<String, FloodRescuer> rescuerMap;

    public FloodRescuer createNewRescuerInMgr(String rescuerId,
                                              int maxNumberOnBoard,
                                              Location location) {
        System.out.println("FloodRescuerManager.createNewRescuerInMgr");

        if (rescuerMap.containsKey(rescuerId))
            return null;

        FloodRescuer rescuer = new FloodRescuer(
                rescuerId,
                maxNumberOnBoard,
                location.getLongitude(), location.getLatitude());

        rescuer.setFloodDestinations(null);

        rescuerMap.put(rescuerId, rescuer);

        return rescuer;
    }

    public FloodRescuer getRescuer(String username) {
        System.out.println("FloodRescuerManager.getRescuer");
        return rescuerMap.get(username);
    }

    public List<FloodRescuer> getListRescuers() {
        System.out.println("FloodRescuerManager.getListRescuers");
        return new LinkedList<>(rescuerMap.values());
    }

    public boolean isHaveRescuer(String rescuerUsername) {
        return rescuerMap.containsKey(rescuerUsername);
    }
}
