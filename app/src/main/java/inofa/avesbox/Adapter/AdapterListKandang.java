package inofa.avesbox.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import inofa.avesbox.Model.DataKandang;
import inofa.avesbox.Model.DataSensor;
import inofa.avesbox.R;

public class AdapterListKandang extends RecyclerView.Adapter<AdapterListKandang.CustomViewHolder>{
    List <DataKandang> listKandang;
    public AdapterListKandang(List<DataKandang>listKandang){
        this.listKandang = listKandang;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_kandang, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        DataKandang listViewKandang = listKandang.get(position);
        holder.txtID.setText(String.valueOf(listViewKandang.getIdKandang()));
        holder.txtPemilik.setText(listViewKandang.getPemilik());
        holder.txtAlamat.setText(listViewKandang.getAlamat());
    }

    @Override
    public int getItemCount() {
        return listKandang.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView txtAlamat, txtPemilik, txtID;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtID = itemView.findViewById(R.id.TVidKandang);
            txtPemilik = itemView.findViewById(R.id.TVnamaPemilik);
            txtAlamat = itemView.findViewById(R.id.TValamatKandang);
        }
    }

    public void refreshEvents(List<DataKandang> listKandang) {
        this.listKandang.clear();
        this.listKandang.addAll(listKandang);
        notifyDataSetChanged();
    }
}
