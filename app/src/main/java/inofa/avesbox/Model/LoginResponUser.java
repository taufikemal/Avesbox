package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponUser {
    @SerializedName("id_users")
    @Expose
    private int id_users;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    public LoginResponUser (int id_users, String nama, String username, String alamat){
        this.id_users = id_users;
        this.nama = nama;
        this.username = username;
        this.alamat = alamat;
    }

    public int getId_users() {
        return id_users;
    }

    public void setId_users(int id_users) {
        this.id_users = id_users;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
