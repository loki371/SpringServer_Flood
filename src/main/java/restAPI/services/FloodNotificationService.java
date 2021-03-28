package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.floodNotifications.FloodLocation;
import restAPI.repository.floodNotifications.FloodLocationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FloodNotificationService {
    Object addWardIdLock;

    @Autowired
    FloodLocationRepository floodLocationRepository;

    public FloodNotificationService() {
        addWardIdLock = new Object();
    }

    public boolean checkThenAddWardIdToFlood(String wardId) {
        synchronized (addWardIdLock) {
            if (!floodLocationRepository.existsByWardId(wardId)) {
                FloodLocation floodLocation = new FloodLocation();
                floodLocation.setWardId(wardId);
                floodLocationRepository.save(floodLocation);

                return true;
            } else {
                return false;
            }
        }
    }

    public List<String> getListWardInFlood() {
        List<FloodLocation> floodLocations = floodLocationRepository.findAll();
        List<String> result = new ArrayList<>();
        for (FloodLocation location : floodLocations) {
            result.add(location.getWardId());
        }

        return result;
    }

    public boolean checkThenRemoveWardIdToFlood(String wardId) {
        synchronized (addWardIdLock) {
            if (floodLocationRepository.existsByWardId(wardId)) {
                floodLocationRepository.deleteById(wardId);

                return true;

            } else {

                return false;
            }
        }
    }
}
