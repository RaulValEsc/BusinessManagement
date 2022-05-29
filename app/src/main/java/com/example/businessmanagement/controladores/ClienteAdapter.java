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
import com.example.businessmanagement.modelos.Cliente;
import com.example.businessmanagement.vistas.Cliente.VistaCliente;

import java.util.ArrayList;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Cliente> listaClientes;

    public ClienteAdapter(ArrayList<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente,null,false);
        return new ClienteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ClienteViewHolder holder, int position) {

        holder.asignarDatos(listaClientes.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, VistaCliente.class);
                intent.putExtra("nombre_cliente",holder.nombre.getText().toString());
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        ImageView ivItemCliente;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            ivItemCliente=itemView.findViewById(R.id.ivItemCliente);
            nombre=itemView.findViewById(R.id.tvNombre);
        }

        public void asignarDatos(Cliente cliente) {

            Glide.with(itemView.getContext()).load(cliente.getImageUri()).into(ivItemCliente);

            nombre.setText(cliente.getNombre());

        }
    }
}

