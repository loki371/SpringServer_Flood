package restAPI.buckets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.location.District;
import restAPI.models.location.Ward;

@Setter
@Getter
@NoArgsConstructor
public class WardBucket {
    private String id;
    private String name;

    public WardBucket(Ward ward) {
        this.id = ward.getId();
        this.name = ward.getName();
    }
}