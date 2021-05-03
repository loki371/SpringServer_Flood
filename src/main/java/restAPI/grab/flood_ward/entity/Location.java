package restAPI.grab.flood_ward.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Location {
    public float longitude;
    public float latitude;

    public Location(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
