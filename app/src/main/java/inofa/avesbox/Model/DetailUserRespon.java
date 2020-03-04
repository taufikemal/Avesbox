package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DetailUserRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("data")
    @Expose
    private LoginResponUser detailUser ;

    public DetailUserRespon(int code, LoginResponUser detailUser){
        this.code = code;
        this.detailUser = detailUser;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LoginResponUser getDetailUser() {
        return detailUser;
    }
//
    public void setDetailUser(LoginResponUser detailUser) {
        this.detailUser = detailUser;
    }
}
