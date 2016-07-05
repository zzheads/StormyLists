package zzheads.com.stormy.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import zzheads.com.stormy.R;
import zzheads.com.stormy.adapters.DayAdapter;
import zzheads.com.stormy.adapters.Settings;
import zzheads.com.stormy.weather.Day;

import static zzheads.com.stormy.weather.Current.getLocationByTimezone;

public class DailyForecastActivity extends Activity {

    private Day[] mDays;

    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.inject(this);
        final Settings curSet = new Settings(this);
        curSet.Load();

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);
        mLocationLabel.setText(getLocationByTimezone(mDays[0].getTimezone()));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp;
                if (!curSet.isCelsius()) {
                    highTemp = mDays[position].getTemperatureMax() + "F";
                } else {
                    highTemp = (mDays[position].getTemperatureMax()-32)*5/9 + "C";
                }
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}










