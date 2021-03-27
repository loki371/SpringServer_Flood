package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.Comment;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.payload.RegistrationPayload;

import java.util.ArrayList;

@Service
public class RegistrationService {
    @Autowired
    WardService wardService;

    @Autowired
    StateService stateService;

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

        System.out.println("ward = " + ward);

        registration.setPhone(request.getPhone());
        registration.setSavedBy(null);
        return registration;
    }
}
