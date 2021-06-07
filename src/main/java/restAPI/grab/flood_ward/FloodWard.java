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
import java.util.List;
import java.util.PriorityQueue;

public class FloodWard {
    public final String wardId;
    private final FloodRescuerManager rescuerManager;
    private final FloodRegistrationManager destinationManager;

    public FloodWard(String wardId, List<Registration> registrationList) {
        this.wardId = wardId;
        rescuerManager = new FloodRescuerManager();
        destinationManager = new FloodRegistrationManager(registrationList);

        System.out.println("FloodWard: constructor: wardId = " + wardId + " destinationSize = " + registrationList.size());
    }

    public synchronized void updateDestinationForRescuers(FloodRescuer floodRescuer){
        System.out.println("FloodWard.updateDestinationForRescuers");

        List<FloodRegistration> floodRegistrations = destinationManager.getListRegistration();

        PriorityQueue<FloodRegistration> regisForRescuer = new PriorityQueue<>(
                FloodRescuerManager.SIZE_REGIS_PER_RESCUER,
                new DistanceComperator());

        System.out.println("Flood RegistrationList = " + floodRegistrations.size());

        float curDistance;
        for (FloodRegistration item : floodRegistrations) {

            if (item.getRescuerUsername() != null)
                continue;

            Registration registration = item.getRegistration();
            if (registration.getLatitude() == -1 || registration.getLongitude() == -1)
                continue;

            curDistance = L2Distance(registration.getLongitude(), registration.getLongitude(),
                    floodRescuer.getLocation().getLongitude(), floodRescuer.getLocation().getLatitude());

            System.out.println(" - Registration name " + registration.getName() + " eState = " + registration.getEState()
                + " distance = " + curDistance);

            if (regisForRescuer.size() < FloodRescuerManager.SIZE_REGIS_PER_RESCUER) {

                item.setRescuerUsername(floodRescuer.rescuerId);
                item.setDistance2Rescuer(curDistance);

                regisForRescuer.add(item);
                System.out.println("   -> size < maxSize : add to queue");

            } else {
                FloodRegistration regisPeek = regisForRescuer.peek();

                if (regisPeek.getDistance2Rescuer() > curDistance) {

                    item.setRescuerUsername(floodRescuer.rescuerId);
                    item.setDistance2Rescuer(curDistance);

                    System.out.println("    -> size == maxSize: remove top("+regisPeek.getDistance2Rescuer()
                            +") and add("+item.getDistance2Rescuer()+")");

                    regisPeek.setRescuerUsername(null);
                    regisPeek.setDistance2Rescuer(0);

                    regisForRescuer.poll();
                    regisForRescuer.add(item);

                } else {
                    System.out.println("    -> cannot add because distance is not small enough");
                }
            }
        }

        System.out.println("Size Queue after all : " + regisForRescuer.size());
        for (FloodRegistration item : regisForRescuer) {
            System.out.println("adding item : " + item.getRegistration().getId()
                    + " sizeToRescuer " + item.getDistance2Rescuer());
            floodRescuer.addToListRegis(item);
        }
    }

    public synchronized void addRescuerToFloodWard(RoleRescuer rescuer, Location location, int boardSize) {
        rescuerManager.createNewRescuerInMgr(rescuer.getUsername(), boardSize, location);
    }

    public synchronized boolean saveDestination(String rescuerId, long regisId, RegistrationRepository registrationRepository,
                                                int numPeople) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerId);

        Registration registration = registrationRepository.findById(regisId).get();

        if (!floodRescuer.increaseNumPeopleOnBoard(registration.getNumPerson()))
            return false;

        if (registration.getNumPerson() <= numPeople) {
            System.out.println("saving: regis.getNumPerson <= saving.numPeople -> saveAll");
            registration.setEState(EState.STATE_SAVED);
            registrationRepository.save(registration);

            floodRescuer.removeRegis(regisId);
            destinationManager.remove(regisId);

        } else {
            int remainPeople = registration.getNumPerson()-numPeople;
            registration.setNumPerson(remainPeople);
            registrationRepository.save(registration);

            System.out.println("saving: regis.getNumPerson > saving.numPeople -> remove numPeople a part = " + numPeople);

            FloodRegistration floodRegistration = destinationManager.getRegistration(regisId);
            floodRegistration.getRegistration().setNumPerson(remainPeople);
        }

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
        System.out.println("Stop Rescuer: " + rescuerUsername);
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        List<FloodRegistration> registrationList = floodRescuer.getFloodDestinations();
        for (FloodRegistration item : registrationList) {
            System.out.println(" - idRegis = " + item.getRegistration().getId() + " rescuerId " + item.getRescuerUsername()
                + " distanceToRescuer = " + item.getDistance2Rescuer());

            item.setRescuerUsername(null);
            item.setDistance2Rescuer(0);
        }
        return true;
    }

    public synchronized void addRegistration(Registration registration) {
        destinationManager.addRegistration(registration);
    }

    public synchronized void removeRegistration(Registration registration) {
        destinationManager.remove(registration.getId());
    }
}
