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
import com.example.businessmanagement.modelos.Tarjeta;
import com.example.businessmanagement.modelos.Usuario;
import com.example.businessmanagement.vistas.Pedido.ActivityPedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    ArrayList<Usuario> listaUsuarios;
    Context c;


    public UsuarioAdapter(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,null,false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {

        holder.asignarDatos(listaUsuarios.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ActivityPedidos.class);
                intent.putExtra("nombreusuario",holder.iduser);
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {

        String iduser;
        TextView nombre,saldo;
        ImageView ivUsuario;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            c=itemView.getContext();

            saldo=itemView.findViewById(R.id.tvSaldo);
            nombre=itemView.findViewById(R.id.tvNombre);
            ivUsuario=itemView.findViewById(R.id.ivItemUsuario);
        }

        public void asignarDatos(Usuario usuario) {

            Tarjeta t = usuario.getTarjeta();

            Glide.with(itemView.getContext()).load(usuario.getImageUri()).into(ivUsuario);
            saldo.setText(Double.toString(t.getSaldo()));
            nombre.setText(usuario.getNombre());

            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("usuarios");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child:snapshot.getChildren()){
                        if (child.child("nombre").getValue().toString().equals(usuario.getNombre())){
                            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+child.getKey());
                            iduser= child.getKey();
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
