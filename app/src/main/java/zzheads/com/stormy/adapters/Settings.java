package zzheads.com.stormy.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import zzheads.com.stormy.R;

/**
 * Created by zzhea on 02.07.2016.
 */
public class Settings  {

    private Context mContext;
    private boolean mCelsius;
    private boolean mLocManual;
    public Location mCurrentLocation = new Location("GPS");
    private String mCity;

    public static Map<String, Location> mCities= new HashMap<String, Location>();

    public Settings (Context context) {
        mContext = context;
        initializeCities();
    }

    public Settings(Context context, boolean celsius, boolean locManual, Location loc, String city) {
        mContext = context;
        mCelsius = celsius;
        mLocManual = locManual;
        mCurrentLocation = loc;
        mCity = city;
        initializeCities();
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

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public static Location findCoords (String city) {
        return (mCities.get(city));
    }

    public static String findCityByLoc (Location loc) {
        Set<Map.Entry<String,Location>> entrySet= mCities.entrySet();
        String city="";
        if (mCities.containsValue(loc)) {
            for (Map.Entry<String,Location> pair : entrySet) {
                //if (loc.distanceTo(pair.getValue())<5000) { // если наша точка в радиусе 5000 м от известного города из списка - считаем что совпали
                if (loc.equals(pair.getValue())) {
                    city = pair.getKey();// нашли наше значение и возвращаем  ключ
                }
            }
        } else {
            city = "Unknown";
        }
        return city;
    }

    public void initializeCities() { // инициализирует Map<city, loc> данными из массива c_array (strings.xml)
        Location loc;

        Resources res = mContext.getResources();
        String[] cities = res.getStringArray(R.array.c_array);
        String[] lat = res.getStringArray(R.array.lat_array);
        String[] lng = res.getStringArray(R.array.lng_array);
        for (int i = 0; i < cities.length; i++) {
            loc = new Location("GPS");
            loc.setLatitude(Double.parseDouble(lat[i]));
            loc.setLongitude(Double.parseDouble(lng[i]));
            mCities.put(cities[i], loc);
        }
    }

    public void Load() {
        SharedPreferences preferences = mContext.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        mCelsius = preferences.getBoolean("TEMP", true);
        mLocManual = preferences.getBoolean("LOCMANUAL", false);
        mCurrentLocation.setLatitude(preferences.getFloat("LOC_LAT",(float)0));
        mCurrentLocation.setLongitude(preferences.getFloat("LOC_LON",(float)0));
        mCity = preferences.getString("CITY", "");
    }

    public void Save () {
        SharedPreferences preferences = mContext.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        preferences.edit().putBoolean("TEMP", mCelsius).apply();
        preferences.edit().putBoolean("LOCMANUAL", mLocManual).apply();
        preferences.edit().putFloat("LOC_LAT", (float) mCurrentLocation.getLatitude()).apply();
        preferences.edit().putFloat("LOC_LON", (float) mCurrentLocation.getLongitude()).apply();
        preferences.edit().putString("CITY", mCity).apply();
    }
}
