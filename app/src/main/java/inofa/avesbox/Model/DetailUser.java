package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailUser {
    @SerializedName("id_user")
    @Expose
    private int kode_user;

    @SerializedName("nama")
    @Expose
    private String namaUser;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    public DetailUser (int kode_user, String namaUser, String username, String alamat){
        this.kode_user = kode_user;
        this.namaUser = namaUser;
        this.namaUser = username;
        this.alamat = alamat;
    }

    public int getKode_user() {
        return kode_user;
    }

    public void setKode_user(int kode_user) {
        this.kode_user = kode_user;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
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
