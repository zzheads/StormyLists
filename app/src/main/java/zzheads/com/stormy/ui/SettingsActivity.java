package zzheads.com.stormy.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Spinner;
import android.widget.Switch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zzheads.com.stormy.R;
import zzheads.com.stormy.adapters.Settings;

public class SettingsActivity extends ActionBarActivity {

    private Settings mSettings = new Settings();

    @InjectView(R.id.switchTemp) Switch switchTemp;
    @InjectView(R.id.switchLoc) Switch switchLoc;
    @InjectView(R.id.spinnerCities) Spinner spinnerCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);

        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        mSettings.Load(preferences);
        UpdateDisplay();
    }

    @Override
    protected  void onStop() {
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        ReadFromDisplay();
        mSettings.Save(preferences);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        mSettings.Load(preferences);
        UpdateDisplay();
    }

    private void UpdateDisplay () {
        switchTemp.setChecked(mSettings.isCelsius());
        switchLoc.setChecked(mSettings.isLocManual());
    }

    private void ReadFromDisplay () {
        mSettings.setCelsius(switchTemp.isChecked());
        mSettings.setLocManual(switchLoc.isChecked());
    }

}
