package restAPI.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.location.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
}
