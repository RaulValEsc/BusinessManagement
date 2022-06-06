package com.example.businessmanagement.vistas.Trabajador;

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
import com.example.businessmanagement.modelos.Trabajador;
import com.example.businessmanagement.vistas.Trabajador.EditarTrabajador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaTrabajador extends AppCompatActivity {

    DatabaseReference database;

    public static Trabajador trabajador;

    public static String dni;

    Bundle b;

    Toolbar tbTrabajador;
    public static TextView tvNombre,tvDni,tvEmail,tvTelefono;
    ImageView ivComercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_trabajador);

        b = getIntent().getExtras();
        dni = b.getString("dni");

        tvNombre = findViewById(R.id.tvNombreTrabajador);
        tvDni = findViewById(R.id.tvDni);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        ivComercio = findViewById(R.id.ivTrabajador);
        tbTrabajador = findViewById(R.id.tbTrabajador);

        cargarTrabajador();
    }

    private void cargarToolbar(){
        setSupportActionBar(tbTrabajador);
        getSupportActionBar().setTitle(trabajador.getNombre());
    }

    private void cargarTrabajador() {
        database = FirebaseDatabase.getInstance().getReference().child("Trabajadores");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    if(child.child("dni").getValue().toString().equals(dni)){
                        trabajador = child.getValue(Trabajador.class);

                        Glide.with(getApplicationContext()).load(trabajador.getImageUri()).into(ivComercio);
                        tvNombre.setText(trabajador.getNombre());
                        tvDni.setText(trabajador.getDni());
                        tvEmail.setText(trabajador.getEmail());
                        tvTelefono.setText(trabajador.getTelefono());
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
        getMenuInflater().inflate(R.menu.menu_trabajadores,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarTrabajador.class);
            intent.putExtra("dni",trabajador.getDni());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {

            EditarTrabajador.aux=true;
            database = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(trabajador.getDni());
            database.removeValue();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
