package zzheads.com.stormy.adapters;

import android.content.SharedPreferences;
import android.location.Location;

/**
 * Created by zzhea on 02.07.2016.
 */
public class Settings  {
    boolean mCelsius;
    boolean mLocManual;
    Location mCurrentLocation = new Location("Test");
    String mCity;

    public Settings() {
    }

    public Settings(boolean celsius, boolean locManual, Location currentLocation) {
        mCelsius = celsius;
        mLocManual = locManual;
        mCurrentLocation = currentLocation;
    }

    public boolean isLocManual() {
        return mLocManual;
    }

    public void setLocManual(boolean locManual) {
        mLocManual = locManual;
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

    public void Load(SharedPreferences preferences) {
        mCelsius = preferences.getBoolean("TEMP", true);
        mLocManual = preferences.getBoolean("LOCMANUAL", false);
        mCurrentLocation.setLatitude(preferences.getFloat("LOC_LAT",(float)0));
        mCurrentLocation.setLongitude(preferences.getFloat("LOC_LON",(float)0));
        mCity = preferences.getString("CITY", "");
    }

    public void Save (SharedPreferences preferences) {
        preferences.edit().putBoolean("TEMP", mCelsius).apply();
        preferences.edit().putBoolean("LOCMANUAL", mLocManual).apply();
        preferences.edit().putFloat("LOC_LAT", (float) mCurrentLocation.getLatitude()).apply();
        preferences.edit().putFloat("LOC_LON", (float) mCurrentLocation.getLongitude()).apply();
        preferences.edit().putString("CITY", mCity).apply();
    }


}
