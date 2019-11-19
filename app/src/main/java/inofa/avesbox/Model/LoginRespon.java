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

    public LoginRespon(int code, String token) {
        this.code = code;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }
}
