package restAPI.repository.road;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.road.RoadPoint;

public interface RoadPointRepository extends JpaRepository<RoadPoint, Long> {
}
