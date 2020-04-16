package inofa.avesbox.Adapter;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import inofa.avesbox.Model.DataListSensor;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.R;

public class AdapterListSensor extends RecyclerView.Adapter<AdapterListSensor.CustomViewHolder> {
    List<DataListSensor> listSensor;

    public AdapterListSensor(List<DataListSensor> listSensor) {
        this.listSensor = listSensor;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_sensor, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        DataListSensor listViewSensor = listSensor.get(position);
        holder.txtIdSensor.setText(String.valueOf(listViewSensor.getIdSensor()));
        holder.txtIdDevice.setText(String.valueOf(listViewSensor.getIdDevice()));
        holder.txtIdKandang.setText(String.valueOf(listViewSensor.getIdKandang()));
        holder.txtNamaSensor.setText(listViewSensor.getSensor());
        holder.txtAlamat.setText(listViewSensor.getAlamat());
        holder.txtKeterangan.setText(listViewSensor.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return listSensor.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtKeterangan, txtNamaSensor, txtIdSensor, txtIdDevice, txtIdKandang, txtAlamat ;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtIdDevice = itemView.findViewById(R.id.TVidDevice);
            txtKeterangan = itemView.findViewById(R.id.keteranganTVSensor);
            txtIdKandang = itemView.findViewById(R.id.TVidKandang);
            txtIdSensor = itemView.findViewById(R.id.TVidSensor);
            txtAlamat = itemView.findViewById(R.id.alamatTVSensor);
            txtNamaSensor = itemView.findViewById(R.id.namaTVsensor);
        }
    }

    public void refreshEvents(List<DataListSensor> listSensor) {
        this.listSensor.clear();
        this.listSensor.addAll(listSensor);
        notifyDataSetChanged();
    }
}
