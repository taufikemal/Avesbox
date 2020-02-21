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

import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.R;

public class AdapterListSensor extends RecyclerView.Adapter<AdapterListSensor.CustomViewHolder> {
    List<DataSensor> listSensor;

    public AdapterListSensor(List<DataSensor> listSensor) {
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
        DataSensor listViewSensor = listSensor.get(position);
        holder.txtID.setText(String.valueOf(listViewSensor.getKodeSensor()));
        holder.txtUpdate.setText(listViewSensor.getTanggal());
        holder.txtKeterangan.setText(listViewSensor.getNamaSensor());

    }

    @Override
    public int getItemCount() {
        return listSensor.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtKeterangan, txtUpdate, txtID;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.idTVSensor1);
            txtKeterangan = itemView.findViewById(R.id.jenisTVSensor1);
            txtUpdate = itemView.findViewById(R.id.updateTVsensor1);
        }
    }

    public void refreshEvents(List<DataSensor> listSensor) {
        this.listSensor.clear();
        this.listSensor.addAll(listSensor);
        notifyDataSetChanged();
    }
}
