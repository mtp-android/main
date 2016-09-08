package mgr.mtp;

/**
 * Created by lukas on 21.02.2016.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import mgr.mtp.Diet.DietHome;
import mgr.mtp.Diet.DietSettings;
import mgr.mtp.Statistics.StatisticsHome;
import mgr.mtp.Training.TrainingHome;

/**
 *
 * Home Screen Activity
 */
public class Home extends AppCompatActivity implements LocationListener, SensorEventListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String date;
    int tab = 0;


    // location variables

    Criteria cr;
    LocationManager lm;
    String provider;
    Location loc;

    // sensors variables

    SensorManager senSensorManager;
    Sensor senAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            tab = extras.getInt("tab");
        }

        // sensors prepare

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(tab != 0){
            switchTab(tab);
        }
        // location prepare

        cr = new Criteria();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        refresh();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(provider, 1000, 1, this);

    Log.d("onCreate","Home");

    }

    // refresh location
    private void refresh() {
        provider = lm.getBestProvider(cr, true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            return;
        }
        loc = lm.getLastKnownLocation(provider);
    }


    @Override
    public void onLocationChanged(Location location) {
        refresh();
        String position = loc.getLongitude()+" "+loc.getLatitude();
        Log.d("onLocationChanged",position);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.d("Latitude", "status");
    }

    public void switchTab(int tab){
        TabLayout.Tab selectedTab = tabLayout.getTabAt(tab);
        selectedTab.select();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(date != null && !date.isEmpty())
        {
            Bundle bundle = new Bundle();
            bundle.putString("date", date);
            DietHome fragobj = new DietHome();
            fragobj.setArguments(bundle);
            adapter.addFragment(fragobj,"Dieta");
        }
        else
        {
            adapter.addFragment(new DietHome(), "Dieta");
        }

        if(date != null && !date.isEmpty())
        {
            Bundle bundle = new Bundle();
            bundle.putString("date", date);
            TrainingHome fragobj = new TrainingHome();
            fragobj.setArguments(bundle);
            adapter.addFragment(fragobj,"Trening");
        }
        else
        {
            adapter.addFragment(new TrainingHome(), "Trening");
        }
        adapter.addFragment(new StatisticsHome(), "Postępy");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Accelerometer","Read");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {

        super.onResume();
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if(allFragments != null)
        {
            DietHome fragment =  allFragments.get(1) instanceof DietHome ? (DietHome) allFragments.get(1) : (DietHome) allFragments.get(0) ;
            fragment.refreshBars();
            fragment.getMealsHeadersForDay(fragment.selectedDate);
            fragment.getSummaryForDay(fragment.selectedDate);
        }
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
            switch (item.getItemId()) {
                case R.id.diet_settings:
                    Intent i = new Intent(this, DietSettings.class);
                    startActivity(i);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

}