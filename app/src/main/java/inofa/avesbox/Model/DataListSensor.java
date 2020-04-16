package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataListSensor {
    @SerializedName("id_sensor")
    @Expose
    private int idSensor;

    @SerializedName("id_kandang")
    @Expose
    private int idKandang;

    @SerializedName("id_device")
    @Expose
    private int idDevice;

    @SerializedName("alamat")
    @Expose
    private String alamat;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("sensor")
    @Expose
    private String sensor;

    public DataListSensor (int idSensor, int idKandang, int idDevice, String alamat, String keterangan, String sensor){
        this.idSensor = idSensor;
        this.idKandang = idKandang;
        this.idDevice = idDevice;
        this.alamat = alamat;
        this.keterangan = keterangan;
        this.sensor = sensor;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public int getIdKandang() {
        return idKandang;
    }

    public void setIdKandang(int idKandang) {
        this.idKandang = idKandang;
    }

    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}
