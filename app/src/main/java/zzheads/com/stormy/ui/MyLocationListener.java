package zzheads.com.stormy.ui;

/**
 * Created by zzhea on 01.07.2016.
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

class MyLocationListener implements LocationListener {

    private Map<String, Location> mCities= new HashMap<String, Location>();
//                    {{
//                        put("Волгоград", new LatLng (48+43/60+9/3600, 44+30/60+6/3600));
//                        put("Москва", new LatLng (55+45/60+7/3600, 37+36/60+56/3600));
//                        put("Нью Йорк", new LatLng (40.7142700, -74.0059700));
//                        put("Лондон", new LatLng (51.5085300, -0.1257400));
//                        put("Рио де Жанейро", new LatLng (-22.9027800, -43.2075000));
//                    }};


    private Location imHere = new Location ("Test");  // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    public Map<String, Location> getCities() {
        return mCities;
    }

    public MyLocationListener () {
        imHere.setLatitude(48+43/60+9/3600);
        imHere.setLongitude(44+30/60+6/3600);
    }

    public void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener); // здесь можно указать другие более подходящие вам параметры
        imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public Location getCoords () {
        return imHere;
    }
}