package restAPI.utils;

import restAPI.models.UserInfo;
import restAPI.models.registration.Registration;

public class RegistrationUtils {
    private static final double R = 6378.137; // Radius of earth in KM

    public static double distanceGPSToMeter(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function

        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters

    }

    public static void main(String[] args) {
        double lat1 = 10.75865276460488;
        double lon1 = 106.70936654474968;
        double lat2 = 10.757828984799932;
        double lon2 = 106.7088997181335;
        System.out.printf("distance from (" + lat1 + ", " + lon1 + ") to (" + lat2 + "," + lon2 + ") = ");
        System.out.println(distanceGPSToMeter(lat1, lon1, lat2, lon2));
    }
}
