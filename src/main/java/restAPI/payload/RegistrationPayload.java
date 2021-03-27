package restAPI.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.location.Ward;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor
public class RegistrationPayload {
    private Long id;

    private String name;

    private Float longitude;

    private Float latitude;

    private String wardId;

    private String phone;

    private int numPerson;

}
