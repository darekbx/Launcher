package com.mlauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.getpebble.android.kit.PebbleKit;
import com.mlauncher.dotpad.DotManager;
import com.mlauncher.gl.GameRenderer;
import com.mlauncher.logic.FilterController;
import com.mlauncher.logic.SmokeLiveColor;
import com.mlauncher.logic.ussd.Caller;
import com.mlauncher.logic.ussd.CallerLimiter;
import com.mlauncher.logic.ussd.model.AccountBalance;
import com.mlauncher.logic.ussd.model.InternetBalance;
import com.mlauncher.logic.zm_air.SmokeApi;
import com.mlauncher.logic.SunriseSunset;
import com.mlauncher.logic.TimeManager;
import com.mlauncher.logic.sojp.Entry;
import com.mlauncher.logic.sojp.SOPJController;
import com.mlauncher.logic.utils.ShellUtils;
import com.mlauncher.logic.utils.NetworkUtils;
import com.mlauncher.logic.adapters.AppsAdapter;
import com.mlauncher.logic.db.AppsStorage;
import com.mlauncher.model.DayItem;
import com.mlauncher.model.SmokeItem;
import com.mlauncher.model.SortableResolveInfo;
import com.mlauncher.view.DayTimeView;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity
        extends Activity
        implements
            AdapterView.OnItemClickListener,
            AdapterView.OnItemLongClickListener,
            DrawerLayout.DrawerListener,
            SensorEventListener,
            Caller.Listener,
            GameRenderer.Listener {

    private class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (timeManager != null) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    if (dayTimeView != null) {
                        dayTimeView.setScreenOn(timeManager.getTodayTime());
                    }
                    timeManager.notifyScreenOn();
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    timeManager.notifyScreenOff();
                }
            }
        }
    }

    private static final boolean USE_SOPJ_AIR_QUALITY_API = false;

    private PackageBroadcast packageBroadcast;
    private ScreenReceiver screenBraodcast;

    private DrawerLayout drawerLayout = null;
    private GridView appsList = null;
    private TableLayout filterTable;
    private DayTimeView dayTimeView;

    private AppsStorage appsStorage = null;
    private AppsAdapter appsAdapter;
    private TimeManager timeManager;
    private FilterController filterController;

    private GLSurfaceView surfaceView;
    private GameRenderer gameRenderer;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    //private CallerLimiter callerLimiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        appsStorage = new AppsStorage(this);

        appsList = (GridView) findViewById(R.id.list_apps);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        filterTable = (TableLayout) findViewById(R.id.filter_table);
        dayTimeView = (DayTimeView) findViewById(R.id.day_time_view);

        bindListeners();
        fillApps();
        loadDayTime();
        loadSmoke();
        registerBroadcasts();
        initializeGLWallpaper();

        filterController = new FilterController(this, filterTable, filterButtonListener);

        new ShellUtils().isDeviceRooted();

        //Caller.getInstance(this);
        //Caller.getInstance(this).setListener(this);

        //callerLimiter = new CallerLimiter(this);
    }

    private int i = 0;

    private void initializeGLWallpaper() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        gameRenderer = new GameRenderer(this, this);

        surfaceView = (GLSurfaceView)findViewById(R.id.container);
        surfaceView.setRenderer(gameRenderer);
    }

    private void loadDotsInfo() {
        String message = String.valueOf(DotManager.countDots());
        ((TextView)findViewById(R.id.dot_count)).setText(message);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        gameRenderer.addXTilt(radToDeg(values[0]) / 250f);
    }

    public float radToDeg(float rad) {
        return rad * 180f / (float) Math.PI;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void registerBroadcasts() {

        timeManager = new TimeManager(this);

        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageFilter.addDataScheme("package");
        packageBroadcast = new PackageBroadcast();
        registerReceiver(packageBroadcast, packageFilter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        screenBraodcast = new ScreenReceiver();
        registerReceiver(screenBraodcast, filter);
    }

    private void loadDayTime() {
        (new AsyncTask<Void, Void, DayItem>() {
            @Override
            protected DayItem doInBackground(Void... voids) {
                return SunriseSunset.getCurrentDayInfo(Calendar.getInstance());
            }

            @Override
            protected void onPostExecute(DayItem dayItem) {
                dayTimeView.setCurrentDay(dayItem);
            }
        }).execute();
    }

    private void loadSmoke() {
        if (USE_SOPJ_AIR_QUALITY_API) {
            SOPJController.loadAirQuality(new SOPJController.Listener() {
                @Override
                public void onData(List<Entry> entries) {
                    SmokeItem pm10 = entries.get(0).toSmokeItem();
                    SmokeItem pm2_5 = entries.get(1).toSmokeItem();
                    dayTimeView.setSmokeItems(pm10, pm2_5);
                }
            }, "PM10", "PM2.5");
        } else {
            new SmokeApi(this, new SmokeApi.Listener() {
                @Override
                public void onItems(SmokeItem pm10, SmokeItem pm2_5) {
                    dayTimeView.setSmokeItems(pm10, pm2_5);
                    //gameRenderer.applySmoke(SmokeLiveColor.takeColorMask(pm2_5.state, pm10.state));
                }
            }).execute();
        }
    }

    private void fillApps() {
        filterTable.setVisibility(View.GONE);
        Thread thread = new Thread() {
            @Override
            public void run() {

                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                final List appsList = getPackageManager().queryIntentActivities(mainIntent, 0);
                appsStorage.addItems(appsList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (appsAdapter == null) {
                            appsAdapter = new AppsAdapter(
                                    MainActivity.this, appsStorage.getItems());
                            MainActivity.this.appsList.setAdapter(appsAdapter);
                        } else {
                            appsAdapter.refresh(appsStorage.getItems());
                            appsAdapter.notifyDataSetChanged();
                        }
                        filterController.setFilterTable(appsList);
                    }
                });
            }
        };
        thread.start();
    }

    private void bindListeners() {
        appsList.setOnItemClickListener(this);
        appsList.setOnItemLongClickListener(this);
        drawerLayout.setDrawerListener(this);

        findViewById(R.id.button_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getPackageManager().getLaunchIntentForPackage(
                        getString(R.string.dialer_package_name)));
            }
        });

        findViewById(R.id.button_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("smsto:"+ getString(R.string.moni_number)));
                startActivity(intent);
            }
        });

        findViewById(R.id.button_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp("com.dotpad");
            }
        });

        findViewById(R.id.button_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTable.setVisibility(View.VISIBLE);
            }
        });

        final ToggleButton wifiButton = (ToggleButton) findViewById(R.id.toggle_wifi);
        wifiButton.setChecked(NetworkUtils.isWiFiEnabled(this));
        wifiButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                NetworkUtils.setWiFiState(getApplicationContext(), b);
            }
        });
    }

    public void openApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        }
    }

    private boolean isMobileDataOn() {
        return Settings.Global.getInt(getContentResolver(), "mobile_data", 0) == 1;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawers();
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (surfaceView != null) {
            surfaceView.onPause();
        }

        if (gyroscopeSensor != null) {
            sensorManager.unregisterListener(this, gyroscopeSensor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadSmoke();
        loadDotsInfo();
        loadDayTime();
        dayTimeView.setIsMobileDataOn(isMobileDataOn());

       /* if (callerLimiter.canCall()) {
            Caller.getInstance(null).retrieveInfo();
        }*/

        if (findViewById(R.id.pebble_disconnected) != null) {
            boolean isConnected = PebbleKit.isWatchConnected(this);
            findViewById(R.id.pebble_disconnected).setVisibility(
                    isConnected ? View.GONE : View.VISIBLE);
        }

        if (surfaceView != null && gameRenderer != null) {
            surfaceView.onResume();
        }

        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onDestroy() {

        if (appsStorage != null)
            appsStorage.close();

        if (packageBroadcast != null)
            unregisterReceiver(packageBroadcast);

        if (screenBraodcast != null)
            unregisterReceiver(screenBraodcast);

        if (gameRenderer != null)
            gameRenderer.destroy();

        //Caller.getInstance(this).cleanUp();

        filterController.destroyButtons();
        filterButtonListener = null;

        super.onDestroy();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppsAdapter adapter = (AppsAdapter) adapterView.getAdapter();
        final SortableResolveInfo item = (SortableResolveInfo) adapter.getItem(i);

        if (item != null) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse(getString(R.string.package_format, item.packageName)));
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AppsAdapter adapter = (AppsAdapter) adapterView.getAdapter();
        SortableResolveInfo item = (SortableResolveInfo) adapter.getItem(i);

        if (item != null) {

            appsStorage.increase(item.packageName);
            drawerLayout.closeDrawers();

            ComponentName name = new ComponentName(item.packageName, item.name);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent.setComponent(name);

            try {
                startActivity(intent);
                appsList.setSelection(0);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        fillApps();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    @Override
    public void onAccountBalance(AccountBalance accountBalance) {
        if (dayTimeView != null) {
            dayTimeView.setAccountBalance(accountBalance);
        }
    }

    @Override
    public void onInternetBalance(InternetBalance internetBalance) {
        if (dayTimeView != null) {
            dayTimeView.setInternetBalance(internetBalance);
        }
    }

    @Override
    public void onFramesPerSecond(int framesPerSecond) {
        if (dayTimeView != null) {
            dayTimeView.setWallpaperFPS(framesPerSecond);
        }
    }

    private class PackageBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            fillApps();
        }
    }

    private View.OnClickListener filterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (appsAdapter != null) {
                String filter = ((Button) view).getText().toString().toLowerCase();
                appsAdapter.getFilter().filter(filter);
            }
            filterTable.setVisibility(View.GONE);
        }
    };
}