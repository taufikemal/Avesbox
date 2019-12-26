package inofa.avesbox;

import android.content.Context;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;

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

    }
    private void setData(){
        LoginRespon loginRespon = SharePrefManager.getInstance(this).getUser();
        String token = loginRespon.getToken();
        // retrofit suhu
        Call<DataSensorRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataHasilSensor(token);
        ArrayList<BarDataSet> dataSets = null;






    }


}
