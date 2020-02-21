package inofa.avesbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Storage.SharePrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirPakanActivity extends AppCompatActivity  {

    private LineChart mChart;
    Context mContex;
    ArrayList<Entry> x;
    ArrayList<String> y;
    public String TAG = "YOUR CLASS NAME";
    List<Float> list = new ArrayList<Float>();
    List<Entry> values = new ArrayList<Entry>();
//    JSONArray jsonArray = new JSONArray(yValues);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_pakan);
        drawLineChart();

    }

    private void setData() {
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        String token = shfm.getString("token", "");
        // retrofit suhu
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);
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
                                    for (int a =0; a < filterDataSuhu.size(); a++){
                                        float nilai = filterDataSuhu.get(a).getNilai();
                                        Log.d("TAG", String.valueOf(nilai));
                                        values.add(new Entry(nilai , nilai));
                                    }
                                }
                            }
                        }

                    }
                }
            }


            @Override
            public void onFailure(retrofit2.Call<DataSensorRespon> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContex, "Something wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawLineChart() {
        LineChart lineChart = findViewById(R.id.chartSuhu);
//        List<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(values, "Air");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);

        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setText("Air");
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        if (values.isEmpty()) {
            lineChart.clear();
        } else {
            // set data
            lineChart.setData(lineData);
        }
//        lineChart.setData(lineData);
    }


}
