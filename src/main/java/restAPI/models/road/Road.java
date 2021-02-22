package restAPI.models.road;

import restAPI.models.location.Ward;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roads")
public class Road {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ward ward;

    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL)
    private List<RoadPoint> roadPointList;
}
