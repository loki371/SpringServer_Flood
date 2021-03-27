package restAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restAPI.models.location.Ward;
import restAPI.repository.location.WardRepository;

import java.util.Optional;

@Service
public class WardService {
    @Autowired
    WardRepository wardRepository;

    public boolean existWardId(String wardId) {
        return wardRepository.existsById(wardId);
    }

    public Ward getWardByWardId(String wardId) {
        Optional<Ward> ward = wardRepository.findById(wardId);
        return ward.orElse(null);
    }
}
