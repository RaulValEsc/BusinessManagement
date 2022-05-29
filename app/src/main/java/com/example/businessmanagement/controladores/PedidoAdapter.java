package com.example.businessmanagement.controladores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Pedido;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.vistas.Pedido.VistaPedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> implements View.OnClickListener{

    Context c;
    ArrayList<Pedido> listaPedidos;

    DatabaseReference database;

    public PedidoAdapter(ArrayList<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido,null,false);
        return new PedidoViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final PedidoViewHolder holder, int position) {

        holder.asignarDatos(listaPedidos.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c.getApplicationContext(), VistaPedido.class);
                intent.putExtra("comercio",holder.comercio.getText().toString());
                intent.putExtra("producto",holder.producto.getText().toString());
                intent.putExtra("cantidad",holder.cantidad.getText().toString());
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        String uid;
        TextView comercio,producto,cantidad,estado;
        ImageView ivItemComercio;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            c = itemView.getContext();

            cantidad = itemView.findViewById(R.id.tvCantidadPedido);
            ivItemComercio=itemView.findViewById(R.id.ivItemPedido);
            estado=itemView.findViewById(R.id.tvEstadoPedido);
            comercio=itemView.findViewById(R.id.tvNombreComercioPedido);
            producto=itemView.findViewById(R.id.tvNombreProducto);
        }

        public void asignarDatos(Pedido pedido) {

            comercio.setText(""+pedido.getIdCom());
            producto.setText(pedido.getIdProd());

            database = FirebaseDatabase.getInstance().getReference().child("productos");

            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot child : snapshot.getChildren()) {

                            if (child.child("nombre").getValue().equals(pedido.getIdProd())) {

                                Producto prod = child.getValue(Producto.class);


                                Glide.with(itemView.getContext()).load(prod.getImageUri()).into(ivItemComercio);

                                cantidad.setText("Cantidad : " + pedido.getCantidad());


                                if (pedido.isPendiente()) {

                                    estado.setText("Pedido pendiente de confirmación");
                                    estado.setTextColor(Color.GRAY);

                                }

                                if (pedido.isConfirmado()) {

                                    estado.setText("Pedido confirmado");
                                    estado.setTextColor(Color.GREEN);

                                }

                                if (pedido.isRechazado()) {

                                    estado.setText("Pedido rechazado");
                                    estado.setTextColor(Color.RED);

                                }

                                if (pedido.isEntregado()) {

                                    estado.setText("Pedido entregado");
                                    estado.setTextColor(Color.MAGENTA);

                                }

                                if (pedido.isEnviado()) {

                                    estado.setText("Pedido enviado");
                                    estado.setTextColor(Color.BLUE);

                                }

                            }

                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
