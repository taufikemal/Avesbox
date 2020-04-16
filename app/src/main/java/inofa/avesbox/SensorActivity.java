package inofa.avesbox;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inofa.avesbox.Adapter.AdapterListKandang;
import inofa.avesbox.Adapter.AdapterListSensor;
import inofa.avesbox.Model.DataKandang;
import inofa.avesbox.Model.DataListSensor;
import inofa.avesbox.Model.DataListSensorRespon;
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
    private RecyclerView recyclerView;
    private AdapterListSensor lAdapter;
//    private SwipeRefreshLayout swipeRefresh;
    LinearLayout layoutNoData;
    Context mContext;
    RelativeLayout layoutListSensor;
    ProgressDialog loading;
    ArrayList<DataListSensor> sensorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        layoutListSensor = findViewById(R.id.layoutListSensor);
        recyclerView = findViewById(R.id.recycleListSensor);
        mContext = this;
//        swipeRefresh = findViewById(R.id.swipeRefresh);
//        swipeRefresh.setEnabled(true);
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                listSensor();
//            }
//        });
        listSensor();

    }
    private void listSensor() {
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        String token = shfm.getString("token", "");
        Call<DataListSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataListSensor(token);

        call.enqueue(new Callback<DataListSensorRespon>() {
            @Override
            public void onResponse(Call<DataListSensorRespon> call, Response<DataListSensorRespon> response) {
                DataListSensorRespon dataSensorRespon = response.body();
                if (response.isSuccessful()) {
                    if (dataSensorRespon.getCode() == 200) {
                        ArrayList<DataListSensor> arrayListSensor = dataSensorRespon.getDataListSensors();
                        if (arrayListSensor.size() > 0) {
                            for (int i = 0; i < arrayListSensor.size(); i++) {
                                DataListSensor listSensor = arrayListSensor.get(i);
                                sensorList.add(listSensor);
                            }
                        }
                    }else  {
                        loading.dismiss();
                        Toast.makeText(SensorActivity.this, "Tidak ada data. Cek koneksi anda.", Toast.LENGTH_SHORT).show();
                    }
                    lAdapter = new AdapterListSensor(sensorList);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(lAdapter);
                }
                loading.dismiss();

            }

            @Override
            public void onFailure(Call<DataListSensorRespon> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                Toast.makeText(SensorActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
                loading.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        listSensor();
    }

}
