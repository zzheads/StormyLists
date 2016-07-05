package zzheads.com.stormy.ui;

/**
 * Created by zzhea on 01.07.2016.
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

class MyLocationListener implements LocationListener {

    public Location imHere = new Location("GPS");  // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    //public MyLocationListener () {
    //    imHere = new MyLocation();
    //    imHere.setLatitude(48.7193900); // 48.7193900, 44.5018400
    //    imHere.setLongitude(44.5018400);
    //}

    public static Location getLastKnownPostion (Context context) {
        Location loc;
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener); // здесь можно указать другие более подходящие вам параметры
        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return loc;
    }

    public boolean SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        Location loc;
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener); // здесь можно указать другие более подходящие вам параметры
        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc!=null) {
            imHere.set(loc);
            return true;
        }
        return false; // последнее местоположение неизвестно
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere.set(loc);
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