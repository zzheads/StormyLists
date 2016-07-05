package zzheads.com.stormy.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import zzheads.com.stormy.R;
import zzheads.com.stormy.adapters.Settings;

public class SettingsActivity extends ActionBarActivity {

    private String[] mCities;
    public Settings mSettings;
    private boolean itemSelectedFirstTime = true;

    @InjectView(R.id.switchTemp) Switch switchTemp;
    @InjectView(R.id.switchLoc) Switch switchLoc;
    @InjectView(R.id.spinnerCities) Spinner spinnerCities;
    @InjectView(R.id.coordsTextView) TextView coordsTextView;
    @InjectView(R.id.cityTextView) TextView cityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        mSettings = new Settings(this);
        mCities = getResources().getStringArray(R.array.c_array);

        mSettings.Load();
        spinnerCities.setEnabled(mSettings.isLocManual());
        switchTemp.setChecked(mSettings.isCelsius());
        switchLoc.setChecked(mSettings.isLocManual());
        coordsTextView.setText(String.format("Lat:%.2f Lng:%.2f",mSettings.getCurrentLocation().getLatitude(),mSettings.getCurrentLocation().getLongitude()));
        cityTextView.setText(mSettings.getCity());
        itemSelectedFirstTime = true;
        for (int i=0;i<mCities.length;i++) {
            if (mCities[i].equals(mSettings.getCity())) {
                spinnerCities.setSelection(i);
            }
        }
    }

    @OnClick (R.id.switchTemp) public void switchTemperature (View view) {
        mSettings.setCelsius(switchTemp.isChecked());
        mSettings.Save();
    }

    @OnClick (R.id.switchLoc) public void switchLocation (View view) {
        mSettings.setLocManual(switchLoc.isChecked());
        if (switchLoc.isChecked()) {
            mSettings.setCity(mCities[spinnerCities.getSelectedItemPosition()]); // по городу устанавливаем координаты
            mSettings.setCurrentLocation(mSettings.findCoords(mSettings.getCity()));
            spinnerCities.setEnabled(true);
        } else {
            mSettings.setCity(mSettings.findCityByLoc(MyLocationListener.getLastKnownPostion(this)));
            spinnerCities.setEnabled(false);
        }
        mSettings.Save();
    }

    @OnItemSelected (R.id.spinnerCities) public void changedCity () {
        if (itemSelectedFirstTime) { // пользователь ничего не выбирал, вызов из onCreate() поэтому ничего в установках не менеям
            itemSelectedFirstTime = false;
            return;
        } else {
            mSettings.setCity(spinnerCities.getSelectedItem().toString());
            Location loc = mSettings.findCoords(mSettings.getCity());
            if (loc!=null) {
                mSettings.setCurrentLocation(loc);
                coordsTextView.setText(String.format("Lat:%.2f Lng:%.2f",mSettings.getCurrentLocation().getLatitude(),mSettings.getCurrentLocation().getLongitude()));
            } else {
                coordsTextView.setText("Coordinates unknown");
            }
            cityTextView.setText(mSettings.getCity());
            coordsTextView.setText(String.format("Lat:%.2f Lng:%.2f",mSettings.getCurrentLocation().getLatitude(),mSettings.getCurrentLocation().getLongitude()));
            mSettings.Save();
        }
    }

}
