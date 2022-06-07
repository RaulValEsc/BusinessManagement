package com.example.businessmanagement.controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Venta;

import java.util.ArrayList;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Venta> listaVenta;

    public VentaAdapter(ArrayList<Venta> listaVenta) {
        this.listaVenta = listaVenta;
    }

    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta,null,false);
        return new VentaViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final VentaViewHolder holder, int position) {

        holder.asignarDatos(listaVenta.get(position));

    }

    @Override
    public int getItemCount() {
        return listaVenta.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class VentaViewHolder extends RecyclerView.ViewHolder {

        TextView stock, codigoProducto;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            stock=itemView.findViewById(R.id.tvStockVenta);
            codigoProducto=itemView.findViewById(R.id.tvCodigoProductoVenta);
        }

        public void asignarDatos(Venta venta) {

            stock.setText(""+venta.getStock());

            codigoProducto.setText(venta.getIdProducto());

        }
    }
}
