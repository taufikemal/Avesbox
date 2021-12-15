package inofa.avesbox.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginRespon;
import inofa.avesbox.Model.LoginResponUser;
import inofa.avesbox.data.model.LoginResponse;

public class SharePrefManager {

    private static final String SP_AVES = "spAvesBox";
    private static SharePrefManager mInstance;
    private Context mContext;
    private static boolean defVal = true;

    private SharePrefManager(Context mContext){
        this.mContext = mContext;
    }

    public static synchronized SharePrefManager getInstance(Context mContext){
        if(mInstance == null){
            mInstance = new SharePrefManager(mContext);
        }
        return mInstance;
    }
    public void saveUser(LoginResponse login){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", login.getToken());
        Gson gson = new Gson();
//        String serializeData = gson.toJson(login.getDataUser());
//        editor.putString("data", serializeData);
//        editor.putInt("id_users", login.getDataUser().getId_users());
        editor.apply();
    }
    public void saveUserUpdate(DetailUserRespon detailUserRespon){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String serializeData = gson.toJson(detailUserRespon.getDetailUser());
        editor.putString("data", serializeData);
        editor.apply();
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", null) != null;
//        return sharedPreferences.getInt("id_users", 0) ;
    }

//    public LoginRespon getUser(){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
//        return new LoginRespon(
//                sharedPreferences.getInt("code",0),
//                sharedPreferences.getString("token", null),
//                sharedPreferences.getString("data", "")
//        );
//    }

    public void clear(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
