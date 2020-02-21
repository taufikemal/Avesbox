package inofa.avesbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import inofa.avesbox.Model.LoginResponUser;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Button buttonUpdateProfil = findViewById(R.id.buttonUpdateProfil);
        Button buttonUpdatePassword = findViewById(R.id.buttonUpdatePassword);
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });
        buttonUpdateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfilActivity.this, UpdateProfilActivity.class);
                startActivity(intent);

            }
        });
        SharedPreferences shfm = getSharedPreferences("spAvesBox", MODE_PRIVATE);
        Gson gson = new Gson();
        String data = shfm.getString("data", "");
        LoginResponUser user = gson.fromJson(data, LoginResponUser.class);
        String nama = user.getNama();
        String alamat = user.getAlamat();
        String username = user.getUsername();

        TextView tvNama = findViewById(R.id.localTVNama);
        tvNama.setText(nama);
        TextView tvUsername = findViewById(R.id.localTVUsername);
        tvUsername.setText(username);
        TextView tvAamat = findViewById(R.id.localTVAlamat);
        tvAamat.setText(alamat);

    }
}
