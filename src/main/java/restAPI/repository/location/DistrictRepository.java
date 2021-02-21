package restAPI.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.location.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
}
