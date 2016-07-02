package zzheads.com.stormy.adapters;

import android.location.Location;

/**
 * Created by zzhea on 02.07.2016.
 */
public class Settings  {
    boolean mCelsius;
    Location mCurrentLocation;

    public Settings() {
    }

    public Settings(boolean celsius, Location currentLocation) {
        mCelsius = celsius;
        mCurrentLocation = currentLocation;
    }

    public boolean isCelsius() {

        return mCelsius;
    }

    public void setCelsius(boolean celsius) {
        mCelsius = celsius;
    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        mCurrentLocation = currentLocation;
    }

}
