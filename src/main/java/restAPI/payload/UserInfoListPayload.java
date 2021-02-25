package restAPI.payload;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.UserInfo;

import java.util.List;

@Getter
@Setter
public class UserInfoListPayload {
    private List<UserInfo> userInfoList;

    public UserInfoListPayload(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
