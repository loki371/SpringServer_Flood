package restAPI.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.location.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    boolean existsById(String id);
}
