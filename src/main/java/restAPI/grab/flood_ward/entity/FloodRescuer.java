package restAPI.grab.flood_ward.entity;

import restAPI.grab.flood_ward.entity.Location;
import restAPI.models.registration.Registration;

import java.util.*;

public class FloodRescuer {
    public final String rescuerId;

    public static final int MAX_REGISTRATION = 5;

    private float longitude;
    private float latitude;

    private int numPeopleOnBoard;
    private int maximumChair;
    private List<FloodRegistration> floodDestinations;

    private long lastActivateTime;

    public FloodRescuer(String rescuerId, int maximumChair, float longitude, float latitude) {
        this.rescuerId = rescuerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maximumChair = maximumChair;
        this.floodDestinations = new LinkedList<>();
    }

    public synchronized boolean increaseNumPeopleOnBoard(int d) {
        int sum = numPeopleOnBoard + d;

        if (sum > maximumChair)
            return false;

        numPeopleOnBoard = sum;
        return true;
    }

    public synchronized boolean decreaseNumPeopleOnBoard(int d) {
        int diff = numPeopleOnBoard - d;

        if (diff < 0)
            return false;

        numPeopleOnBoard = diff;

        return true;
    }

    public synchronized void changeLocation(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public synchronized Location getLocation() {
        return new Location(longitude, latitude);
    }

    public synchronized void setFloodDestinations(List<FloodRegistration> newDestinations) {
        this.floodDestinations = newDestinations;
    }

    public synchronized List<FloodRegistration> getFloodDestinations() {
        return new LinkedList<>(this.floodDestinations);
    }

    public synchronized void addToListRegis(FloodRegistration item) {
        if (floodDestinations == null)
            floodDestinations = new LinkedList<>();
        floodDestinations.add(item);
    }

    public synchronized boolean containRegis(long regidId) {
        for (FloodRegistration item : floodDestinations)
            if (item.getRegistration().getId() == regidId)
                return true;
        return false;
    }

    public synchronized void removeRegis(long regisId) {
        floodDestinations.remove(regisId);
    }

    public synchronized void setLocation(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }
}
