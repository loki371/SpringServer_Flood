package restAPI.models.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.District;
import restAPI.models.location.Province;
import restAPI.models.location.Ward;
import restAPI.models.registration.Registration;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role_user")
@Getter @Setter @NoArgsConstructor
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private UserInfo userInfo;

    @ManyToMany
    @JoinTable( name = "subscriber_registration",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "registration_id")
    )
    private List<Registration> registrationList;

    @ManyToMany
    @JoinTable( name = "favorite_ward",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ward_id")
    )
    private List<Ward> favoriteWards;

    @ManyToMany
    @JoinTable( name = "favorite_district",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "district_id")
    )
    private List<District> favoriteDistrict;

    @ManyToMany
    @JoinTable( name = "favorite_province",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "province_id")
    )
    private List<Province> favoriteProvince;

    public RoleUser(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.registrationList = new ArrayList<>();
        this.favoriteWards = new ArrayList<>();
        this.favoriteDistrict = new ArrayList<>();
        this.favoriteProvince = new ArrayList<>();
    }
}
