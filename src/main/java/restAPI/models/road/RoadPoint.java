package restAPI.models.road;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "road_points")
@Getter
@Setter
@NoArgsConstructor
public class RoadPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Road road;

    private int serial;

    private Float longitude;

    private Float latitude;

    public RoadPoint(int serial, Float longitude, Float latitude, Road road) {
        this.serial = serial;
        this.latitude = latitude;
        this.longitude = longitude;
        this.road = road;
    }
}
