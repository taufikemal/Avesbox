package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private LoginResponUser dataUser;

    public LoginRespon(int code, String token, LoginResponUser dataUser) {
        this.code = code;
        this.token = token;
        this.dataUser = dataUser;
    }

    public LoginResponUser getDataUser() {
        return dataUser;
    }

    public int getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }
}
