package inofa.avesbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginResponUser;
import inofa.avesbox.Model.UpdatePassRespon;
import inofa.avesbox.Rest.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {
    private TextView etOldPass, etNewPass, etNewConfirmPass;
    ProgressDialog loading;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        mContext = this;

        etOldPass = findViewById(R.id.localEToldPass);
        etNewPass = findViewById(R.id.localETUnewPass);
        etNewConfirmPass = findViewById(R.id.localETconfirmationPass);
        Button btnConfirm = findViewById(R.id.Local_ButtonUpdatePassword);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, true);
                changePass();
            }
        });
    }

    public void changePass() {
        String oldPass = etOldPass.getText().toString().trim();
        String newPass = etNewPass.getText().toString().trim();
        String newConfirmPass = etNewConfirmPass.getText().toString().trim();

        if (oldPass.isEmpty()) {
            loading.dismiss();
            etOldPass.setError("Old Password is required");
            etOldPass.requestFocus();
            return;
        }
        if (oldPass.length() < 8) {
            loading.dismiss();
            etOldPass.setError("Password should be at least 8 characters long");
            etOldPass.requestFocus();
            return;
        }
        if (newPass.isEmpty()) {
            loading.dismiss();
            etNewPass.setError("New Password is required");
            etNewPass.requestFocus();
            return;
        }
        if (newPass.length() < 8) {
            loading.dismiss();
            etNewPass.setError("Password should be at least 8 characters long");
            etNewPass.requestFocus();
            return;
        }

        if (newConfirmPass.isEmpty()) {
            loading.dismiss();
            etNewConfirmPass.setError("Confirm New Password is required");
            etNewConfirmPass.requestFocus();
            return;
        }

        if (newConfirmPass.length() < 8) {
            loading.dismiss();
            etNewConfirmPass.setError("Password should be at least 8 characters long");
            etNewConfirmPass.requestFocus();
            return;
        }

        if (!newPass.equals(newConfirmPass)) {
            loading.dismiss();
            etNewConfirmPass.setError("Password not matching");
            etNewConfirmPass.requestFocus();
            return;
        }
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        Gson gson = new Gson();
        String token = shfm.getString("token", "");
        String data = shfm.getString("data", "");
        LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        int id = user.getId_users();
        Call<UpdatePassRespon> call = ApiClient
                .getInstance()
                .getApi()
                .updatePassUser(token, id, oldPass, newPass, newConfirmPass);
        call.enqueue(new Callback<UpdatePassRespon>() {
            @Override
            public void onResponse(Call<UpdatePassRespon> call, Response<UpdatePassRespon> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    UpdatePassRespon changePassUserResponse = response.body();
                    if (changePassUserResponse.getCode() == 200) {
                        Toast.makeText(mContext, "Password changed", Toast.LENGTH_LONG).show();
                        UpdatePasswordActivity.this.finish();
                    } else {
                        Toast.makeText(mContext, "Old Password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UpdatePassRespon> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Something wrong. Try again later", Toast.LENGTH_LONG).show();
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
