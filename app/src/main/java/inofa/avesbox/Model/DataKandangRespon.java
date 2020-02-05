package inofa.avesbox.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataKandangRespon {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("result")
    @Expose
    private ArrayList<DataKandang> dataKandangs = null;

    public DataKandangRespon (int code, ArrayList<DataKandang> dataKandangs){
        this.code = code;
        this.dataKandangs = dataKandangs;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<DataKandang> getDataKandangs() {
        return dataKandangs;
    }

    public void setDataKandangs(ArrayList<DataKandang> dataKandangs) {
        this.dataKandangs = dataKandangs;
    }
}
