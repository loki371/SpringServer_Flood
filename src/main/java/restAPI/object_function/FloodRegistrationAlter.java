package restAPI.object_function;

import restAPI.grab.FloodWardService;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;
import restAPI.models.registration.EState;
import restAPI.models.registration.Registration;
import restAPI.repository.registration.RegistrationRepository;

public class FloodRegistrationAlter implements I_ObjectFunction{
    private UserInfo userInfo;
    private Registration registration;
    private FloodWardService floodWardService;
    private EState oldState;
    private EState newState;
    private RegistrationRepository repo;

    @Override
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public void setFloodWardService(FloodWardService service) {
        this.floodWardService = service;
    }

    public void setOldState(EState state) { this.oldState = state; }

    public void setNewState(EState state) { this.newState = state; }

    public void setRegistrationRepository(RegistrationRepository repo) { this.repo = repo; }

    @Override
    public void execute() {
        Ward ward = registration.getWard();

        if (!floodWardService.checkInFlood(ward.getId()))
            return;

        switch (oldState) {
            case STATE_DANGER:
            case STATE_SAFE:
            case STATE_SAVED:
                if (newState == EState.STATE_EMERGENCY)
                    floodWardService.addRegistrationToFloodWard(ward.getId(), registration);

                break;

            case STATE_EMERGENCY:
                if (newState == EState.STATE_SAFE || newState == EState.STATE_SAVED || newState == EState.STATE_DANGER)
                    floodWardService.removeRegistrationFromFloodWard(ward.getId(), registration);
                break;

            case STATE_UNAUTHENTICATED:
                if (newState == EState.STATE_AUTHENTICATED) {

                    registration.setEState(EState.STATE_DANGER);

                    repo.save(registration);

                    //floodWardService.addRegistrationToFloodWard(ward.getId(), registration);
                }
                break;
        }
    }
}
