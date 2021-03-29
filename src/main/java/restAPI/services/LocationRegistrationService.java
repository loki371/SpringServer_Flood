package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.locationRegistration.RescuerLocationRegistration;
import restAPI.models.locationRegistration.VolunteerLocationRegistration;
import restAPI.models.role.ERole;
import restAPI.repository.locationRegistration.AuthorityRegistrationRepository;
import restAPI.repository.locationRegistration.RescuerRegistrationRepository;
import restAPI.repository.locationRegistration.VolunteerRegistrationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationRegistrationService {

    @Autowired
    AuthorityRegistrationRepository authorityRepository;

    @Autowired
    RescuerRegistrationRepository rescuerRegistrationRepository;

    @Autowired
    VolunteerRegistrationRepository volunteerRegistrationRepository;

    public boolean addRegistrationToLocation(String username, String locationId, String locationType, ERole eRole) {
        switch (eRole) {
            case ROLE_AUTHORITY:

                if (authorityRepository.existsById(username))
                    return false;

                AuthorityLocationRegistration regis1 = new AuthorityLocationRegistration(username, locationId, locationType);
                authorityRepository.save(regis1);

                break;

            case ROLE_RESCUER:

                if (rescuerRegistrationRepository.existsById(username))
                    return false;

                RescuerLocationRegistration regis2 = new RescuerLocationRegistration(username, locationId, locationType);
                rescuerRegistrationRepository.save(regis2);

                break;

            case ROLE_VOLUNTEER:

                if (volunteerRegistrationRepository.existsById(username))
                    return false;

                VolunteerLocationRegistration regis3 = new VolunteerLocationRegistration(username, locationId, locationType);
                volunteerRegistrationRepository.save(regis3);

                break;

            default:
                return false;
        }
        return true;
    }

    public List<Object> getListAllLocation(ERole eRole) {
        List<Object> result = new ArrayList<>();
        switch (eRole) {
            case ROLE_AUTHORITY:
                List<AuthorityLocationRegistration> list1 = authorityRepository.findAll();
                result.addAll(list1);
                break;

            case ROLE_RESCUER:
                List<RescuerLocationRegistration> list2 = rescuerRegistrationRepository.findAll();
                result.addAll(list2);
                break;

            case ROLE_VOLUNTEER:
                List<VolunteerLocationRegistration> list3 = volunteerRegistrationRepository.findAll();
                result.addAll(list3);
                break;
        }
        return result;
    }
}
