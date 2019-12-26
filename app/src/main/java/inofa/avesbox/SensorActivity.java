package inofa.avesbox;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Storage.SharePrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorActivity extends AppCompatActivity {
    Context mContext;
    Runnable refresh;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

//        //pull refresh
//        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                sensor();
//                pullToRefresh.setRefreshing(false);
//            }
//        });

        //Refresh
        refresh = new Runnable() {
            public void run() {
                sensor();
                handler.postDelayed(refresh, 60000);
            }
        };
        handler.post(refresh);


    }
    public void sensor(){
        LoginRespon loginRespon = SharePrefManager.getInstance(this).getUser();
        String token = loginRespon.getToken();
        // retrofit suhu
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);

        final TextView iDSensor1 = findViewById(R.id.idTVSensor1);
        final TextView jenisSensor1 = findViewById(R.id.jenisTVSensor1);
        final TextView updateSensor1 = findViewById(R.id.updateTVsensor1);

        final TextView iDSensor2 = findViewById(R.id.idTVSensor2);
        final TextView jenisSensor2 = findViewById(R.id.jenisTVSensor2);
        final TextView updateSensor2 = findViewById(R.id.updateTVsensor2);

        final TextView iDSensor3 = findViewById(R.id.idTVSensor3);
        final TextView jenisSensor3 = findViewById(R.id.jenisTVSensor3);
        final TextView updateSensor3 = findViewById(R.id.updateTVsensor3);

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
                                if (dataSensor.getKodeSensor() == 1) {
                                    filterDataSuhu.add(dataSensor);
                                    int id = filterDataSuhu.get(filterDataSuhu.size() - 1).getKodeSensor();
                                    String jenis = filterDataSuhu.get(filterDataSuhu.size() - 1).getNamaSensor();
                                    String update = filterDataSuhu.get(filterDataSuhu.size() - 1).getTanggal();
                                    iDSensor1.setText(String.valueOf((id)));
                                    jenisSensor1.setText(String.valueOf(jenis));
                                    updateSensor1.setText(String.valueOf(update));
                                } else if (dataSensor.getKodeSensor() == 6) {
                                    filterDataSuhu.add(dataSensor);
                                    int id = filterDataSuhu.get(filterDataSuhu.size() - 1).getKodeSensor();
                                    String jenis = filterDataSuhu.get(filterDataSuhu.size() - 1).getNamaSensor();
                                    String update = filterDataSuhu.get(filterDataSuhu.size() - 1).getTanggal();
                                    iDSensor2.setText(String.valueOf((id)));
                                    jenisSensor2.setText(String.valueOf(jenis));
                                    updateSensor2.setText(String.valueOf(update));
                                } else if (dataSensor.getKodeSensor() == 5) {
                                    filterDataSuhu.add(dataSensor);
                                    int id = filterDataSuhu.get(filterDataSuhu.size() - 1).getKodeSensor();
                                    String jenis = filterDataSuhu.get(filterDataSuhu.size() - 1).getNamaSensor();
                                    String update = filterDataSuhu.get(filterDataSuhu.size() - 1).getTanggal();
                                    iDSensor3.setText(String.valueOf((id)));
                                    jenisSensor3.setText(String.valueOf(jenis));
                                    updateSensor3.setText(String.valueOf(update));
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refresh);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensor();
    }

}
