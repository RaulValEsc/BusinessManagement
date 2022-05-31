package com.example.businessmanagement.controladores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Proveedor;
import com.example.businessmanagement.vistas.Proveedor.VistaProveedor;

import java.util.ArrayList;

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Proveedor> listaProveedor;

    public ProveedorAdapter(ArrayList<Proveedor> listaProveedor) {
        this.listaProveedor = listaProveedor;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proveedor,null,false);
        return new ProveedorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProveedorViewHolder holder, int position) {

        holder.asignarDatos(listaProveedor.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, VistaProveedor.class);
                intent.putExtra("nif",holder.nif);
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaProveedor.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ProveedorViewHolder extends RecyclerView.ViewHolder {

        String nif;
        TextView nombre;
        ImageView ivItemProveedor;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            ivItemProveedor=itemView.findViewById(R.id.ivItemProveedor);
            nombre=itemView.findViewById(R.id.tvNombre);
        }

        public void asignarDatos(Proveedor proveedor) {

            Glide.with(itemView.getContext()).load(proveedor.getImageUri()).into(ivItemProveedor);

            nombre.setText(proveedor.getNombre());

            nif = proveedor.getNif();

        }
    }
}
