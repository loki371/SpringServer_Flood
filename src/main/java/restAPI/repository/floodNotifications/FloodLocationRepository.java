package restAPI.repository.floodNotifications;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.floodNotifications.FloodLocation;

public interface FloodLocationRepository extends JpaRepository<FloodLocation, String> {
    boolean existsByWardId(String id);
}
