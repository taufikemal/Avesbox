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

    List<PointValue> sumbuY1 = new ArrayList<>();
    List<PointValue> yAxisValues1 = new ArrayList();

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
                        List<DataSensor> arrayDataSensor = dataSensorRespon.getDataSensors();
                        if (arrayDataSensor.size() > 0) {
                            // lchart
                            lineChartView = findViewById(R.id.chart);

                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 4) {

//                                     kodingan asli
                                    sumbuX.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
                                    float data = (float) dataSensor.getNilai();
                                    sumbuY.add(new PointValue(i, data));

                                }
                                else if(dataSensor.getKodeSensor() == 6){
                                    // line 2
                                    float data = (float) dataSensor.getNilai();
                                    sumbuY1.add(new PointValue(i, data));
                                }
                            }
                            axisValues = sumbuX.subList(sumbuX.size()-start, sumbuX.size());
                            yAxisValues = sumbuY.subList(sumbuY.size()-start, sumbuY.size());
                            //line 2
                            yAxisValues1 = sumbuY1.subList(sumbuY1.size()-start, sumbuY1.size());

                            Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
//                            line.setStrokeWidth(2);
//                            line.setPointRadius(0);
                            Line line2 = new Line(yAxisValues1).setColor(Color.parseColor("#0000FF"));
                            List<Line> lines = new ArrayList();
                            lines.add(line);
                            lines.add(line2);
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
//                            yAxis.setName("Sales in millions");
                            yAxis.setTextColor(Color.parseColor("#03A9F4"));
                            yAxis.setTextSize(16);
                            data.setAxisYLeft(yAxis);

                            lineChartView.setLineChartData(data);
                            Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                            viewport.top = 40;
                            lineChartView.setMaximumViewport(viewport);
                            lineChartView.setCurrentViewport(viewport);
                        }
                        //
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
//                axisValues = sumbuX.subList(sumbuX.size()-5, sumbuX.size());
//                yAxisValues = sumbuY.subList(sumbuY.size()-5, sumbuY.size());
                start = 5;
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
//                axisValues = sumbuX.subList(sumbuX.size()-10, sumbuX.size());
//                yAxisValues = sumbuY.subList(sumbuY.size()-10, sumbuY.size());
                start = 10;
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
//                axisValues = sumbuX.subList(sumbuX.size()-15, sumbuX.size());
//                yAxisValues = sumbuY.subList(sumbuY.size()-15, sumbuY.size());
                start = 15;
                break;
            case 3:
                // Whatever you want to happen when the thrid item gets selected
//                axisValues = sumbuX.subList(sumbuX.size()-20, sumbuX.size());
//                yAxisValues = sumbuY.subList(sumbuY.size()-20, sumbuY.size());
                start = 20;
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

}
