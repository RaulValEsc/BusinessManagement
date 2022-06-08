package com.example.businessmanagement.vistas.Acreedor;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.AcreedorAdapter;
import com.example.businessmanagement.controladores.bdLocal.SQLAcreedoresController;
import com.example.businessmanagement.controladores.bdLocal.SQLClientesController;
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

        fbAñadir = findViewById(R.id.fbAñadirAcreedor);
        rvAcreedores = findViewById(R.id.recyclerViewAcreedores);

        if(isNetworkAvailable()){
            database= FirebaseDatabase.getInstance().getReference();
        }

        cargarAcreedores();

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarAcreedores();
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

    private void cargarAcreedores() {
        if(isNetworkAvailable()){
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
        }else {
            SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
            arrayAcreedores = sql.cargarAcreedores();
            rvAcreedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvAcreedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            rvAcreedores.setAdapter(new AcreedorAdapter(arrayAcreedores));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
