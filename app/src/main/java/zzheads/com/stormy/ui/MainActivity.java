package zzheads.com.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import zzheads.com.stormy.R;
import zzheads.com.stormy.adapters.Settings;
import zzheads.com.stormy.weather.Current;
import zzheads.com.stormy.weather.Day;
import zzheads.com.stormy.weather.Forecast;
import zzheads.com.stormy.weather.Hour;

import static zzheads.com.stormy.adapters.Settings.findCityByLoc;
import static zzheads.com.stormy.weather.Current.getLocationByTimezone;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    private Settings currentSettings;
    private Forecast mForecast;
    //private double latitude, longitude;
    public boolean isLocManually = false;
    public String currentCity;
    public MyLocationListener locListener = new MyLocationListener();
    public Location currentLoc = new Location("Test");


    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.angleTextView) TextView mAngleTextView;
    @InjectView(R.id.isLocManuallyTextView) TextView mIsLocManuallyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        if (locListener.SetUpLocationListener(this)) {
            currentLoc = MyLocationListener.getLastKnownPostion(this);
            currentCity = findCityByLoc(currentLoc);
            isLocManually = false;
            // установили посление координаты
        } else {
            currentLoc.setLatitude(48.7193900); // по умолчанию координаты Волгограда 48.7193900, 44.5018400
            currentLoc.setLongitude(44.5018400);
            currentCity = "Volgograd";
            isLocManually = true;
            // не известно последнее местоположение
        }

        currentSettings = new Settings(true, isLocManually, currentLoc, currentCity); // градусы в Цельсий, местоположение - текщие координаты
        currentSettings.Save(this);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.Load(MainActivity.this);
                if (!currentSettings.isLocManual()) {
                    currentLoc = MyLocationListener.getLastKnownPostion(MainActivity.this);
                    currentSettings.setCurrentLocation(currentLoc);
                    getForecast(currentLoc.getLatitude(), currentLoc.getLongitude());
                } else {
                    currentLoc = currentSettings.getCurrentLocation();
                    getForecast(currentSettings.findCoords(currentSettings.getCity()).getLatitude(), currentSettings.findCoords(currentSettings.getCity()).getLongitude());
                }
                currentSettings.Save(MainActivity.this);
            }
        });

        getForecast(currentSettings.getCurrentLocation().getLatitude(), currentSettings.getCurrentLocation().getLongitude());

        Log.d(TAG, "Main UI code is running!");
    }

    @Override
    protected void onResume () {
        super.onResume();
        currentSettings.Load(MainActivity.this);
        if (currentSettings.isLocManual()) {
            getForecast(currentSettings.findCoords(currentSettings.getCity()).getLatitude(), currentSettings.findCoords(currentSettings.getCity()).getLongitude());
        } else {
            currentLoc = MyLocationListener.getLastKnownPostion(this);
            currentSettings.setCurrentLocation(currentLoc);
            getForecast(currentLoc.getLatitude(), currentLoc.getLongitude());
        }
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "27974c4bc33201748eaf542a6769c3b7";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);

        if (currentSettings.isCelsius()) {
            mTemperatureLabel.setText((current.getTemperature()-32)*5/9 + ""); // в Цельсии
            mAngleTextView.setText("C");
        } else {
            mTemperatureLabel.setText(current.getTemperature() + ""); // Фаренгейт
            mAngleTextView.setText("F");
        }


        if (currentSettings.isLocManual()) {
            mIsLocManuallyTextView.setText("Location set Manually");
            mLocationLabel.setText(currentSettings.getCity());
        } else {
            mIsLocManuallyTextView.setText("Location set GPS");
            mLocationLabel.setText(getLocationByTimezone(current.getTimeZone()));
        }
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            days[i] = day;
        }

        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;
    }


    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);
    }

    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }

    @OnClick (R.id.settingsButton)
    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra("SETTINGS_TEMP", currentSettings.isCelsius());
        intent.putExtra("SETTINGS_LOCMANUAL", currentSettings.isLocManual());
        intent.putExtra("SETTINGS_LOC", currentSettings.getCurrentLocation().toString());
        startActivity(intent);
    }

}














