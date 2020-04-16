package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataListSensorRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("result")
    @Expose
    private ArrayList<DataListSensor> dataListSensors = null;

    public DataListSensorRespon(int code, ArrayList<DataListSensor> dataListSensors){
        this.code = code;
        this.dataListSensors = dataListSensors;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<DataListSensor> getDataListSensors() {
        return dataListSensors;
    }

    public void setDataListSensors(ArrayList<DataListSensor> dataListSensors) {
        this.dataListSensors = dataListSensors;
    }
}
