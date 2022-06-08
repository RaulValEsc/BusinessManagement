package com.example.businessmanagement.vistas.Proveedor;

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
import com.example.businessmanagement.controladores.ProveedorAdapter;
import com.example.businessmanagement.controladores.ProveedorAdapter;
import com.example.businessmanagement.controladores.bdLocal.SQLProveedoresController;
import com.example.businessmanagement.modelos.Proveedor;
import com.example.businessmanagement.vistas.Proveedor.CrearProveedor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityProveedores extends AppCompatActivity {

    FloatingActionButton fbAñadir;
    RecyclerView rvProveedores;
    ArrayList<Proveedor> arrayProveedores;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        arrayProveedores = new ArrayList<Proveedor>();

        if(isNetworkAvailable()){
            database= FirebaseDatabase.getInstance().getReference();
        }

        fbAñadir = findViewById(R.id.fbAñadirProveedor);
        rvProveedores = findViewById(R.id.recyclerViewProveedores);

        cargarProveedores();

        setup();
    }

    private void setup() {
        fbAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), CrearProveedor.class);
                startActivity(intent);
            }
        });
    }

    private void cargarProveedores() {
        if(isNetworkAvailable()) {
            database.child("Proveedores").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrayProveedores.clear();
                    rvProveedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvProveedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvProveedores.setAdapter(new ProveedorAdapter(arrayProveedores));
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        arrayProveedores.add(child.getValue(Proveedor.class));
                        rvProveedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvProveedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        rvProveedores.setAdapter(new ProveedorAdapter(arrayProveedores));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }else {
            SQLProveedoresController sql = new SQLProveedoresController(getApplicationContext());
            arrayProveedores = sql.cargarProveedores();
            rvProveedores.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvProveedores.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            rvProveedores.setAdapter(new ProveedorAdapter(arrayProveedores));
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

