package restAPI.buckets;

import restAPI.models.UserInfo;
import restAPI.models.registration.EState;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class DangKyQuyen {
    public String username;
    public String phone;
    public String email;
    public EState estate;
}
