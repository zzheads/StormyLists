package zzheads.com.stormy.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zzheads.com.stormy.R;
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
        UpdateDisplay();
    }

    @Override
    protected  void onStop() {
        ReadFromDisplay();
        mSettings.Save(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCities = getResources().getStringArray(R.array.c_array);
        mSettings.Load(this);
        UpdateDisplay();
    }

    private void UpdateDisplay () {
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

    private void ReadFromDisplay () {
        mSettings.setCelsius(switchTemp.isChecked());
        mSettings.setLocManual(switchLoc.isChecked());
        mSettings.setCity(mCities[spinnerCities.getSelectedItemPosition()]);
    }

}
