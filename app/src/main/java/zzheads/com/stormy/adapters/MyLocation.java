package zzheads.com.stormy.adapters;

import android.location.Location;

/**
 * Created by zzhea on 04.07.2016.
 */
public class MyLocation extends Location {

    public MyLocation() {
        super("Test");
    }

    public MyLocation(double Lat, double Lng) {
        super("Test");
        setLatitude(Lat);
        setLongitude(Lng);
    }

    public MyLocation(String provider) {
        super(provider);
    }

    public MyLocation(Location l) {
        super(l);
    }

    public boolean equal (Location location) {
        return (Math.round(getLatitude()) == Math.round(location.getLatitude()) && (Math.round(getLongitude()) == Math.round(location.getLongitude())));
    }
}
