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
import restAPI.models.role.RoleRescuer;
import restAPI.models.role.RoleVolunteer;
import restAPI.repository.location.WardRepository;
import restAPI.repository.locationRegistration.AuthorityRegistrationRepository;
import restAPI.repository.locationRegistration.RescuerRegistrationRepository;
import restAPI.repository.locationRegistration.VolunteerRegistrationRepository;
import restAPI.repository.role.RoleAuthorityRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleVolunteerRepository;

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
    RoleRescuerRepository roleRescuerRepository;

    @Autowired
    RoleVolunteerRepository roleVolunteerRepository;

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

    public List<Object> getListAllLocation(ERole eRole, String username) {
        List<Object> result = new ArrayList<>();

        RoleAuthority roleAuthority = roleAuthorityRepository.findByUsername(username).get();

        Ward ward = roleAuthority.getWard();

        if (roleAuthority.getWard() == null)
            return result;

        switch (eRole) {
            case ROLE_AUTHORITY:
                List<AuthorityLocationRegistration> list1 = authorityRegistrationRepository.findAllByLocationId(ward.getId());
                result.addAll(list1);
                break;

            case ROLE_RESCUER:
                List<RescuerLocationRegistration> list2 = rescuerRegistrationRepository.findAllByLocationId(ward.getId());
                result.addAll(list2);
                break;

            case ROLE_VOLUNTEER:
                List<VolunteerLocationRegistration> list3 = volunteerRegistrationRepository.findAllByLocationId(ward.getId());
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
            if (realAuthority.getWard() == null) {
                System.out.println("Authority ward = NULL");
                return false;
            }

            String authorityLocation = realAuthority.getWard().getId();
            String childRequestLocation = registration.getLocationId();

            if (authorityLocation.equals(childRequestLocation) || fatherUsername.equals("SuperAdmin")) {
                if (!accepting) {
                    registration.setRejected(true);
                    authorityRegistrationRepository.save(registration);

                    System.out.println("name: " + childUsername + " accepting: false");
                }
                else {
                    authorityRegistrationRepository.delete(registration);

                    RoleAuthority childAuthority = roleAuthorityRepository.findByUsername(childUsername).get();
                    childAuthority.setFarther(realAuthority);

                    Ward ward = wardRepository.findById(registration.getLocationId()).get();
                    childAuthority.setWard(ward);
                    roleAuthorityRepository.save(childAuthority);

                    System.out.println("name: " + childUsername + " accepting: true");
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

    public boolean processRescuerRegistration(String fatherUsername, String childUsername, boolean accepting) {
        System.out.println("into processRescuerRegistration");

        Optional<RoleAuthority> authority = roleAuthorityRepository.findByUsername(fatherUsername);
        if (!authority.isPresent()) {
            System.out.println("father authority is not exists");
            return false;
        }

        RoleAuthority realAuthority = authority.get();

        RescuerLocationRegistration registration = rescuerRegistrationRepository.findByUsername(childUsername).get();

        String locationTypeOfRegistration = registration.getLocationType();
        if (locationTypeOfRegistration.equals(Constants.WARD_TYPE)) {
            if (realAuthority.getWard() == null) {
                System.out.println("Authority ward = NULL");
                return false;
            }

            String location = realAuthority.getWard().getId();
            String childRequestLocation = registration.getLocationId();

            if (location.equals(childRequestLocation)) {
                if (!accepting) {

                    registration.setRejected(true);
                    rescuerRegistrationRepository.save(registration);

                    System.out.println("name: " + childUsername + " accepting: false");
                } else {
                    rescuerRegistrationRepository.delete(registration);

                    RoleRescuer child = roleRescuerRepository.findByUsername(childUsername).get();
                    Ward ward = wardRepository.findById(registration.getLocationId()).get();
                    child.setWard(ward);
                    roleRescuerRepository.save(child);

                    System.out.println("name: " + childUsername + " accepting: true");
                }
                return true;
            } else
                return false;
        } else if (locationTypeOfRegistration.equals(Constants.DISTRICT_TYPE)) {
            return false;

        } else if (locationTypeOfRegistration.equals(Constants.PROVINCE_TYPE)) {
            return false;

        } else
            return false;
    }

    public boolean processVolunteerRegistration(String fatherUsername, String childUsername, boolean accepting) {
        System.out.println("into processVolunteerRegistration");

        Optional<RoleAuthority> authority = roleAuthorityRepository.findByUsername(fatherUsername);
        if (!authority.isPresent()) {
            System.out.println("father authority is not exists");
            return false;
        }

        RoleAuthority realAuthority = authority.get();

        VolunteerLocationRegistration registration = volunteerRegistrationRepository.findByUsername(childUsername).get();

        String locationTypeOfRegistration = registration.getLocationType();
        if (locationTypeOfRegistration.equals(Constants.WARD_TYPE)) {
            if (realAuthority.getWard() == null) {
                System.out.println("Authority ward = NULL");
                return false;
            }

            String location = realAuthority.getWard().getId();
            String childRequestLocation = registration.getLocationId();

            if (location.equals(childRequestLocation)) {
                if (!accepting) {

                    registration.setRejected(true);
                    volunteerRegistrationRepository.save(registration);

                    System.out.println("name: " + childUsername + " accepting: false");
                } else {
                    volunteerRegistrationRepository.delete(registration);

                    RoleVolunteer child = roleVolunteerRepository.findByUsername(childUsername).get();
                    Ward ward = wardRepository.findById(registration.getLocationId()).get();
                    child.setWard(ward);
                    roleVolunteerRepository.save(child);

                    System.out.println("name: " + childUsername + " accepting: true");
                }
                return true;
            } else
                return false;
        } else if (locationTypeOfRegistration.equals(Constants.DISTRICT_TYPE)) {
            return false;

        } else if (locationTypeOfRegistration.equals(Constants.PROVINCE_TYPE)) {
            return false;

        } else
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
