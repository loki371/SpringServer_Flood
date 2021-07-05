package restAPI.buckets;

import restAPI.models.locationRegistration.AuthorityLocationRegistration;
import restAPI.models.locationRegistration.RescuerLocationRegistration;
import restAPI.models.locationRegistration.VolunteerLocationRegistration;
import restAPI.models.registration.EState;

public class LocationRegistrationBucket {
    protected String username;

    protected String locationType;

    protected String locationId;

    protected EState eState;

    public LocationRegistrationBucket(AuthorityLocationRegistration item) {
        this.username = item.getUsername();
        this.locationId = item.getLocationId();
        this.locationType = item.getLocationType();
        this.eState = item.getEstate();
    }

    public LocationRegistrationBucket(VolunteerLocationRegistration item) {
        this.username = item.getUsername();
        this.locationId = item.getLocationId();
        this.locationType = item.getLocationType();
        this.eState = item.getEstate();
    }

    public LocationRegistrationBucket(RescuerLocationRegistration item) {
        this.username = item.getUsername();
        this.locationId = item.getLocationId();
        this.locationType = item.getLocationType();
        this.eState = item.getEstate();
    }
}
