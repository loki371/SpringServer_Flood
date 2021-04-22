package restAPI.buckets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.location.Province;

import javax.persistence.Column;

@NoArgsConstructor @Getter @Setter
public class ProvinceBucket {
    private String id;
    private String name;

    public ProvinceBucket(Province province) {
        this.id = province.getId();
        this.name = province.getName();
    }
}
