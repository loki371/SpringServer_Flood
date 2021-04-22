package restAPI.buckets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.location.District;

@Setter @Getter @NoArgsConstructor
public class DistrictBucket {
    private String id;
    private String name;

    public DistrictBucket(District district) {
        this.id = district.getId();
        this.name = district.getName();
    }
}
