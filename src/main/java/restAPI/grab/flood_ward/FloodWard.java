package restAPI.grab.flood_ward;

import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.grab.flood_ward.flood_destination.FloodRegistrationManager;
import restAPI.grab.flood_ward.flood_rescuer.FloodRescuerManager;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.models.role.RoleRescuer;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.services.RegistrationService;

import java.util.List;

public class FloodWard {
    public final String wardId;
    private FloodRescuerManager rescuerManager;
    private FloodRegistrationManager destinationManager;

    public FloodWard(String wardId, List<Registration> registrationList) {
        this.wardId = wardId;
        rescuerManager = new FloodRescuerManager();
        destinationManager = new FloodRegistrationManager(registrationList);
        System.out.println("FloodWard: constructor: wardId = " + wardId + " destinationSize = " + registrationList.size());
    }

    public synchronized void updateDestinationForRescuers(){
        System.out.println("FloodWard.updateDestinationForRescuers");

        List<FloodRescuer> floodRescuers = rescuerManager.getListRescuers();
        List<FloodRegistration> floodRegistrations = destinationManager.getListRegistration();

        String rescuerUsername;
        float minDistance, curDistance;
        FloodRescuer target;
        for (FloodRegistration item : floodRegistrations) {

            Registration registration = item.getRegistration();
            if (registration.getLatitude() == -1 || registration.getLongitude() == -1)
                continue;

            rescuerUsername = "";
            minDistance = Float.MAX_VALUE;
            target = null;
            for (FloodRescuer board : floodRescuers) {

                curDistance = L2Distance(registration.getLongitude(), registration.getLongitude(),
                        board.getLocation().getLongitude(), board.getLocation().getLatitude());

                if (curDistance > minDistance) {
                    rescuerUsername = board.rescuerId;
                    minDistance = curDistance;
                    target = board;
                }
            }

            item.setRescuerUsername(rescuerUsername);

            FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
            floodRescuer.addToListRegis(item);

            if (floodRescuer.getFloodDestinations().size() > FloodRescuer.MAX_REGISTRATION)
                floodRescuers.remove(target);
        }

    }

    public synchronized void addRescuerToFloodWard(RoleRescuer rescuer, Location location, int boardSize) {
        rescuerManager.createNewRescuerInMgr(rescuer.getUsername(), boardSize, location);
    }

    public synchronized List<FloodRegistration> getDestinationForRescuer(String rescuerId) {
        System.out.println("FloodWard.getDestinationForRescuer");
        return destinationManager.getRegistrationsOfRescuer(rescuerId);
    }

    public synchronized boolean saveDestination(String rescuerId, long regisId, RegistrationRepository registrationRepository) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerId);
        Registration registration = registrationRepository.findById(regisId).get();

        if (!floodRescuer.increaseNumPeopleOnBoard(registration.getNumPerson()))
            return false;

        registration.setEState(EState.STATE_SAVED);
        registrationRepository.save(registration);

        floodRescuer.removeRegis(regisId);

        destinationManager.remove(regisId);

        return true;
    }

    public synchronized FloodRescuer getRescuer(String rescuerUsername) {
        return rescuerManager.getRescuer(rescuerUsername);
    }

    private float L2Distance(float long1, float lat1, float long2, float lat2) {
        return (long1-long2)*(long1-long2) + (lat1 - lat2) * (lat1 - lat2);
    }

    public synchronized void setGPSForRescuer(String rescuerUsername, Location location) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        floodRescuer.setLocation(location);
    }

    public synchronized boolean IsHaveRescuer(String rescuerUsername) {
        return rescuerManager.isHaveRescuer(rescuerUsername);
    }

    public synchronized boolean checkContainValidRegis(long regisId) {
        FloodRegistration floodRegistration = destinationManager.getRegistration(regisId);
        return floodRegistration != null;
    }

    public synchronized boolean isRescuerContainRegistration(String rescuerUsername, long regisId) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        return floodRescuer.containRegis(regisId);
    }

    public synchronized boolean decreasePeopleOnRescuerBoard(String rescuerUsername, int numPeople) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        return floodRescuer.decreaseNumPeopleOnBoard(numPeople);
    }

    public synchronized boolean stopRescuer(String rescuerUsername) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        List<Registration> registrationList = floodRescuer.getFloodDestinations();
        for (Registration item : registrationList) {
            destinationManager.setNullRescuerToDestination(item.getId());
        }
        return true;
    }
}
