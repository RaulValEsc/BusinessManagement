package com.example.businessmanagement.vistas.Comercio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.ClienteAdapter;
import com.example.businessmanagement.modelos.Comercio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityComercios extends AppCompatActivity {

    FloatingActionButton fButton;
    RecyclerView rvComercios;
    ArrayList<Comercio> arrayComercio;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comercios);

        arrayComercio = new ArrayList<Comercio>();

        database= FirebaseDatabase.getInstance().getReference();

        fButton = findViewById(R.id.fbAñadir);
        rvComercios = findViewById(R.id.recyclerViewUsuarios);

        cargarComercios();

        setup();
    }

    private void setup() {
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CrearComercio.class);
                startActivity(intent);
            }
        });
    }

    private void cargarComercios() {
        database.child("comercios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayComercio.clear();
                rvComercios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvComercios.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvComercios.setAdapter(new ClienteAdapter(arrayComercio));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    arrayComercio.add(child.getValue(Comercio.class));
                    rvComercios.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvComercios.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvComercios.setAdapter(new ClienteAdapter(arrayComercio));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}