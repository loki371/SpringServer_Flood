package restAPI.repository.road;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.road.Road;

public interface RoadRepository extends JpaRepository<Road, Long> {
}
