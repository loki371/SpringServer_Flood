package restAPI.object_function;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.registration.Registration;
import restAPI.object_function.I_ObjectFunction;

@Getter @Setter @NoArgsConstructor
public class SetSavedByFunction implements I_ObjectFunction {
    private UserInfo userInfo;
    private Registration registration;

    public void execute() {
        registration.setSavedBy(userInfo);
    }
}
