package restAPI.grab.flood_ward;

import restAPI.grab.flood_ward.entity.FloodRegistration;
import restAPI.grab.flood_ward.entity.FloodRescuer;
import restAPI.grab.flood_ward.entity.Location;
import restAPI.grab.flood_ward.flood_destination.FloodRegistrationManager;
import restAPI.grab.flood_ward.flood_rescuer.FloodRescuerManager;
import restAPI.models.registration.EState;
import restAPI.models.registration.RegisOrder;
import restAPI.models.registration.Registration;
import restAPI.models.role.RoleRescuer;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.services.RegisOrderService;
import restAPI.utils.RegistrationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class FloodWard {
    private RegisOrderService regisOrderService;

    public final String wardId;
    private final FloodRescuerManager rescuerManager;
    private final FloodRegistrationManager destinationManager;
    public static final double RANGE_TARGET_RESCUER = 1.5;

    public FloodWard(String wardId, List<Registration> registrationList, RegisOrderService regisOrderService) {
        this.wardId = wardId;
        this.regisOrderService = regisOrderService;
        rescuerManager = new FloodRescuerManager();
        destinationManager = new FloodRegistrationManager(registrationList, regisOrderService);

        System.out.println("\nFloodWard: constructor: wardId = " + wardId + " destinationSize = " + registrationList.size());
    }

    private int getMaxEmergencyOrderOfListFloodRegis(List<FloodRegistration> floodRegistrations) {
        int maxEmergencyOrder = RegisOrderService.ORDER_NORMAL;
        for (FloodRegistration item : floodRegistrations) {
            if (item.getOrder() <= maxEmergencyOrder)
                maxEmergencyOrder = item.getOrder();
        }
        return maxEmergencyOrder;
    }

    private FloodRegistration findTargetDestination(List<FloodRegistration> floodRegistrations, int maxEmergencyOrder,
                                                    FloodRescuer floodRescuer) {
        System.out.println("findTargetDestination");

        double minDistance = Long.MAX_VALUE;
        double curDistance;

        FloodRegistration target = null;

        for (FloodRegistration item : floodRegistrations) {

            if (item.getRescuerUsername() != null)
                continue;

            if (item.getOrder() != maxEmergencyOrder)
                continue;

            Registration registration = item.getRegistration();
            if (registration.getLatitude() == -1 || registration.getLongitude() == -1)
                continue;

            curDistance = RegistrationUtils.distanceGPSToMeter(
                    registration.getLatitude(),
                    registration.getLongitude(),
                    floodRescuer.getLocation().getLatitude(),
                    floodRescuer.getLocation().getLongitude());

            System.out.println(" - Registration name " + registration.getName() + " eState = " + registration.getEState()
                    + " distance = " + curDistance);

            if (curDistance > minDistance) {
                target = item;
                minDistance = curDistance;
                System.out.println("   -> set minDistance = " + minDistance + " and target = " + registration.getId());
            }
        }

        if (target == null)
            return null;

        target.setDistance2Rescuer(minDistance);
        target.setRescuerUsername(floodRescuer.rescuerId);

        floodRescuer.addToListRegis(target);

        return target;
    }

    public synchronized void updateDestinationForRescuers(FloodRescuer floodRescuer){
        System.out.println("\nFloodWard.updateDestinationForRescuers");

        List<FloodRegistration> floodRegistrations = destinationManager.getListRegistration();

        PriorityQueue<FloodRegistration> regisForRescuer = new PriorityQueue<>(
                FloodRescuerManager.SIZE_REGIS_PER_RESCUER,
                new DistanceComperator());

        System.out.println("Flood RegistrationList = " + floodRegistrations.size());

        int maxEmergencyOrder = getMaxEmergencyOrderOfListFloodRegis(floodRegistrations);

        System.out.println("max Emergency Order = " + maxEmergencyOrder);

        FloodRegistration targetDestination = findTargetDestination(floodRegistrations, maxEmergencyOrder, floodRescuer);

        if (targetDestination == null) {
            System.out.println("targetDestination == null -> return");
            return;
        }

        double targetDistance = targetDestination.getDistance2Rescuer();

        System.out.println("\nnow, find destination from road to target");
        double totalDistance;
        for (FloodRegistration item : floodRegistrations) {

            if (item.getRescuerUsername() != null)
                continue;

            Registration registration = item.getRegistration();
            if (registration.getLatitude() == -1 || registration.getLongitude() == -1)
                continue;

            double distanceRegisRescuer = RegistrationUtils.distanceGPSToMeter(
                    registration.getLatitude(),
                    registration.getLongitude(),
                    floodRescuer.getLocation().getLatitude(),
                    floodRescuer.getLocation().getLongitude());

            double distanceRegisTarget = RegistrationUtils.distanceGPSToMeter(
                    registration.getLatitude(),
                    registration.getLongitude(),
                    targetDestination.getRegistration().getLatitude(),
                    targetDestination.getRegistration().getLongitude());

            totalDistance = distanceRegisRescuer + distanceRegisTarget;

            System.out.println(" - Registration name " + registration.getName() + " eState = " + registration.getEState()
                + " total Distance = " + totalDistance + " targetDistance*range = " + targetDistance * RANGE_TARGET_RESCUER);

            if (totalDistance > targetDistance * RANGE_TARGET_RESCUER) {
                System.out.println("   - totalDistance > targetDistance*range -> skip");
                continue;
            }

            if (regisForRescuer.size() < FloodRescuerManager.SIZE_REGIS_PER_RESCUER) {

                item.setRescuerUsername(floodRescuer.rescuerId);
                item.setDistance2Rescuer(distanceRegisRescuer);

                regisForRescuer.add(item);
                System.out.println("   -> size < maxSize : add to queue");

            } else {
                FloodRegistration regisPeek = regisForRescuer.peek();

                if (regisPeek.getDistance2Rescuer() > totalDistance) {

                    item.setRescuerUsername(floodRescuer.rescuerId);
                    item.setDistance2Rescuer(distanceRegisRescuer);

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

    public synchronized List<FloodRegistration> updateGSPRegistrationForRescuers(FloodRescuer floodRescuer) {
        System.out.println("\nFloodWard.updateGSPRegistrationForRescuers:");

        List<FloodRegistration> floodRegistrations = destinationManager.getListRegistration();

        PriorityQueue<FloodRegistration> regisForRescuer = new PriorityQueue<>(
                FloodRescuerManager.SIZE_REGIS_NEAR_RESCUER,
                new DistanceComperator());

        System.out.println("Flood RegistrationList = " + floodRegistrations.size());

        double curDistance;
        for (FloodRegistration item : floodRegistrations) {

            if (item.getRescuerUsername() != null)
                continue;

            Registration registration = item.getRegistration();
            if (registration.getLatitude() == -1 || registration.getLongitude() == -1)
                continue;

            curDistance = RegistrationUtils.distanceGPSToMeter(
                    registration.getLatitude(),
                    registration.getLongitude(),
                    floodRescuer.getLocation().getLatitude(),
                    floodRescuer.getLocation().getLongitude());

            System.out.println(" - Registration name " + registration.getName() + " eState = " + registration.getEState()
                    + " distance = " + curDistance);

            if (curDistance > 200) {
                System.out.println("   -> currDistance > 200m -> skip");
                continue;
            }

            if (regisForRescuer.size() < FloodRescuerManager.SIZE_REGIS_NEAR_RESCUER) {

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

        return new ArrayList<>(regisForRescuer);
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
            int remainPeople = registration.getNumPerson() - numPeople;
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
        destinationManager.addRegistration(registration, regisOrderService);
    }

    public synchronized void removeRegistration(Registration registration) {
        destinationManager.remove(registration.getId());
    }

    public synchronized void removeRegistrationOfRescuer(String rescuerUsername, long[] registrationIds) {
        FloodRescuer floodRescuer = rescuerManager.getRescuer(rescuerUsername);
        floodRescuer.getFloodDestinations().removeIf(item -> {
            for (long id : registrationIds)
                if (item.getRegistration().getId() == id) {
                    item.setRescuerUsername(null);
                    item.setDistance2Rescuer(0);
                    return true;
                }
            return false;
        });
    }

    public synchronized void updateNumPeopleAndOrderRegis(long regisId, int numPeople, int order) {
        FloodRegistration floodRegistration = destinationManager.getRegistration(regisId);
        if (floodRegistration == null)
            return;

        floodRegistration.setOrder(order);
        floodRegistration.getRegistration().setNumPerson(numPeople);
    }
}
