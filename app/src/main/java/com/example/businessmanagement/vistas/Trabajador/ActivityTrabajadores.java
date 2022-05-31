package com.example.businessmanagement.vistas.Trabajador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.TrabajadorAdapter;
import com.example.businessmanagement.modelos.Trabajador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityTrabajadores extends AppCompatActivity {

    FloatingActionButton fbAñadir;
    RecyclerView rvTrabajadores;
    ArrayList<Trabajador> arrayTrabajadores;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajadores);

        arrayTrabajadores = new ArrayList<Trabajador>();

        database= FirebaseDatabase.getInstance().getReference();

        fbAñadir = findViewById(R.id.fbAñadirTrabajador);
        rvTrabajadores = findViewById(R.id.recyclerViewTrabajadores);

        cargarTrabajadores();

        setup();
    }

    private void setup() {
        fbAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CrearTrabajador.class);
                startActivity(intent);
            }
        });
    }

    private void cargarTrabajadores() {
        database.child("Trabajadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayTrabajadores.clear();
                rvTrabajadores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvTrabajadores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvTrabajadores.setAdapter(new TrabajadorAdapter(arrayTrabajadores));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    arrayTrabajadores.add(child.getValue(Trabajador.class));
                    rvTrabajadores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvTrabajadores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvTrabajadores.setAdapter(new TrabajadorAdapter(arrayTrabajadores));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
