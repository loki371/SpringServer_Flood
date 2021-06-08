package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.floodNotifications.FloodLocation;
import restAPI.models.location.Ward;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.repository.floodNotifications.FloodLocationRepository;
import restAPI.repository.registration.RegistrationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FloodNotificationService {
    Object addWardIdLock;

    @Autowired
    FloodLocationRepository floodLocationRepository;

    @Autowired
    RegistrationRepository registrationRepository;

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

    public boolean checkThenRemoveWardIdToFlood(Ward ward) {
        synchronized (addWardIdLock) {
            if (floodLocationRepository.existsByWardId(ward.getId())) {

                floodLocationRepository.deleteById(ward.getId());

                List<Registration> registrationList = registrationRepository.findAllByWard(ward);

                for (Registration registration : registrationList)
                    if (registration.getEState() != EState.STATE_UNAUTHENTICATED) {
                        registration.setEState(EState.STATE_AUTHENTICATED);
                    }

                registrationRepository.saveAll(registrationList);

                return true;

            } else {

                return false;
            }
        }
    }
}
