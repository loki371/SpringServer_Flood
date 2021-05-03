package restAPI.grab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.grab.flood_ward.FloodWard;
import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;
import restAPI.models.role.RoleRescuer;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.services.RegistrationService;
import restAPI.services.WardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FloodWardService {
    @Autowired
    RegistrationRepository registrationRepository;

    private Map<String, FloodWard> floodingWardMap = new ConcurrentHashMap<>();

    public boolean createFloodingLocation(String wardId, List<Registration> registrationList) {
        System.out.println("create Flooding Location: regisList.size = " + registrationList.size());
        if (floodingWardMap.containsKey(wardId))
            return false;

        FloodWard floodWard = new FloodWard(wardId, registrationList);

        floodingWardMap.put(wardId, floodWard);

        return true;
    }

    public List<FloodRegistration> getFloodRegistrationOfRescuer(String wardId, String rescuerUsername) {
        System.out.println("FloodWardService.getFloodRegistrationOfRescuer");
        FloodWard floodWard = floodingWardMap.get(wardId);
        if (floodWard == null)
            return null;

        FloodRescuer floodRescuer = floodWard.getRescuer(rescuerUsername);
        if (floodRescuer == null) {
            return null;
        }

        floodWard.updateDestinationForRescuers();

        return floodWard.getDestinationForRescuer(rescuerUsername);
    }

    public boolean checkInFlood(String wardId) {
        return floodingWardMap.containsKey(wardId);
    }

    public void startSavingFromRescuer(RoleRescuer roleRescuer,
                                       Location location,
                                       int boardSize) {
        Ward ward = roleRescuer.getWard();
        FloodWard floodWard = floodingWardMap.get(ward.getId());
        floodWard.addRescuerToFloodWard(roleRescuer, location, boardSize);
        return;
    }

    public void setGPSForRescuer(String rescuerUsername, Location location, String wardId) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        floodWard.setGPSForRescuer(rescuerUsername, location);
    }

    public boolean checkRescuerStarted(String rescuerUsername, String wardId) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        return floodWard.IsHaveRescuer(rescuerUsername);
    }

    public boolean checkContainValidRegis(String wardId, long regisId) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        return floodWard.checkContainValidRegis(regisId);
    }

    public boolean saveDestination(String rescuerUsername, String wardId, long regisId) {
        FloodWard floodWard = floodingWardMap.get(wardId);

        if (!floodWard.isRescuerContainRegistration(rescuerUsername, regisId))
            return false;

        return floodWard.saveDestination(rescuerUsername, regisId, registrationRepository);
    }

    public boolean peopleGetOutFromRescuerBoard(String wardId, String rescuerUsername, int numPeople) {
        if (numPeople <= 0)
            return false;

        FloodWard floodWard = floodingWardMap.get(wardId);
        return floodWard.decreasePeopleOnRescuerBoard(rescuerUsername, numPeople);
    }
}
