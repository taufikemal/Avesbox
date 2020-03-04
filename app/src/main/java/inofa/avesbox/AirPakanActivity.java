package inofa.avesbox;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirPakanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LineChart mChart;
    Context mContex;
    List<String> temperature_data_entry = new ArrayList<>();
    List<String> temperature_dates_entry = new ArrayList<>();

    List<DataSensor> list = new ArrayList<DataSensor>();
    List<Entry> values = new ArrayList<Entry>();
    Spinner dropdown;
    private static final String[] paths = {"Hari", "Minggu", "Bulan", "Semua Data"};
    float nilai;
    Float tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_pakan);

        dropdown = findViewById(R.id.spinnerPeriode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AirPakanActivity.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        lineChart();
    }

    public void lineChart() {
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
                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 5) {
                                    temperature_data_entry.add(String.valueOf(dataSensor.getNilai()));
                                    temperature_dates_entry.add(dataSensor.getTanggal());
                                }
                            }
                        }
                    }
                    try {
                        drawLineChart(getDataDaySet());
                    } catch (ParseException e) {
                        e.printStackTrace();
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

    private void drawLineChart(List<Entry> lineEntries) throws ParseException {
        LineChart lineChart = findViewById(R.id.chartSuhu);

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Air");
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

        lineChart.setData(lineData);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                System.out.println("hari");
                try {
                    drawLineChart(getDataDaySet());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                try {
                    drawLineChart(getDataWeekSet());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                try {
                    drawLineChart(getDataMonthSet());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


    private List<Entry> getDataDaySet() throws ParseException {
        List<Entry> lineEntries = new ArrayList<Entry>();
        long size = temperature_data_entry.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < size; i++) {
            // logic disini
            Date date = simpleDateFormat.parse(temperature_dates_entry.get(i));
            int day = date.getDate();
            float dayFloat = (float) day;
            Entry entry = new Entry(dayFloat, Float.parseFloat(temperature_data_entry.get(i)));
            lineEntries.add(entry);
        }
        return lineEntries;
    }

    private List<Entry> getDataWeekSet() throws ParseException {
        List<Entry> lineEntries = new ArrayList<Entry>();
        long size = temperature_data_entry.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < size; i++) {
            Date date = simpleDateFormat.parse(temperature_dates_entry.get(i));
            int week = date.getDay();
            float weekFloat = (float) week;
            Entry entry = new Entry(weekFloat, Float.parseFloat(temperature_data_entry.get(i)));
            lineEntries.add(entry);
        }
        return lineEntries;
    }

    private List<Entry> getDataMonthSet() throws ParseException {
        List<Entry> lineEntries = new ArrayList<Entry>();
        long size = temperature_data_entry.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < size; i++) {
            Date date = simpleDateFormat.parse(temperature_dates_entry.get(i));
            int month = date.getMonth();
            float monthFloat = (float) month;
            Entry entry = new Entry(monthFloat, Float.parseFloat(temperature_data_entry.get(i)));
            lineEntries.add(entry);
        }
        return lineEntries;
    }


}
