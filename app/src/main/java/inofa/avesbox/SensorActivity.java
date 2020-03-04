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
    private RecyclerView recyclerView;
    private AdapterListSensor lAdapter;
    private SwipeRefreshLayout swipeRefresh;
    LinearLayout layoutNoData;
    Context mContext;
    RelativeLayout layoutListSensor;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mContext = this;
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setEnabled(true);
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        String token = shfm.getString("token", "");
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);

        call.enqueue(new Callback<DataSensorRespon>() {

            @Override
            public void onResponse(Call<DataSensorRespon> call, Response<DataSensorRespon> response) {
                DataSensorRespon dataSensorRespon = response.body();
                if (response.isSuccessful()) {
                    if (dataSensorRespon.getCode() == 200) {
                        ArrayList<DataSensor> arrayListSensor = dataSensorRespon.getDataSensors();
                        if (arrayListSensor.size() > 0) {
                            ArrayList<DataSensor> dataAakhir1 = new ArrayList<>();
                            ArrayList<DataSensor> dataAakhir2 = new ArrayList<>();
                            ArrayList<DataSensor> dataAakhir3 = new ArrayList<>();
                            ArrayList<DataSensor> dataAakhir4 = new ArrayList<>();
                            ArrayList<DataSensor> dataAakhir5 = new ArrayList<>();
                            for (int i = 0; i < arrayListSensor.size(); i++) {
                                DataSensor listSensor = arrayListSensor.get(i);
                                if (listSensor.getKodeSensor() == 3) {
                                    dataAakhir1.add(listSensor);
                                    int id = dataAakhir1.get(dataAakhir1.size() - 1).getKodeSensor();
                                    String nama = dataAakhir1.get(dataAakhir1.size() - 1).getNamaSensor();
                                    String update = dataAakhir1.get(dataAakhir1.size() - 1).getTanggal();
                                    TextView idTV1 = findViewById(R.id.idSensor1);
                                    TextView namaTV1 = findViewById(R.id.jenisSensor1);
                                    TextView updateTV1 = findViewById(R.id.updateSensor1);
                                    idTV1.setText(String.valueOf(id));
                                    namaTV1.setText(String.valueOf(nama));
                                    updateTV1.setText(String.valueOf(update));
                                } else if (listSensor.getKodeSensor() == 4) {
                                    dataAakhir2.add(listSensor);
                                    int id = dataAakhir2.get(dataAakhir2.size() - 1).getKodeSensor();
                                    String nama = dataAakhir2.get(dataAakhir2.size() - 1).getNamaSensor();
                                    String update = dataAakhir2.get(dataAakhir2.size() - 1).getTanggal();
                                    TextView idTV2 = findViewById(R.id.idSensor2);
                                    TextView namaTV2 = findViewById(R.id.jenisSensor2);
                                    TextView updateTV2 = findViewById(R.id.updateSensor2);
                                    idTV2.setText(String.valueOf(id));
                                    namaTV2.setText(String.valueOf(nama));
                                    updateTV2.setText(String.valueOf(update));
                                } else if (listSensor.getKodeSensor() == 5) {
                                    dataAakhir3.add(listSensor);
                                    int id = dataAakhir3.get(dataAakhir3.size() - 1).getKodeSensor();
                                    String nama = dataAakhir3.get(dataAakhir3.size() - 1).getNamaSensor();
                                    String update = dataAakhir3.get(dataAakhir3.size() - 1).getTanggal();
                                    TextView idTV3 = findViewById(R.id.idSensor3);
                                    TextView namaTV3 = findViewById(R.id.jenisSensor3);
                                    TextView updateTV3 = findViewById(R.id.updateSensor3);
                                    idTV3.setText(String.valueOf(id));
                                    namaTV3.setText(String.valueOf(nama));
                                    updateTV3.setText(String.valueOf(update));
                                } else if (listSensor.getKodeSensor() == 6) {
                                    dataAakhir4.add(listSensor);
                                    int id = dataAakhir4.get(dataAakhir4.size() - 1).getKodeSensor();
                                    String nama = dataAakhir4.get(dataAakhir4.size() - 1).getNamaSensor();
                                    String update = dataAakhir4.get(dataAakhir4.size() - 1).getTanggal();
                                    TextView idTV4 = findViewById(R.id.idSensor4);
                                    TextView namaTV4 = findViewById(R.id.jenisSensor4);
                                    TextView updateTV4 = findViewById(R.id.updateSensor4);
                                    idTV4.setText(String.valueOf(id));
                                    namaTV4.setText(String.valueOf(nama));
                                    updateTV4.setText(String.valueOf(update));
                                }
                            }
                        }
                    }
                }
                loading.dismiss();
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
