package zzheads.com.stormy.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zzhea on 02.07.2016.
 */
public class Settings  {

    private boolean mCelsius;
    private boolean mLocManual;
    public Location mCurrentLocation = new Location("GPS");
    private String mCity;

    public static Map<String, Location> mCities= new HashMap<String, Location>();
                    //{{
                    //    put("Volgograd", new Location (48.7193900, 44.5018400));
                    //    put("Moscow", new Location (55.7522200, 37.6155600));
                    //                        put("New York", new Location (40.7142700, -74.0059700));
                    //                        put("London", new Location (51.5085300, -0.1257400));
                    //                        put("Rio de Janeiro", new Location (-22.9027800, -43.2075000));
                    //                        put("Pekin", new Location (39.9075000, 116.3972300));
                    //                        put("Sochi", new Location (43.5991700, 39.7256900));
                    //                        put("Krasnodar", new Location ( 45.0448400, 38.9760300));
                    //                        put("Sankt Peterburg", new Location (59.9386300, 30.3141300));
                    //                        put("Paris", new Location ( 48.8534100, 2.3488000));
                    //                        put("Los Angeles", new Location ( 34.0522300,-118.2436800));
                    //                    }};

    public void initializeCities() {
        Location loc = new Location("Test");
        String city = "";

        city = "Volgograd";
        loc.setLatitude(48.7193900);
        loc.setLongitude(44.5018400);
        mCities.put(city, loc);

        city = "Moscow";
        loc.setLatitude(55.7522200);
        loc.setLongitude(37.6155600);
        mCities.put(city, loc);

        city = "New York";
        loc.setLatitude(40.7142700);
        loc.setLongitude(-74.0059700);
        mCities.put(city, loc);

        city = "London";
        loc.setLatitude(51.5085300);
        loc.setLongitude(-0.1257400);
        mCities.put(city, loc);

        city = "Rio de Janeiro";
        loc.setLatitude(-22.9027800);
        loc.setLongitude(-43.2075000);
        mCities.put(city, loc);

        city = "Pekin";
        loc.setLatitude(39.9075000);
        loc.setLongitude(116.3972300);
        mCities.put(city, loc);

        city = "Sochi";
        loc.setLatitude(43.5991700);
        loc.setLongitude(39.7256900);
        mCities.put(city, loc);

        city = "Krasnodar";
        loc.setLatitude(45.0448400);
        loc.setLongitude(38.9760300);
        mCities.put(city, loc);

        city = "Sankt Peterburg";
        loc.setLatitude(59.9386300);
        loc.setLongitude(30.3141300);
        mCities.put(city, loc);

        city = "Paris";
        loc.setLatitude(48.8534100);
        loc.setLongitude(2.3488000);
        mCities.put(city, loc);

        city = "Los Angeles";
        loc.setLatitude(34.0522300);
        loc.setLongitude(-118.2436800);
        mCities.put(city, loc);
    }

    public Settings () {
        initializeCities();
    }
    public Settings(boolean celsius, boolean locManual, Location loc, String city) {
        initializeCities();
        mCelsius = celsius;
        mLocManual = locManual;
        mCurrentLocation = loc;
        mCity = city;
    }

    public static String findCityByLoc (Location loc) {
        Set<Map.Entry<String,Location>> entrySet= mCities.entrySet();
        String city="";
        if (mCities.containsValue(loc)) {
            for (Map.Entry<String,Location> pair : entrySet) {
                if (loc.equals(pair.getValue())) {
                    city = pair.getKey();// нашли наше значение и возвращаем  ключ
                }
            }
        } else {
            city = "Unknown";
        }
        return city;
    }

    public Map<String, Location> getCities () {
        return mCities;
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

    public Location findCoords (String city) {
        return (mCities.get(city));
    }

    public String findCity (Location loc) {
        Set<Map.Entry<String, Location>> entrySet =mCities.entrySet();
        for (Map.Entry<String,Location> pair : entrySet) {
            if (loc.equals(pair.getValue())) {
                return pair.getKey();// нашли наше значение и возвращаем  ключ
            }
        }
        return "Not found";
    }

    public void Load(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        mCelsius = preferences.getBoolean("TEMP", true);
        mLocManual = preferences.getBoolean("LOCMANUAL", false);
        mCurrentLocation.setLatitude(preferences.getFloat("LOC_LAT",(float)0));
        mCurrentLocation.setLongitude(preferences.getFloat("LOC_LON",(float)0));
        mCity = preferences.getString("CITY", "");
    }

    public void Save (Context context) {

        SharedPreferences preferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        preferences.edit().putBoolean("TEMP", mCelsius).apply();
        preferences.edit().putBoolean("LOCMANUAL", mLocManual).apply();
        preferences.edit().putFloat("LOC_LAT", (float) mCurrentLocation.getLatitude()).apply();
        preferences.edit().putFloat("LOC_LON", (float) mCurrentLocation.getLongitude()).apply();
        preferences.edit().putString("CITY", mCity).apply();
    }
}
