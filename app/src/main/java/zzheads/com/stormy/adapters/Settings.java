package zzheads.com.stormy.adapters;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zzhea on 02.07.2016.
 */
public class Settings  {

    private boolean mCelsius;
    private boolean mLocManual;
    private MyLocation mCurrentLocation = new MyLocation();
    private String mCity;
    private Map<String, MyLocation> mCities= new HashMap<String, MyLocation>()
                    {{
                        put("Volgograd", new MyLocation (48.7193900, 44.5018400));
                        put("Moscow", new MyLocation (55.7522200, 37.6155600));
                        put("New York", new MyLocation (40.7142700, -74.0059700));
                        put("London", new MyLocation (51.5085300, -0.1257400));
                        put("Rio de Janeiro", new MyLocation (-22.9027800, -43.2075000));
                        put("Pekin", new MyLocation (39.9075000, 116.3972300));
                    }};


    public Settings() {
    }

    public Settings(boolean celsius, boolean locManual, String city) {
        mCelsius = celsius;
        mLocManual = locManual;
        mCity = city;
    }

    public Map<String, MyLocation> getCities () {
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

    public MyLocation getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(MyLocation currentLocation) {
        mCurrentLocation = currentLocation;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public MyLocation findCoords (String city) {
        return (mCities.get(city));
    }

    public String findCity (MyLocation loc) {
        Set<Map.Entry<String, MyLocation>> entrySet =mCities.entrySet();
        for (Map.Entry<String,MyLocation> pair : entrySet) {
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
