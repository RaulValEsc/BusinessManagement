package com.example.businessmanagement.vistas.Venta;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.VentaAdapter;
import com.example.businessmanagement.modelos.Venta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityVentas extends AppCompatActivity {

    RecyclerView rvVentas;
    ArrayList<Venta> arrayVentas;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        arrayVentas = new ArrayList<Venta>();

        database= FirebaseDatabase.getInstance().getReference();

        rvVentas = findViewById(R.id.recyclerViewVentas);

        cargarVentas();
    }

    private void cargarVentas() {
        database.child("Ventas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayVentas.clear();
                rvVentas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvVentas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                rvVentas.setAdapter(new VentaAdapter(arrayVentas));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    arrayVentas.add(child.getValue(Venta.class));
                    rvVentas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvVentas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvVentas.setAdapter(new VentaAdapter(arrayVentas));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
