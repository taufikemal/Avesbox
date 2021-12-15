package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Rest.ApiClient;
import inofa.avesbox.Storage.SharePrefManager;
import inofa.avesbox.Storage.SharePrefManagerUser;
import inofa.avesbox.data.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username;
    EditText password;
    Context mContext;
    ProgressDialog loading;
    LinearLayout formLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.button_login);
        username = findViewById(R.id.EtUsername);
        password = findViewById(R.id.EtPassword);
        formLogin = findViewById(R.id.formLogin);
        mContext = this;

        if(SharePrefManager.getInstance(LoginActivity.this).isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                loginUser();
            }
        });
    }

    private void loginUser() {
        String uname = username.getText().toString().trim();
        String pass = password.getText().toString().trim();


        if (uname.isEmpty()) {
            loading.dismiss();
            username.setError("Username is required");
            username.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            loading.dismiss();
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        Call<LoginResponse> call = ApiClient
                .getInstance()
                .getApi()
                .loginUser(uname, pass);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
//                DetailUserRespon detailUserRespon = response.body();
                loading.dismiss();
                if (response.isSuccessful()) {
                    if (loginResponse.getCode() == 200) {
                        SharePrefManager.getInstance(LoginActivity.this).saveUser(loginResponse);
//                        SharePrefManagerUser.getInstance(LoginActivity.this).saveUserUpdate(loginResponse.getDataUser());
//                        SharePrefManager.getInstance(LoginActivity.this).saveUserUpdate(detailUserRespon);
                        Toast.makeText(mContext, "Login successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Username password salah!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContext, "Something wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}