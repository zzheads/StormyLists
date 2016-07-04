package zzheads.com.stormy.ui;

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
import zzheads.com.stormy.adapters.MyLocation;
import zzheads.com.stormy.adapters.Settings;

public class SettingsActivity extends ActionBarActivity {

    private Settings mSettings = new Settings();
    private String[] mCities;

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

        mCities = getResources().getStringArray(R.array.c_array);
        mSettings.Load(this);
        spinnerCities.setEnabled(mSettings.isLocManual());
        switchTemp.setChecked(mSettings.isCelsius());
        switchLoc.setChecked(mSettings.isLocManual());
        coordsTextView.setText(String.format("Lat:%.2f Lng:%.2f",mSettings.getCurrentLocation().getLatitude(),mSettings.getCurrentLocation().getLongitude()));
        cityTextView.setText(mSettings.getCity());
        for (int i=0;i<mCities.length;i++) {
            if (mCities[i].equals(mSettings.getCity())) {
                spinnerCities.setSelection(i);
            }
        }
    }

    @OnClick (R.id.switchTemp) public void switchTemperature (View view) {
        mSettings.setCelsius(switchTemp.isChecked());
        mSettings.Save(this);
    }

    @OnClick (R.id.switchLoc) public void switchLocation (View view) {
        mSettings.setLocManual(switchLoc.isChecked());
        if (switchLoc.isChecked()) {
            mSettings.setCity(mCities[spinnerCities.getSelectedItemPosition()]); // по городу устанавливаем координаты
            mSettings.setCurrentLocation(mSettings.findCoords(mSettings.getCity()));
            spinnerCities.setEnabled(true);
        } else {
            mSettings.setCity(mSettings.findCity((MyLocation) mSettings.getCurrentLocation()));
            spinnerCities.setEnabled(false);
        }
        mSettings.Save(this);
    }

    @OnItemSelected (R.id.spinnerCities) public void changedCity () {
        mSettings.setCity((String) spinnerCities.getSelectedItem());
        if (switchLoc.isChecked()) {
            mSettings.setCity(mCities[spinnerCities.getSelectedItemPosition()]); // по городу устанавливаем координаты
            mSettings.setCurrentLocation(mSettings.findCoords(mSettings.getCity()));
        } else {
            mSettings.setCity(mSettings.findCity((MyLocation) mSettings.getCurrentLocation()));
        }
        cityTextView.setText(mSettings.getCity());
        coordsTextView.setText(String.format("Lat:%.2f Lng:%.2f",mSettings.getCurrentLocation().getLatitude(),mSettings.getCurrentLocation().getLongitude()));
        mSettings.Save(this);
    }

}
