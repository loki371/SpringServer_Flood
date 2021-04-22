package restAPI.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.location.District;
import restAPI.models.location.Ward;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    boolean existsById(String id);
    public List<Ward> findAllByDistrict(District locationId);
}
