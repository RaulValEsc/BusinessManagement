package com.example.businessmanagement.vistas.Compra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.businessmanagement.R;
import com.example.businessmanagement.controladores.AcreedorAdapter;
import com.example.businessmanagement.controladores.CompraAdapter;
import com.example.businessmanagement.controladores.bdLocal.SQLComprasController;
import com.example.businessmanagement.modelos.Compra;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityCompras extends AppCompatActivity {

    RecyclerView rvCompras;
    ArrayList<Compra> arrayCompras;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);

        arrayCompras = new ArrayList<Compra>();

        if(isNetworkAvailable()){
            database= FirebaseDatabase.getInstance().getReference();
        }

        rvCompras = findViewById(R.id.recyclerViewCompras);

        cargarCompras();
    }

    private void cargarCompras() {
        if(isNetworkAvailable()){
            database.child("Compras").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrayCompras.clear();
                    rvCompras.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvCompras.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    rvCompras.setAdapter(new CompraAdapter(arrayCompras));
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        arrayCompras.add(child.getValue(Compra.class));
                        rvCompras.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rvCompras.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                        rvCompras.setAdapter(new CompraAdapter(arrayCompras));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }else {
            SQLComprasController sql = new SQLComprasController(getApplicationContext());
            arrayCompras = sql.cargarCompras();
            rvCompras.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvCompras.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            rvCompras.setAdapter(new CompraAdapter(arrayCompras));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}