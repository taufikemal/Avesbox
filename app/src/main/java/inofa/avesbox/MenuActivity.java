package inofa.avesbox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import inofa.avesbox.Adapter.ListNewsAdapter;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Model.LoginResponUser;
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
    SwipeRefreshLayout swipeRefreshLayout;
    Runnable refresh;
    float lembap,air,pakan,suhu;

    // newslist
    String API_KEY = "9c8df7817a1a41de8d10732a3d57e887";
    String NEWS_SOURCE = "id";
    ListView listNews;
    ProgressBar loader;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    public static String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static String KEY_DESCRIPTION = "description";
    public static final String KEY_URL = "url";
    public static final String KEY_URLTOIMAGE = "urlToImage";
    public static final String KEY_PUBLISHEDAT = "publishedAt";

    NavigationView navigationView;
    TextView tvNama;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        // news
        listNews = (ListView) findViewById(R.id.listNews);
        loader = (ProgressBar) findViewById(R.id.loader1);
        listNews.setEmptyView(loader);


        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

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
        drawer.refreshDrawableState();
        toggle.syncState();

        //header sidebar
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.inflateHeaderView(R.layout.navigation_header_menu);
        ImageView fotprof = (ImageView) hView.findViewById(R.id.myPict);
        tvNama = (TextView) hView.findViewById(R.id.tvNama);
        fotprof.setImageResource(R.mipmap.iconprofil);

        //Refresh
        refresh = new Runnable() {
            public void run() {
                suhu();
                handler.postDelayed(refresh, 60000);
            }
        };
        handler.post(refresh);


//        pull refresh
        swipeRefreshLayout = findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                suhu();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        // listview dan swipeResfresh
        listNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listNews.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(listNews.getFirstVisiblePosition() == 0 && listNews.getChildAt(0).getTop() == 0);
                }
            }
        });


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
        //inten halaman sensor
        LinearLayout MenuSensorKandang = findViewById(R.id.buttonSensorKandang);
        MenuSensorKandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });
       LinearLayout MenuSuhuKelembapan = findViewById(R.id.buttonSuhuKelembapan);
       MenuSuhuKelembapan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MenuActivity.this, SuhuLembapActivity.class);
               startActivity(intent);
           }
       });

        LinearLayout MenuSmartAmbience = findViewById(R.id.smartAmbience);
        MenuSmartAmbience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Dalam Pengembangan", Toast.LENGTH_SHORT).show();
            }
        });

        TextView FullBerita = findViewById(R.id.textViewBacaSemua);
        FullBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListNewsActivity.class);
                startActivity(i);
            }
        });
    }


    public void suhu() {

        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        String token = shfm.getString("token", "");
        // retrofit suhu
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);
        final TextView TvSuhu = findViewById(R.id.TVSuhu);
        final TextView TvPakan = findViewById(R.id.TVPakan);
        final TextView TvAir = findViewById(R.id.TVAir);
        final TextView TVLembap = findViewById(R.id.tvLembap);

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
                                    suhu = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvSuhu.setText(String.valueOf(decimalFormat.format(suhu)));
                                } else if (dataSensor.getKodeSensor() == 5) {
                                    filterDataSuhu.add(dataSensor);
                                    pakan = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvPakan.setText(String.valueOf(decimalFormat.format(pakan)));
                                } else if (dataSensor.getKodeSensor() == 1) {
                                    filterDataSuhu.add(dataSensor);
                                    air = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TvAir.setText(String.valueOf(decimalFormat.format(air)));
                                } else if (dataSensor.getKodeSensor() == 6) {
                                    filterDataSuhu.add(dataSensor);
                                    lembap = filterDataSuhu.get(filterDataSuhu.size() - 1).getNilai();
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    TVLembap.setText(String.valueOf(decimalFormat.format(lembap)));
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
        loading.dismiss();


        // notifikasi
        int SUMMARY_ID = 0;
        String GROUP_KEY_WORK_EMAIL = "com.android.example.";
        if (0 < suhu && (29 >= suhu || suhu >= 32 )) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MenuActivity.this
            )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notifikasi Suhu")
                    .setContentText("Perhatian, suhu kandang tidak ideal")
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{1000,1000})
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true);
            Intent intent = new Intent(MenuActivity.this, SuhuLembapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
            notificationManager.notify(1,builder.build());
        }
        if (0 < lembap && (lembap >= 28 || lembap <= 25)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MenuActivity.this
            )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notifikasi Kelembapan")
                    .setContentText("Perhatian, kelembapan kandang tidak ideal")
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{1000,1000})
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true);
            Intent intent = new Intent(MenuActivity.this, SuhuLembapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
            notificationManager.notify(2,builder.build());
        }
        if (0 < pakan && pakan >= 80){

            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MenuActivity.this
            )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notifikasi Pakan")
                    .setContentText("Perhatian, pakan ternak hampir habis")
//                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{1000,1000})
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true);
            Intent intent = new Intent(MenuActivity.this, AirPakanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
            notificationManager.notify(3,builder.build());

        }
        if (0 < air && air >= 110 ){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    MenuActivity.this
            )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notifikasi Air")
                    .setContentText("Perhatian, minum ternak hampir habis")
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setVibrate(new long[]{1000,1000})
                    .setGroup(GROUP_KEY_WORK_EMAIL)
                    .setAutoCancel(true);
            Intent intent = new Intent(MenuActivity.this, AirPakanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
            notificationManager.notify(4,builder.build());
        }

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_profil) {
            // Handle the camera action
            Intent i = new Intent(MenuActivity.this, ProfilActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            Intent intent = new Intent(MenuActivity.this, KandangActivity.class);
            startActivity(intent);
        } else if (id == R.id.btlogout) {
//            loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
            SharePrefManager.getInstance(MenuActivity.this).clear();
            Toast.makeText(mContext, "Logout successfully", Toast.LENGTH_LONG).show();
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
        sidebar();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        suhu();
    }

    public void sidebar() {
        SharedPreferences shfm = getSharedPreferences("spUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String data = shfm.getString("data", "");
        LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        String nama = user.getNama();
        tvNama.setText(nama);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String xml = "";
            String urlParameters = "";
            xml = Function.excuteGet("http://newsapi.org/v2/top-headlines?country=" + NEWS_SOURCE + "&apiKey=" + API_KEY, urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            if (xml.length() > 10) { // проверяет, если нет пусто
                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                ListNewsAdapter adapter = new ListNewsAdapter(MenuActivity.this, dataList);
                listNews.setAdapter(adapter);

                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(MenuActivity.this, DetailNewsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        startActivity(i);
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
