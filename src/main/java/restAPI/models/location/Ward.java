package restAPI.models.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import restAPI.models.road.Road;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wards")
@Getter @Setter
public class Ward {
    @Id
    @Column(columnDefinition = "CHAR(6)")
    private String id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    @Column(columnDefinition="VARCHAR(45)")
    private String type;

    @ManyToOne
    @JoinColumn(name = "district_id")
    @JsonIgnore
    private District district;
}
