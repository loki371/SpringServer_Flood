package restAPI.payload;

import lombok.Getter;
import lombok.Setter;
import restAPI.models.UserInfo;

import java.util.List;

@Getter
@Setter
public class UserInfoList {
    private List<UserInfo> userInfoList;

    public UserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
