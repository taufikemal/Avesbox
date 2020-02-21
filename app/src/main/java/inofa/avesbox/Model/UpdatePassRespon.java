package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePassRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("user")
    @Expose
    private DetailUser detailUser;

    public UpdatePassRespon(int code, DetailUser detailUser){
        this.code = code;
        this.detailUser = detailUser;
    }

    public int getCode() {
        return code;
    }

    public DetailUser getDetailUser() {
        return detailUser;
    }
}
