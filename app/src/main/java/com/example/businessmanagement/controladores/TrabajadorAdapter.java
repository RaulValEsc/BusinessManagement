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
import com.example.businessmanagement.modelos.Trabajador;
import com.example.businessmanagement.vistas.Trabajador.VistaTrabajador;

import java.util.ArrayList;

public class TrabajadorAdapter extends RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Trabajador> listaTrabajador;

    public TrabajadorAdapter(ArrayList<Trabajador> listaTrabajador) {
        this.listaTrabajador = listaTrabajador;
    }

    @NonNull
    @Override
    public TrabajadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trabajador,null,false);
        return new TrabajadorViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final TrabajadorViewHolder holder, int position) {

        holder.asignarDatos(listaTrabajador.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, VistaTrabajador.class);
                intent.putExtra("dni",holder.dni);
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaTrabajador.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class TrabajadorViewHolder extends RecyclerView.ViewHolder {

        String dni;
        TextView nombre;
        ImageView ivItemTrabajador;

        public TrabajadorViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            ivItemTrabajador=itemView.findViewById(R.id.ivItemTrabajador);
            nombre=itemView.findViewById(R.id.tvNombre);
        }

        public void asignarDatos(Trabajador trabajador) {

            Glide.with(itemView.getContext()).load(trabajador.getImageUri()).into(ivItemTrabajador);

            nombre.setText(trabajador.getNombre());

            dni = trabajador.getDni();

        }
    }
}
