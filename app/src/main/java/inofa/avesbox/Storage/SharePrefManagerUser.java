package inofa.avesbox.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import inofa.avesbox.Model.DetailUserRespon;
import inofa.avesbox.Model.LoginResponUser;


public class SharePrefManagerUser {

    private static final String SP_AVES = "spUser";
    private static SharePrefManagerUser mInstance;
    private Context mContext;

    private SharePrefManagerUser(Context mContext){
        this.mContext = mContext;
    }

    public static synchronized SharePrefManagerUser getInstance(Context mContext){
        if(mInstance == null){
            mInstance = new SharePrefManagerUser(mContext);
        }
        return mInstance;
    }
    public void saveUserUpdate(LoginResponUser detailUserRespon){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String serializeData = gson.toJson(detailUserRespon);
        editor.putString("data", serializeData);
        editor.apply();
    }
}
