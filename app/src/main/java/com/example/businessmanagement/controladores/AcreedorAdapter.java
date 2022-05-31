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
import com.example.businessmanagement.modelos.Acreedor;
import com.example.businessmanagement.vistas.Acreedor.VistaAcreedor;

import java.util.ArrayList;

public class AcreedorAdapter extends RecyclerView.Adapter<AcreedorAdapter.AcreedorViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Acreedor> listaAcreedor;

    public AcreedorAdapter(ArrayList<Acreedor> listaAcreedor) {
        this.listaAcreedor = listaAcreedor;
    }

    @NonNull
    @Override
    public AcreedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acreedor,null,false);
        return new AcreedorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final AcreedorViewHolder holder, int position) {

        holder.asignarDatos(listaAcreedor.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, VistaAcreedor.class);
                intent.putExtra("nif",holder.nif);
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaAcreedor.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class AcreedorViewHolder extends RecyclerView.ViewHolder {

        String nif;
        TextView nombre;
        ImageView ivItemAcreedor;

        public AcreedorViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            ivItemAcreedor=itemView.findViewById(R.id.ivItemAcreedor);
            nombre=itemView.findViewById(R.id.tvNombre);
        }

        public void asignarDatos(Acreedor acreedor) {

            Glide.with(itemView.getContext()).load(acreedor.getImageUri()).into(ivItemAcreedor);

            nombre.setText(acreedor.getNombre());

            nif = acreedor.getNif();

        }
    }
}
