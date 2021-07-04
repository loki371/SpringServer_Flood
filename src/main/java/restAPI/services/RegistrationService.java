package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.ErrorCode;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Comment;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.models.role.*;
import restAPI.object_function.I_ObjectFunction;
import restAPI.payload.RegistrationPayload;
import restAPI.repository.registration.RegistrationRepository;
import restAPI.repository.role.RoleAuthorityRepository;
import restAPI.repository.role.RoleRescuerRepository;
import restAPI.repository.role.RoleUserRepository;
import restAPI.repository.role.RoleVolunteerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    @Autowired
    WardService wardService;

    @Autowired
    StateService stateService;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    RoleVolunteerRepository roleVolunteerRepository;

    @Autowired
    RoleRescuerRepository roleRescuerRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    UserInfoService userInfoService;

    public boolean checkValidRegistrationRequest(RegistrationPayload payload) {
        if (payload.getName() == null)
            return false;

        if (payload.getLatitude() < -90 || payload.getLatitude() > 90)
            return false;

        if (payload.getLongitude() < -180 || payload.getLongitude() > 180)
            return false;

        if (payload.getPhone() == null)
            return false;

        int sizePhone = payload.getPhone().length();
        if (sizePhone < 9 || sizePhone > 11)
            return false;

        if (payload.getNumPerson() <= 0)
            return false;

        if (payload.getWardId() == null)
            return false;

        if (!wardService.existWardId(
                payload.getWardId()
        ))
            return false;

        return true;
    }

    public Registration createNewRegistration(RegistrationPayload request, UserInfo userInfo){
        Registration registration = new Registration();
        registration.setName(request.getName());
        registration.setCommentList(new ArrayList<Comment>());
        registration.setCreateBy(userInfo);
        registration.setEState(EState.STATE_UNAUTHENTICATED);
        registration.setLongitude(request.getLongitude());
        registration.setLatitude(request.getLatitude());

        Ward ward = wardService.getWardByWardId(request.getWardId());
        registration.setWard(ward);
        System.out.println("ward = " + ward.getName() + " id = " + ward.getId());

        registration.setPhone(request.getPhone());
        registration.setSavedBy(null);
        registration.setNumPerson(request.getNumPerson());

        registrationRepository.save(registration);

        return registration;
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public List<Registration> getLocationRegistrations(String username) {
        System.out.println("getLocationRegistrations");
        List<Registration> result = null;
        Ward ward = null;

        Optional<RoleAuthority> authority = roleAuthorityRepository.findByUsername(username);
        if (authority.isPresent()) {
            RoleAuthority realAuthority = authority.get();
            ward = realAuthority.getWard();
            System.out.println("Authority: ward = " + ward.getId());
        } else {

            Optional<RoleRescuer> rescuer = roleRescuerRepository.findByUsername(username);
            if (rescuer.isPresent()) {
                RoleRescuer realRescuer = rescuer.get();
                ward = realRescuer.getWard();
                System.out.println("Rescuer: ward = " + ward.getId());

            } else {

                Optional<RoleVolunteer> volunteer = roleVolunteerRepository.findByUsername(username);
                if (volunteer.isPresent()) {
                    RoleVolunteer realVolunteer = volunteer.get();
                    ward = realVolunteer.getWard();
                    System.out.println("Volunteer: ward = " + ward.getId());

                }
            }
        }

        if (ward == null)
            return result;

        result = registrationRepository.findAllByWard(ward);
        for (Registration registration : result) {
            registration.setSavedBy(null);
            registration.setCreateBy(null);
        }

        return result;
    }

    public List<Registration> getMyRegistrations(String username) {
        System.out.println("getMyRegistrations");
        List<Registration> result = null;

        Optional<RoleUser> user = roleUserRepository.findByUsername(username);
        if (user.isPresent()) {
            UserInfo userInfo = userInfoService.getUserInfoByUsername(username);

            System.out.println("UserInfo = " + userInfo);

            result = registrationRepository.findAllByCreateBy(userInfo);
            for (Registration item : result) {
                item.setCreateBy(null);
                item.setSavedBy(null);
            }
        }

        return result;
    }

    public boolean changeStateRegistration(Registration registration, EState eState) {
        return true;
    }

    public List<Registration> changeNotifyFloodRegistrationAndGetAllRegistrations(String warId) {
        Ward ward = wardService.getWardByWardId(warId);
        if (ward == null)
            return null;

        List<Registration> registrations = registrationRepository.findAllByWard(ward);

        for (Registration item : registrations)
            if (item.getEState() == EState.STATE_AUTHENTICATED)
                item.setEState(EState.STATE_DANGER);

        registrationRepository.saveAll(registrations);
        return registrations;
    }

    public ErrorCode applyNewEStateToRegistration(Long regisId, String clientUsername, ERole clientRole, EState oldState, EState applyEState, I_ObjectFunction lastFunction) {
        Ward ward = userInfoService.getWardOfUserRole(clientUsername, clientRole);
        if (ward == null)
            return ErrorCode.CLIENT_WARD_NOT_VALID;

        Optional<Registration> registration = registrationRepository.findById(regisId);
        if (!registration.isPresent())
            return ErrorCode.REGISTRATION_NOT_FOUND;

        Registration realRegis = registration.get();
        if (!realRegis.getWard().getId().equals(ward.getId()))
            return ErrorCode.CLIENT_WARD_NOT_EQUAL_REGIS_WARD;

        if (realRegis.getEState() != oldState)
            return ErrorCode.NEW_ESTATE_ISNOT_VALID;

        realRegis.setEState(applyEState);

        if (lastFunction != null) {
            UserInfo userInfo = userInfoService.getUserInfoByUsername(clientUsername);
            lastFunction.setUserInfo(userInfo);
            lastFunction.setRegistration(realRegis);
            lastFunction.execute();
        }

        registrationRepository.save(realRegis);

        return ErrorCode.OK;
    }
}
