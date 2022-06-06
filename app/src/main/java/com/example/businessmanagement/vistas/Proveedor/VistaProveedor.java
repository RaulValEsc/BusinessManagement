package com.example.businessmanagement.vistas.Proveedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Proveedor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaProveedor extends AppCompatActivity {

    DatabaseReference database;

    public static Proveedor proveedor;

    public static String nif;

    Bundle b;

    Toolbar tbProveedor;
    public static TextView tvNombre, tvNif,tvEmail,tvTelefono;
    ImageView ivProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_proveedor);

        b = getIntent().getExtras();
        nif = b.getString("nif");

        tvNombre = findViewById(R.id.tvNombreProveedor);
        tvNif = findViewById(R.id.tvNif);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        ivProveedor = findViewById(R.id.ivProveedor);
        tbProveedor = findViewById(R.id.tbProveedor);

        cargarCliente();

    }

    private void cargarToolbar(){
        setSupportActionBar(tbProveedor);
        getSupportActionBar().setTitle(proveedor.getNombre());
    }

    private void cargarCliente() {
        database = FirebaseDatabase.getInstance().getReference().child("Proveedores");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    if(child.child("nif").getValue().toString().equals(nif)){
                        proveedor = child.getValue(Proveedor.class);

                        Glide.with(getApplicationContext()).load(proveedor.getImageUri()).into(ivProveedor);
                        tvNombre.setText(proveedor.getNombre());
                        tvNif.setText(proveedor.getNif());
                        tvEmail.setText(proveedor.getEmail());
                        tvTelefono.setText(proveedor.getTelefono());
                    }
                }
                cargarToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proveedores,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarProveedor.class);
            intent.putExtra("nif", proveedor.getNif());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {

            EditarProveedor.aux=true;
            database = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(proveedor.getNif());
            database.removeValue();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}