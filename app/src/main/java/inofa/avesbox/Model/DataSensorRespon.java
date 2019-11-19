package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataSensorRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("result")
    @Expose
    private ArrayList<DataSensor> dataSensors = null;

    public DataSensorRespon(int code, ArrayList<DataSensor> dataSensors){
        this.code = code;
        this.dataSensors = dataSensors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<DataSensor> getDataSensors() {
        return dataSensors;
    }

    public void setDataSensors(ArrayList<DataSensor> dataSensors) {
        this.dataSensors = dataSensors;
    }
}
