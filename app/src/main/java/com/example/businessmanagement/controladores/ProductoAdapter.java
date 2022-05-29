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
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.vistas.Producto.VistaProducto;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Producto> listaProductos;
    Double preciodouble;

    public ProductoAdapter(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,null,false);
        return new ProductoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProductoViewHolder holder, int position) {

        holder.asignarDatos(listaProductos.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, VistaProducto.class);
                intent.putExtra("nombre_producto",holder.nombre.getText().toString());
                intent.putExtra("precio",preciodouble);
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,precio;
        ImageView ivProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();
            precio=itemView.findViewById(R.id.tvDescripcion);
            nombre=itemView.findViewById(R.id.tvNombre);
            ivProducto=itemView.findViewById(R.id.ivItemProducto);
        }

        public void asignarDatos(Producto producto) {

            Glide.with(itemView.getContext()).load(producto.getImageUri()).into(ivProducto);

            preciodouble = producto.getPrecio();

            precio.setText("Precio : "+producto.getPrecio());
            nombre.setText(producto.getNombre());

        }
    }
}