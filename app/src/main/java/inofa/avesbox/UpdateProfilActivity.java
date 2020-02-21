package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Model.LoginResponUser;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Storage.SharePrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilActivity extends AppCompatActivity {

    EditText ETnama;
    EditText ETusername;
    EditText ETalamat;
    Button ButtonSimpan;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);

        mContext = this;
        ETnama = findViewById(R.id.localETNama);
        ETusername = findViewById(R.id.localETUsername);
        ETalamat = findViewById(R.id.localETAlamat);
        ButtonSimpan = findViewById(R.id.ButtonSimpanDataProfil);

        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        Gson gson = new Gson();
        String data = shfm.getString("data", "");
        LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        String nama = user.getNama();
        String alamat = user.getAlamat();
        String username = user.getUsername();

        ETnama.setText(nama);
        ETusername.setText(username);
        ETalamat.setText(alamat);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ButtonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, true);
                updateDataProfil();
            }
        });
    }

    private void updateDataProfil(){
        String nama = ETnama.getText().toString().trim();
        String username = ETusername.getText().toString().trim();
        String alamat = ETalamat.getText().toString().trim();
        if (nama.isEmpty()) {
            loading.dismiss();
            ETnama.setError("Nama is required");
            ETnama.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            loading.dismiss();
            ETusername.setError("Username is required");
            ETusername.requestFocus();
            return;
        }
        if (alamat.isEmpty()) {
            loading.dismiss();
            ETalamat.setError("Alamat is required");
            ETalamat.requestFocus();
            return;
        }
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        Gson gson = new Gson();
        String token = shfm.getString("token", "");
        String data = shfm.getString("data", "");
        LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        int id = user.getId_users();
        Call<DetailUserRespon> call = ApiClient
                .getInstance()
                .getApi()
                .dataProfilUser(token, id, nama, username, alamat);

        call.enqueue(new Callback<DetailUserRespon>() {
            @Override
            public void onResponse(Call<DetailUserRespon> call, Response<DetailUserRespon> response) {
                DetailUserRespon detailUserRespon = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (detailUserRespon.getCode() == 200) {
                        SharePrefManager.getInstance(UpdateProfilActivity.this).saveUserUpdate(detailUserRespon);
                        Toast.makeText(mContext, "Profil diperbaharui", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateProfilActivity.this, ProfilActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
//                    else {
//                        Toast.makeText(mContext, "Data salah!", Toast.LENGTH_LONG).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<DetailUserRespon> call, Throwable t) {
                loading.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
