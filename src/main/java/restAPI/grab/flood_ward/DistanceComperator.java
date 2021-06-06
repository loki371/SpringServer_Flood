package restAPI.grab.flood_ward;

import restAPI.grab.flood_ward.entity.FloodRegistration;

import java.util.Comparator;

public class DistanceComperator implements Comparator<FloodRegistration> {
    @Override
    public int compare(FloodRegistration o1, FloodRegistration o2) {
        if (o1.getDistance2Rescuer() < o2.getDistance2Rescuer())
            return -1;
        if (o1.getDistance2Rescuer() > o2.getDistance2Rescuer())
            return 1;
        return 0;
    }
}
