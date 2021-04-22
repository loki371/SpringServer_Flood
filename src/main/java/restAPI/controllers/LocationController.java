package restAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restAPI.buckets.DistrictBucket;
import restAPI.buckets.ProvinceBucket;
import restAPI.buckets.WardBucket;
import restAPI.models.location.District;
import restAPI.models.location.Province;
import restAPI.models.location.Ward;
import restAPI.payload.SimplePayload;
import restAPI.repository.location.DistrictRepository;
import restAPI.repository.location.ProvinceRepository;
import restAPI.repository.location.WardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/api/locations")
public class LocationController {
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    WardRepository wardRepository;

    @GetMapping("/{locationType}/{locationId}")
    public HttpEntity<?> getLocationList(@PathVariable("locationType") String locationType,
                                         @PathVariable("locationId") String locationId) {
        switch (locationType) {
            case ("provinces"): {
                List<Province> provinceList = provinceRepository.findAll();
                List<ProvinceBucket> provinceBuckets = new ArrayList<>();
                for (Province item : provinceList) {
                    ProvinceBucket newItem = new ProvinceBucket(item);
                    provinceBuckets.add(newItem);
                }
                return new ResponseEntity<>(new SimplePayload("ok", provinceBuckets), HttpStatus.OK);
            }
            case ("districts"): {
                Province province = provinceRepository.findById(locationId).get();
                List<District> districtList = districtRepository.findAllByProvince(province);
                List<DistrictBucket> districtBuckets = new ArrayList<>();
                for (District item : districtList) {
                    DistrictBucket districtBucket = new DistrictBucket(item);
                    districtBuckets.add(districtBucket);
                }
                return new ResponseEntity<>(new SimplePayload("ok", districtBuckets), HttpStatus.OK);
            }

            case ("wards"): {
                District district = districtRepository.findById(locationId).get();
                List<Ward> wardList = wardRepository.findAllByDistrict(district);
                List<WardBucket> wardBuckets = new ArrayList<>();
                for (Ward item : wardList) {
                    WardBucket wardBucket = new WardBucket(item);
                    wardBuckets.add(wardBucket);
                }
                return new ResponseEntity<>(new SimplePayload("ok", wardBuckets), HttpStatus.OK);
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
