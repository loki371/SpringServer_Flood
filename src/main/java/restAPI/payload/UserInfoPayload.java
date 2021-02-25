package restAPI.payload;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.UserInfo;

@Getter @Setter
public class UserInfoPayload {
    UserInfo userInfo;

    public UserInfoPayload(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
