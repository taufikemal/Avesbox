package inofa.avesbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

public class SuhuLembapActivity extends AppCompatActivity {
    LineChartView lineChartView;
    Context mContex;
    List<String> temperature_data_entry = new ArrayList<>();
    List<String> temperature_dates_entry = new ArrayList<>();
    Float nilai;
    String tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu_lembap);
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

                            List<PointValue> yAxisValues = new ArrayList();
                            List<AxisValue> axisValues = new ArrayList();
                            Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
                            Log.d("this is my array", "arr: " + TextUtils.join(" ", axisValues));

                            for (int i = 0; i < arrayDataSensor.size(); i++) {
                                DataSensor dataSensor = arrayDataSensor.get(i);
                                if (dataSensor.getKodeSensor() == 6) {
                                    List<PointValue> sumbuY = new ArrayList<>();
                                    List<AxisValue> sumbuX = new ArrayList<>();

                                    axisValues.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
                                    float data = (float) dataSensor.getNilai();
                                    yAxisValues.add(new PointValue(i, data));
//                                    axisValues.add(new AxisValue(i).setLabel(dataSensor.getTanggal()));
//                                    float data = (float) dataSensor.getNilai();
//                                    yAxisValues.add(new PointValue(i, data));
                                }
                            }

                            List lines = new ArrayList();
                            lines.add(line);

                            LineChartData data = new LineChartData();
                            data.setLines(lines);

                            Axis axis = new Axis();
                            axis.setValues(axisValues);
                            axis.setTextSize(16);
                            axis.setTextColor(Color.parseColor("#03A9F4"));
                            data.setAxisXBottom(axis);

                            Axis yAxis = new Axis();
                            yAxis.setName("Sales in millions");
                            yAxis.setTextColor(Color.parseColor("#03A9F4"));
                            yAxis.setTextSize(16);
                            data.setAxisYLeft(yAxis);

                            lineChartView.setLineChartData(data);
                            Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
                            viewport.top = 110;
                            lineChartView.setMaximumViewport(viewport);
                            lineChartView.setCurrentViewport(viewport);

//                            lchart();
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

    private void lchart() {


        for (int i = 0; i < temperature_dates_entry.size(); i++) {

        }

        for (int i = 0; i < temperature_data_entry.size(); i++) {

        }


    }
}
