package com.example.businessmanagement.vistas.Acreedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.AcreedorAdapter;
import com.example.businessmanagement.modelos.Acreedor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityAcreedores extends AppCompatActivity {

    FloatingActionButton fbAñadir;
    RecyclerView rvAcreedores;
    ArrayList<Acreedor> arrayAcreedores;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acreedores);

        arrayAcreedores = new ArrayList<Acreedor>();

        database= FirebaseDatabase.getInstance().getReference();

        fbAñadir = findViewById(R.id.fbAñadirAcreedor);
        rvAcreedores = findViewById(R.id.recyclerViewAcreedores);

        cargarClientes();

        setup();
    }

    private void setup() {
        fbAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CrearAcreedor.class);
                startActivity(intent);
            }
        });
    }

    private void cargarClientes() {
        database.child("Acreedores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayAcreedores.clear();
                rvAcreedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvAcreedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvAcreedores.setAdapter(new AcreedorAdapter(arrayAcreedores));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    arrayAcreedores.add(child.getValue(Acreedor.class));
                    rvAcreedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvAcreedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvAcreedores.setAdapter(new AcreedorAdapter(arrayAcreedores));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
