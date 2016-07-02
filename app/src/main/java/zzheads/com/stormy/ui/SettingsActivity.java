package zzheads.com.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import zzheads.com.stormy.R;
import zzheads.com.stormy.adapters.Settings;

public class SettingsActivity extends ActionBarActivity {

    private boolean isCelsius = true;
    private String mLoc;
    private Settings mSettings = new Settings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView mLocTV, mTempTV;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        intent.getBooleanExtra("SETTINGS_TEMP",isCelsius);
        mLoc = intent.getStringExtra("SETTINGS_LOC");

        mTempTV = (TextView) findViewById(R.id.settingTempLabel);
        mLocTV = (TextView) findViewById(R.id.settingLocLabel);
        mLocTV.setText(Boolean.toString(isCelsius));
        mTempTV.setText(mLoc);
    }
}
