package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSensor {

    @SerializedName("id_record")
    @Expose
    private int kodeRecord;

    @SerializedName("id_sensor")
    @Expose
    private int kodeSensor;

    @SerializedName("value")
    @Expose
    private float nilai;

    @SerializedName("date")
    @Expose
    private String tanggal;

    @SerializedName("sensor")
    @Expose
    private String namaSensor;

    @SerializedName("satuan")
    @Expose
    private String  satuan;

    public DataSensor( int kodeRecord, int kodeSensor,int nilai, String tanggal, String namaSensor, String satuan){
        this.kodeRecord = kodeRecord;
        this.kodeSensor = kodeSensor;
        this.nilai = nilai;
        this.tanggal = tanggal;
        this.namaSensor = namaSensor;
        this.satuan = satuan;


    }

    public int getKodeRecord() {
        return kodeRecord;
    }

    public void setKodeRecord(int kodeRecord) {
        this.kodeRecord = kodeRecord;
    }

    public int getKodeSensor() {
        return kodeSensor;
    }

    public void setKodeSensor(int kodeSensor) {
        this.kodeSensor = kodeSensor;
    }

    public float getNilai() {
        return nilai;
    }

    public void setNilai(float nilai) {
        this.nilai = nilai;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaSensor() {
        return namaSensor;
    }

    public void setNamaSensor(String namaSensor) {
        this.namaSensor = namaSensor;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
