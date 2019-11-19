package inofa.avesbox.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import inofa.avesbox.Model.LoginRespon;

public class SharePrefManager {

    private static final String SP_AVES = "spAvesBox";
    private static SharePrefManager mInstance;
    private Context mContext;

    private SharePrefManager(Context mContext){
        this.mContext = mContext;
    }

    public static synchronized SharePrefManager getInstance(Context mContext){
        if(mInstance == null){
            mInstance = new SharePrefManager(mContext);
        }
        return mInstance;
    }
    public void saveUser(LoginRespon login){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", login.getToken());
        editor.apply();
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null) != null;
    }

    public LoginRespon getUser(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        return new LoginRespon(
                sharedPreferences.getInt("code",0),
                sharedPreferences.getString("token", null)
        );
    }
    public void clear(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SP_AVES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
