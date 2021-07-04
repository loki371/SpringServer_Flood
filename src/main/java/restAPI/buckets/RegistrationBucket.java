package restAPI.buckets;

import restAPI.models.location.Ward;
import restAPI.models.registration.EState;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

public class RegistrationBucket {
    public Long id;

    public String name;

    public Float longitude;

    public Float latitude;

    public Ward ward;

    public String phone;

    public int numPerson;

    public EState eState;

    public int order;
}
