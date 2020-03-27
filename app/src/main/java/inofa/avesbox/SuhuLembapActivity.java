package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
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

public class SuhuLembapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LineChartView lineChartView;
    LineChartView lineChartView2;
    Context mContex;
    Spinner dropdown;
    private static final String[] paths = {"5 Data", "10 Data", "15 Data", "20 Data"};
    int start;
    ProgressDialog loading;
    SwipeRefreshLayout swipeRefreshLayout;
    List<PointValue> yAxisValues = new ArrayList();
    List<AxisValue> axisValues = new ArrayList();
    List<PointValue> sumbuY = new ArrayList<>();
    List<AxisValue> sumbuX = new ArrayList<>();

    List<PointValue> temperature_data_entry2 = new ArrayList<>();
    List<AxisValue> temperature_dates_entry2 = new ArrayList<>();
    List<AxisValue> axisValues2 = new ArrayList<>();
    List<PointValue> yAxisValues2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu_lembap);
        mContex = this;
        swipeRefreshLayout = findViewById(R.id.swipeRefreshSuhuLembap);
        swipeRefreshLayout.setEnabled(true);
        loading = ProgressDialog.show(mContex, null, "Harap Tunggu...", true, false);
        dropdown = findViewById(R.id.spinnerPeriodeSuhuLembap);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SuhuLembapActivity.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
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
                        List<DataSensor> arrayDataSensor = dataSensorRespon.getDataSensors();
                        if (arrayDataSensor.size() > 0) {
                            // lchart
                            lineChartView = findViewById(R.id.chart);
                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 4) {
                                    sumbuX.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
                                    float data = (float) dataSensor.getNilai();
                                    sumbuY.add(new PointValue(i, data));
                                } else if (dataSensor.getKodeSensor() == 5) {
                                    float nilai = (float) dataSensor.getNilai();
                                    temperature_data_entry2.add(new PointValue(i, nilai));
                                    temperature_dates_entry2.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
                                }
                            }
                            axisValues = sumbuX.subList(sumbuX.size() - start, sumbuX.size());
                            yAxisValues = sumbuY.subList(sumbuY.size() - start, sumbuY.size());
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
                            axis.getMaxLabelChars();
                            axis.setName("Suhu");
                            axis.setTextColor(Color.parseColor("#03A9F4"));
                            data.setAxisXBottom(axis);

                            Axis yAxis = new Axis();
                            yAxis.setTextColor(Color.parseColor("#03A9F4"));
                            yAxis.setTextSize(16);
                            data.setAxisYLeft(yAxis);

                            lineChartView.setLineChartData(data);
                            Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                            viewport.top = 40;
                            lineChartView.setMaximumViewport(viewport);
                            lineChartView.setCurrentViewport(viewport);
                        }
                        chart2();
                        loading.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
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
    public void chart2() {
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
        lineChartView2 = findViewById(R.id.chartLembap);
        lineChartView2.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView2.getMaximumViewport());
        viewport.top = 100;
        lineChartView2.setMaximumViewport(viewport);
        lineChartView2.setCurrentViewport(viewport);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                start = 5;
                break;
            case 1:
                start = 10;
                break;
            case 2:
                start = 15;
                break;
            case 3:
                start = 20;
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

}
