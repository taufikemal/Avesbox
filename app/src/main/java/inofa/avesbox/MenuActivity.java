package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Storage.SharePrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView TVGreeting;
    Context mContext;
    Handler handler = new Handler();
    ProgressDialog loading;
    Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        //Salam Sapaan//
        TVGreeting = findViewById(R.id.TVgreeting);
        //Get the time of day
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        //Set greeting//
        String greeting = null;
        if (hour >= 10 && hour < 15) {
            greeting = "Selamat Siang";
        } else if (hour >= 15 && hour < 18) {
            greeting = "Selamat Sore";
        } else if (hour >= 18 && hour < 24) {
            greeting = "Selamat Malam";
        } else {
            greeting = "Selamat Pagi";
        }
        TVGreeting.setText(greeting);

        // Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Refresh
        refresh = new Runnable() {
            public void run() {
                suhu();
                handler.postDelayed(refresh, 60000);
            }
        };
        handler.post(refresh);

        //Inten Menu
        LinearLayout MenuAirPakan;
        MenuAirPakan = findViewById(R.id.ButtonPakanAir);
        MenuAirPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, AirPakanActivity.class);
                startActivity(i);
            }
        });
    }

    public void suhu() {

        LoginRespon loginRespon = SharePrefManager.getInstance(this).getUser();
        String token = loginRespon.getToken();
        // retrofit suhu
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);
        final TextView TvSuhu = findViewById(R.id.TVSuhu);
        final TextView TvPakan = findViewById(R.id.TVPakan);
        final TextView TvAir = findViewById(R.id.TVAir);

        call.enqueue(new Callback<DataSensorRespon>() {
            @Override
            public void onResponse(retrofit2.Call<DataSensorRespon> call, Response<DataSensorRespon> response) {
                DataSensorRespon dataSensorRespon = response.body();
                if (response.isSuccessful()) {
                    if (dataSensorRespon.getCode() == 200) {
                        ArrayList<DataSensor> arrayDataSensor = dataSensorRespon.getDataSensors();
                        if (arrayDataSensor.size() > 0) {
                            ArrayList<DataSensor> filterDataSuhu = new ArrayList<>();
                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 4) {
                                    filterDataSuhu.add(dataSensor);

                                    float suhu = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvSuhu.setText(String.valueOf(decimalFormat.format(suhu)));
                                }
                                else if (dataSensor.getKodeSensor() == 5){
                                    filterDataSuhu.add(dataSensor);
                                    float pakan = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvPakan.setText(String.valueOf(decimalFormat.format(pakan)));
                                }
                                else if (dataSensor.getKodeSensor() == 6){
                                    filterDataSuhu.add(dataSensor);
                                    float air = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvAir.setText(String.valueOf(decimalFormat.format(air)));
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<DataSensorRespon> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.btlogout) {
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            SharePrefManager.getInstance(MenuActivity.this).clear();
            Toast.makeText(mContext,"Logout successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(refresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        suhu();
    }
}
