package restAPI.object_function;

import restAPI.models.UserInfo;
import restAPI.models.registration.Registration;

public interface I_ObjectFunction {
    public void setUserInfo(UserInfo userInfo);
    public void setRegistration(Registration registration);
    public void execute();
}
