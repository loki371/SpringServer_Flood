package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.registration.RegisOrder;
import restAPI.repository.registration.RegisOrderRepository;

import java.util.Optional;

@Service
public class RegisOrderService {
    @Autowired
    RegisOrderRepository regisOrderRepository;

    public static final int ORDER_EMERGENCY = 0; // cap cuu: nguoi benh, phu nu sap sinh
    public static final int ORDER_MORE_DANGER = 1; // cap cuu: nhieu tre nho
    public static final int ORDER_DANGER = 2;   // cap cuu: nhieu nguoi gia, phu nu
    public static final int ORDER_NORMAL = 3;   // cap cuu: con lai

    public int getOrderById(long regisId) {
        Optional<RegisOrder> regisOrderOptional = regisOrderRepository.findById(regisId);
        return regisOrderOptional.map(RegisOrder::getOrder).orElse(ORDER_NORMAL);
    }
}
