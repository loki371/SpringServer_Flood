package restAPI.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restAPI.models.location.District;
import restAPI.models.location.Province;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    public List<District> findAllByProvince(Province province);
}
