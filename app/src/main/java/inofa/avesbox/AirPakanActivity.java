package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Rest.ApiClient;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirPakanActivity extends AppCompatActivity {
// implements AdapterView.OnItemSelectedListener
    LineChartView lineChartView;
    LineChartView lineChartView2;
    Context mContex;
    List<PointValue> temperature_data_entry = new ArrayList<>();
    List<AxisValue> temperature_dates_entry = new ArrayList<>();
    ArrayList<DataSensor> arrayDataSensor = new ArrayList<>();
    List<AxisValue> axisValues = new ArrayList<>();
    List<PointValue> yAxisValues = new ArrayList<>();

    List<PointValue> temperature_data_entry2 = new ArrayList<>();
    List<AxisValue> temperature_dates_entry2 = new ArrayList<>();
    List<AxisValue> axisValues2 = new ArrayList<>();
    List<PointValue> yAxisValues2 = new ArrayList<>();
//    Spinner dropdown;
    private static final String[] paths = {"5 Data", "10 Data", "15 Data", "20 Data"};
    int start;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_pakan);
        mContex = this;
        loading = ProgressDialog.show(mContex, null, "Harap Tunggu...", true, false);
//        dropdown = findViewById(R.id.spinnerPeriode);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AirPakanActivity.this,
//                android.R.layout.simple_spinner_item, paths);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(this);
        lineChart();
    }
    public void lineChart() {
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        String token = shfm.getString("token", "");
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
                       arrayDataSensor = dataSensorRespon.getDataSensors();
                        if (arrayDataSensor.size() > 0) {
                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 1) {
                                    float nilai = (float) dataSensor.getNilai();
                                    temperature_data_entry.add(new PointValue(i, nilai));
                                    temperature_dates_entry.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));

                                } else if (dataSensor.getKodeSensor() == 5) {
                                    float nilai = (float) dataSensor.getNilai();
                                    temperature_data_entry2.add(new PointValue(i, nilai));
                                    temperature_dates_entry2.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
                                }
                            }
                            start = 6;
                            axisValues = temperature_dates_entry.subList(temperature_dates_entry.size() - start, temperature_dates_entry.size());
                            yAxisValues = temperature_data_entry.subList(temperature_data_entry.size() - start, temperature_data_entry.size());
                            axisValues2 = temperature_dates_entry2.subList(temperature_dates_entry2.size() - start, temperature_dates_entry2.size());
                            yAxisValues2 = temperature_data_entry2.subList(temperature_data_entry2.size() - start, temperature_data_entry2.size());
                            Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
                            List<Line> lines = new ArrayList();
                            lines.add(line);
                            LineChartData data = new LineChartData();
                            data.setLines(lines);
                            Axis axis = new Axis();
                            axis.setValues(axisValues);
                            axis.setTextSize(6);
//                        axis.getMaxLabelChars();
//                            axis.setName("Suhu");
                            axis.setTextColor(Color.parseColor("#03A9F4"));
                            data.setAxisXBottom(axis);
                            Axis yAxis = new Axis();
                            yAxis.setTextColor(Color.parseColor("#03A9F4"));
                            yAxis.setTextSize(16);
                            data.setAxisYLeft(yAxis);
                            lineChartView = findViewById(R.id.chartAirPakan);
                            lineChartView.setLineChartData(data);
                            Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                            viewport.top = 150;
                            viewport.bottom = 0;
                            lineChartView.setMaximumViewport(viewport);
                            lineChartView.setCurrentViewport(viewport);
                        }
                    } else  {
                        loading.dismiss();
                        Toast.makeText(mContex, "Tidak ada data. Cek koneksi anda.", Toast.LENGTH_SHORT).show();
                    }
                }
                lineChart2();
                loading.dismiss();
            }
            @Override
            public void onFailure(retrofit2.Call<DataSensorRespon> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContex, "Tidak ada data. Cek koneksi anda.", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }


    public void lineChart2() {
        Line line = new Line(yAxisValues2).setColor(Color.parseColor("#9C27B0"));
        List<Line> lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        Axis axis = new Axis();
        axis.setValues(axisValues2);
        axis.setTextSize(6);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);
        Axis yAxis = new Axis();
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);
        lineChartView2 = findViewById(R.id.chartAirPakan2);
        lineChartView2.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView2.getMaximumViewport());
        viewport.top = 150;
        viewport.bottom = 0;
        lineChartView2.setMaximumViewport(viewport);
        lineChartView2.setCurrentViewport(viewport);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//        switch (position) {
//            case 0:
//                // Whatever you want to happen when the first item gets selected
//                start = 5;
//                break;
//            case 1:
//                // Whatever you want to happen when the second item gets selected
//                start = 10;
//                break;
//            case 2:
//                // Whatever you want to happen when the thrid item gets selected
//                start = 15;
//                break;
//            case 3:
//                // Whatever you want to happen when the thrid item gets selected
//                start = 20;
//                break;
//        }
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        // TODO Auto-generated method stub
//    }
    @Override
    protected void onResume() {
        super.onResume();
        lineChart();
    }
}
