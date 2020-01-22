package inofa.avesbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import inofa.avesbox.Adapter.AdapterListSensor;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Rest.ApiInterface;
import inofa.avesbox.Storage.SharePrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorActivity extends AppCompatActivity {
    private ArrayList<DataSensor> sensorList;
    private RecyclerView recyclerView;
    private AdapterListSensor lAdapter;
    private SwipeRefreshLayout swipeRefresh;
    LinearLayout layoutNoData;
    Context mContext;
    RelativeLayout layoutListSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mContext = this;
        layoutListSensor = findViewById(R.id.layoutListSensor);
        recyclerView = findViewById(R.id.recycleSensor);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lAdapter != null) {
                    lAdapter.refreshEvents(sensorList);
                }
                listSensor();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        listSensor();
    }

    private void listSensor() {
//        layoutListSensor.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);


        LoginRespon loginRespon = SharePrefManager.getInstance(this).getUser();
        String token = loginRespon.getToken();
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);

        call.enqueue(new Callback<DataSensorRespon>() {

            @Override
            public void onResponse(Call<DataSensorRespon> call, Response<DataSensorRespon> response) {
                if (response.isSuccessful()) {
//                    recyclerView.setVisibility(View.VISIBLE);
                    //filter if()
                    sensorList = response.body().getDataSensors();
                    lAdapter = new AdapterListSensor(sensorList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(lAdapter);

                }

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataSensorRespon> call, Throwable t) {

                swipeRefresh.setRefreshing(false);
                Toast.makeText(SensorActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }


}
