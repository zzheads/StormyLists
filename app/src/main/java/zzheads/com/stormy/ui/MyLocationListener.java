package zzheads.com.stormy.ui;

/**
 * Created by zzhea on 01.07.2016.
 */

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import zzheads.com.stormy.adapters.MyLocation;

class MyLocationListener implements LocationListener {

    private MyLocation imHere = new MyLocation();  // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    public MyLocationListener () {
        imHere.setLatitude(48+43/60+9/3600);
        imHere.setLongitude(44+30/60+6/3600);
    }

    public void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        Location loc;
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener); // здесь можно указать другие более подходящие вам параметры
        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        imHere.set(loc);
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

    public MyLocation getCoords () {
        return imHere;
    }
}