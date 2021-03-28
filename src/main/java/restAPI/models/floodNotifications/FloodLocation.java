package restAPI.models.floodNotifications;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.location.Ward;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor
public class FloodLocation {
    @Id
    public String wardId;
}
