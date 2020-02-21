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
    private ArrayList<DetailUser> detailUser = null;

    public DetailUserRespon(int code, ArrayList<DetailUser> detailUser){
        this.code = code;
        this.detailUser = detailUser;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<DetailUser> getDetailUser() {
        return detailUser;
    }
//
    public void setDetailUser(ArrayList<DetailUser> detailUser) {
        this.detailUser = detailUser;
    }
}
