package inofa.avesbox;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

public class AirPakanActivity extends AppCompatActivity  {

    private LineChart mCahrt;
    Context mContex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_pakan);
        mCahrt = findViewById(R.id.chartSuhu);
        mCahrt.setTouchEnabled(true);
        mCahrt.setPinchZoom(true);
        mCahrt.setDragEnabled(true);
        mCahrt.setScaleEnabled(true);


        mCahrt.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 50f));
        yValues.add(new Entry(2, 70f));
        yValues.add(new Entry(3, 30f));
        yValues.add(new Entry(4, 50f));
        yValues.add(new Entry(5, 60f));

        LineDataSet lineDataSet1 = new LineDataSet(yValues, "Dataset1");
        lineDataSet1.setFillAlpha(110);
        lineDataSet1.setColor((R.color.WarnaDominan));
        lineDataSet1.setLineWidth(3f);
        lineDataSet1.setValueTextSize(15f);

        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSet1);
        setData();
//        LineDataSet lineDataSet = new LineDataSet(setData(),"Suhu");
//        lineDataSet.setColor(ContextCompat.getColor(this, R.color.BiruSoft));
//        lineDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.WarnaDominan));
//        XAxis xAxis = mCahrt.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        final DefaultAxisValueFormatter valueFormatter = new DefaultAxisValueFormatter(){
//            @Override
//            public int getAxisLabel(float value, AxisBase axis){
////                for (int i =0; i < setData().size(); ){
////                    getAxisLabel()
////                }
//            }
//        };
//        xAxis.setGranularity(1f);
////        xAxis.setValueFormatter();
//        YAxis yAxisRight = mCahrt.getAxisRight();
//        yAxisRight.setEnabled(false);
//
//        YAxis yAxisLeft = mCahrt.getAxisLeft();
//        yAxisLeft.setGranularity(1f);

//        LineData data = new LineData(lineDataSet1);
//        mCahrt.setData(data);
//        mCahrt.animateX(2500);
//        mCahrt.invalidate();

    }
    private ArrayList setData() {

        final ArrayList<DataSensor> arraydata = new ArrayList<>();
        LoginRespon loginRespon = SharePrefManager.getInstance(this).getUser();
        String token = loginRespon.getToken();
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
                                    filterDataSuhu = arraydata;
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

        return arraydata;
    }

}
