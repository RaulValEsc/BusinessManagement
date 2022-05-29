package com.example.businessmanagement.vistas.Usuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.UsuarioAdapter;
import com.example.businessmanagement.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityUsuarios extends AppCompatActivity {

    RecyclerView rvUsuarios;
    ArrayList<Usuario> arrayUsuarios;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        arrayUsuarios = new ArrayList<Usuario>();

        database= FirebaseDatabase.getInstance().getReference();

        rvUsuarios = findViewById(R.id.recyclerViewUsuarios);

        cargarUsuarios();

    }

    private void cargarUsuarios() {
        database.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayUsuarios.clear();
                rvUsuarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvUsuarios.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvUsuarios.setAdapter(new UsuarioAdapter(arrayUsuarios));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Usuario u = child.getValue(Usuario.class);
                    arrayUsuarios.add(u);
                    rvUsuarios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvUsuarios.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvUsuarios.setAdapter(new UsuarioAdapter(arrayUsuarios));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}