package restAPI.grab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.grab.flood_ward.FloodWard;
import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.models.location.Ward;
import restAPI.models.registration.RegisOrder;
import restAPI.models.registration.Registration;
import restAPI.models.role.RoleRescuer;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.services.FloodNotificationService;
import restAPI.services.RegisOrderService;
import restAPI.services.RegistrationService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FloodWardService {
    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    FloodNotificationService floodNotificationService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    RegisOrderService regisOrderService;

    private Map<String, FloodWard> floodingWardMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        List<String> listWardIdInFlood = floodNotificationService.getListWardInFlood();

        for (String wardId : listWardIdInFlood) {
            List<Registration> registrations = registrationService.changeNotifyFloodRegistrationAndGetAllRegistrations(wardId);
            createFloodingLocation(wardId, registrations);
        }
    }

    public boolean createFloodingLocation(String wardId, List<Registration> registrationList) {
        System.out.println("create Flooding Location: ward " + wardId + " regisList.size = " + registrationList.size());
        if (floodingWardMap.containsKey(wardId)) {
            System.out.println("map contain wardId in Flood -> return false");
            return false;
        }

        FloodWard floodWard = new FloodWard(wardId, registrationList, regisOrderService);

        floodingWardMap.put(wardId, floodWard);

        return true;
    }

    public void removeFloodWard(String wardId) {
        floodingWardMap.remove(wardId);
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

        floodRescuer.removeAllRegis();

        floodWard.updateDestinationForRescuers(floodRescuer);

        return floodRescuer.getFloodDestinations();
    }

    public List<FloodRegistration> getFloodRegistrationNearRescuer(String wardId, String rescuerUsername) {
        System.out.println("FloodWardService.getFloodRegistrationNearRescuer");
        FloodWard floodWard = floodingWardMap.get(wardId);
        if (floodWard == null)
            return null;

        FloodRescuer floodRescuer = floodWard.getRescuer(rescuerUsername);
        if (floodRescuer == null)
            return null;

        return floodWard.updateGSPRegistrationForRescuers(floodRescuer);
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

    public boolean saveDestination(String rescuerUsername, String wardId, long regisId, int numPeople) {
        FloodWard floodWard = floodingWardMap.get(wardId);

        if (!floodWard.isRescuerContainRegistration(rescuerUsername, regisId))
            return false;

        return floodWard.saveDestination(rescuerUsername, regisId, registrationRepository, numPeople);
    }

    public boolean peopleGetOutFromRescuerBoard(String wardId, String rescuerUsername, int numPeople) {
        if (numPeople <= 0)
            return false;

        FloodWard floodWard = floodingWardMap.get(wardId);
        return floodWard.decreasePeopleOnRescuerBoard(rescuerUsername, numPeople);
    }

    public void stop(String rescuerUsername, String wardId) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        floodWard.stopRescuer(rescuerUsername);
    }

    public void addRegistrationToFloodWard(String wardId, Registration registration) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        floodWard.addRegistration(registration);
    }

    public void removeRegistrationFromFloodWard(String wardId, Registration registration) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        floodWard.removeRegistration(registration);
    }

    public void deleteDestinationFromRescuer(String wardId, String rescuerUsername, long[] registrationIds) {
        FloodWard floodWard = floodingWardMap.get(wardId);
        floodWard.removeRegistrationOfRescuer(rescuerUsername, registrationIds);
    }

    public void updateRegistrationInWard(String wardId, long regisId, int numPeople, int order) {
        FloodWard floodWard = floodingWardMap.get(wardId);

        RegisOrder regisOrder = regisOrderService.getObjectByRegisId(regisId, wardId);
        regisOrder.setOrderRegis(order);
        regisOrderService.save(regisOrder);

        floodWard.updateNumPeopleAndOrderRegis(regisId, numPeople, order);
    }
}
