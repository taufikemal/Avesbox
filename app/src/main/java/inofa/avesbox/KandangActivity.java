package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import inofa.avesbox.Adapter.AdapterListKandang;
import inofa.avesbox.Adapter.AdapterListSensor;
import inofa.avesbox.Model.DataKandang;
import inofa.avesbox.Model.DataKandangRespon;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.Model.DataSensorRespon;
import inofa.avesbox.Model.LoginResponUser;
import inofa.avesbox.Rest.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KandangActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterListKandang lAdapter;
    private SwipeRefreshLayout swipeRefresh;
    LinearLayout layoutNoData;
    Context mContext;
    RelativeLayout layoutListKandang;
    ProgressDialog loading;
    ArrayList<DataKandang> kandanglist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kandang);

//        kandanglist = new ArrayList<>();
        mContext = this;
        layoutListKandang = findViewById(R.id.layoutListKandang);
        recyclerView = findViewById(R.id.recycleViewKandang);
        swipeRefresh = findViewById(R.id.swipeRefreshKandang);
        swipeRefresh.setEnabled(true);
        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (lAdapter != null) {
                    lAdapter.refreshEvents(kandanglist);
                }
                listKandang();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        listKandang();
    }

    public void listKandang(){

        final SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        Gson gson = new Gson();
        String token = shfm.getString("token", "");
        String data = shfm.getString("data", "");
        final LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        Call<DataKandangRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataListKandang(token);

        call.enqueue(new Callback<DataKandangRespon>() {

            @Override
            public void onResponse(Call<DataKandangRespon> call, Response<DataKandangRespon> response) {
                DataKandangRespon dataKandangRespon = response.body();
                if (response.isSuccessful()) {
                    if (dataKandangRespon.getCode() == 200) {
                        ArrayList<DataKandang> arrayListKandang = dataKandangRespon.getDataKandangs();
                        if (arrayListKandang.size() > 0) {
                            ArrayList<DataKandang> filterDataKandang = new ArrayList<>();
                            for (int i = 0; i < arrayListKandang.size(); i++) {
                                DataKandang listKandang = arrayListKandang.get(i);
                                if (listKandang.getIdUsers() == user.getId_users()) {
                                    kandanglist.add(listKandang);
//                                    kandanglist.add(filterDataKandang);
                                } else {
                                    Toast.makeText(KandangActivity.this, "Data tidak ada", Toast.LENGTH_LONG).show();
                                }
//                                if (listSensor.getKodeSensor() == 4) {
//                                    dataAakhir4.add(listSensor);
//                                    DataSensor list4 = dataAakhir4.get(dataAakhir4.size()-1);
//                                    sensorList.add(list4);
//                                }
//                                else if (listSensor.getKodeSensor() == 5) {
//                                    dataAakhir5.add(listSensor);
//                                    DataSensor list5 = dataAakhir5.get(dataAakhir5.size()-1);
//                                    sensorList.add(list5);
////                                    String vv = dataAakhir5.get(dataAakhir5.size()-1).getTanggal();
////                                    TextView tv = findViewById(R.id.localTVSensor);
////                                    tv.setText(vv);
//                                }
                            }
//                            sensorList.addAll();
//                            sensorList.addAll(dataAakhir5);
                        }
                    }
//                  recyclerView.setVisibility(View.VISIBLE);
                    //filter if()
//                  sensorList = response.body().getDataSensors();

                    lAdapter = new AdapterListKandang(kandanglist);
                    RecyclerView.LayoutManager eLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(eLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(lAdapter);
                }
                loading.dismiss();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataKandangRespon> call, Throwable t) {

                swipeRefresh.setRefreshing(false);
                Toast.makeText(KandangActivity.this, "Something wrong. Please try again later.", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

}

