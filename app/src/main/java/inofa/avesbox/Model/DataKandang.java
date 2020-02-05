package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataKandang {
    @SerializedName("id_kandang")
    @Expose
    private int idKandang;

    @SerializedName("id_users")
    @Expose
    private int idUsers;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("pemilik")
    @Expose
    private String pemilik;

    public DataKandang (int idKandang, int idUsers, String alamat, String keterangan, String pemilik){
        this.idKandang = idKandang;
        this.idUsers = idUsers;
        this.alamat = alamat;
        this.keterangan = keterangan;
        this.pemilik = pemilik;
    }

    public int getIdKandang() {
        return idKandang;
    }

    public void setIdKandang(int idKandang) {
        this.idKandang = idKandang;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }
}
