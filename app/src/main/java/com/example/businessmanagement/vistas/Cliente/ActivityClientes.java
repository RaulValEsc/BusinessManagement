package com.example.businessmanagement.vistas.Cliente;

import android.content.Intent;
import android.os.Bundle;

import com.example.businessmanagement.controladores.ClienteAdapter;
import com.example.businessmanagement.modelos.Cliente;
import com.example.businessmanagement.vistas.Comercio.CrearComercio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.businessmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityClientes extends AppCompatActivity {

    FloatingActionButton fbAñadir;
    RecyclerView rvClientes;
    ArrayList<Cliente> arrayClientes;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        arrayClientes = new ArrayList<Cliente>();

        database= FirebaseDatabase.getInstance().getReference();

        fbAñadir = findViewById(R.id.fbAñadir);
        rvClientes = findViewById(R.id.recyclerViewClientes);

        cargarClientes();

        setup();
    }

    private void setup() {
        fbAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CrearCliente.class);
                startActivity(intent);
            }
        });
    }

    private void cargarClientes() {
        database.child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayClientes.clear();
                rvClientes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvClientes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvClientes.setAdapter(new ClienteAdapter(arrayClientes));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    arrayClientes.add(child.getValue(Cliente.class));
                    rvClientes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvClientes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvClientes.setAdapter(new ClienteAdapter(arrayClientes));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}