package com.example.businessmanagement.controladores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Compra;

import java.util.ArrayList;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Compra> listaCompra;

    public CompraAdapter(ArrayList<Compra> listaCompra) {
        this.listaCompra = listaCompra;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compra,null,false);
        return new CompraViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final CompraViewHolder holder, int position) {

        holder.asignarDatos(listaCompra.get(position));

    }

    @Override
    public int getItemCount() {
        return listaCompra.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class CompraViewHolder extends RecyclerView.ViewHolder {

        TextView stock, codigoProducto;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            stock=itemView.findViewById(R.id.tvStockCompra);
            codigoProducto=itemView.findViewById(R.id.tvCodigoProductoCompra);
        }

        public void asignarDatos(Compra compra) {

            stock.setText(""+compra.getStock());

            codigoProducto.setText(compra.getIdProducto());

        }
    }
}

