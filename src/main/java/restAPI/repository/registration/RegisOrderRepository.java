package restAPI.repository.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import restAPI.models.registration.RegisOrder;


import java.util.List;
import java.util.Optional;

public interface RegisOrderRepository extends JpaRepository<RegisOrder, Long> {
    public List<RegisOrder> findAllByWardId(String wardId);
    public Optional<RegisOrder> findById(long regisId);
}