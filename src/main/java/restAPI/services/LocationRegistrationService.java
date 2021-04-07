package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.Constants;
import restAPI.models.location.Ward;
import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.locationRegistration.RescuerLocationRegistration;
import restAPI.models.locationRegistration.VolunteerLocationRegistration;
import restAPI.models.role.ERole;
import restAPI.models.role.RoleAuthority;
import restAPI.repository.location.WardRepository;
import restAPI.repository.locationRegistration.AuthorityRegistrationRepository;
import restAPI.repository.locationRegistration.RescuerRegistrationRepository;
import restAPI.repository.locationRegistration.VolunteerRegistrationRepository;
import restAPI.repository.role.RoleAuthorityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationRegistrationService {

    @Autowired
    AuthorityRegistrationRepository authorityRegistrationRepository;

    @Autowired
    RescuerRegistrationRepository rescuerRegistrationRepository;

    @Autowired
    VolunteerRegistrationRepository volunteerRegistrationRepository;

    @Autowired
    RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    WardRepository wardRepository;

    public boolean addRegistrationToLocation(String username, String locationId, String locationType, ERole eRole) {
        switch (eRole) {
            case ROLE_AUTHORITY:

                if (authorityRegistrationRepository.existsById(username))
                    return false;

                AuthorityLocationRegistration regis1 = new AuthorityLocationRegistration(username, locationId, locationType);
                authorityRegistrationRepository.save(regis1);

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
                List<AuthorityLocationRegistration> list1 = authorityRegistrationRepository.findAll();
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

    public boolean processAuthorityRegistration(String fatherUsername, String childUsername, boolean accepting) {
        Optional<RoleAuthority> authority = roleAuthorityRepository.findByUsername(fatherUsername);
        if (!authority.isPresent())
            return false;

        RoleAuthority realAuthority = authority.get();

        AuthorityLocationRegistration registration = authorityRegistrationRepository.findByUsername(childUsername).get();

        String locationTypeOfRegistration = registration.getLocationType();

        if (locationTypeOfRegistration.equals(Constants.WARD_TYPE)) {
            if (realAuthority.getWard() == null)
                return false;

            String authorityLocation = realAuthority.getWard().getId();
            String childRequestLocation = registration.getLocationId();

            if (authorityLocation.equals(childRequestLocation)) {
                if (!accepting) {
                    registration.setRejected(true);
                    authorityRegistrationRepository.save(registration);
                }
                else {
                    RoleAuthority childAuthority = roleAuthorityRepository.findByUsername(childUsername).get();
                    childAuthority.setFarther(realAuthority);

                    Ward ward = wardRepository.findById(registration.getLocationId()).get();
                    childAuthority.setWard(ward);
                }
                return true;
            }
            else
                return false;
        }
        else if (locationTypeOfRegistration.equals(Constants.DISTRICT_TYPE)) {
            return false;
        }
        else if (locationTypeOfRegistration.equals(Constants.PROVINCE_TYPE)) {
            return false;
        }
        else
            return false;
    }

    public boolean checkExistsRequestOfUsername(String username, ERole eRole) {
        switch (eRole) {
            case ROLE_AUTHORITY:
                return authorityRegistrationRepository.existsByUsername(username);

            case ROLE_RESCUER:
                return rescuerRegistrationRepository.existsByUsername(username);

            case ROLE_VOLUNTEER:
                return volunteerRegistrationRepository.existsByUsername(username);

            default:
                return false;
        }
    }

    public boolean deleteRegistrationOfLocation(String username, String locationId, String locationType, ERole eRole) {
        switch (eRole) {
            case ROLE_AUTHORITY:

                if (!authorityRegistrationRepository.existsById(username))
                    return false;

                authorityRegistrationRepository.deleteById(username);

                break;

            case ROLE_RESCUER:

                if (!rescuerRegistrationRepository.existsById(username))
                    return false;

                rescuerRegistrationRepository.deleteById(username);

                break;

            case ROLE_VOLUNTEER:

                if (!volunteerRegistrationRepository.existsById(username))
                    return false;

                volunteerRegistrationRepository.deleteById(username);

                break;

            default:
                return false;
        }
        return true;
    }
}
