package com.example.businessmanagement.vistas.Cliente;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.businessmanagement.controladores.bdLocal.SQLClientesController;
import com.example.businessmanagement.modelos.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaCliente extends AppCompatActivity {

    DatabaseReference database;

    public static Cliente cliente;

    public static String dni;

    Bundle b;

    Toolbar tbCliente;
    public static TextView tvNombre,tvDni,tvEmail,tvTelefono;
    ImageView ivComercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_cliente);

        b = getIntent().getExtras();
        dni = b.getString("dni");

        tvNombre = findViewById(R.id.tvNombreCliente);
        tvDni = findViewById(R.id.tvDni);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        ivComercio = findViewById(R.id.ivCliente);
        tbCliente = findViewById(R.id.tbCliente);

        cargarCliente();
    }

    private void cargarToolbar(){
        setSupportActionBar(tbCliente);
        getSupportActionBar().setTitle(cliente.getNombre());
    }

    private void cargarCliente() {
        if(isNetworkAvailable()) {
            database = FirebaseDatabase.getInstance().getReference().child("Clientes");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.child("dni").getValue().toString().equals(dni)) {
                            cliente = child.getValue(Cliente.class);

                            Glide.with(getApplicationContext()).load(cliente.getImageUri()).into(ivComercio);
                            tvNombre.setText(cliente.getNombre());
                            tvDni.setText(cliente.getDni());
                            tvEmail.setText(cliente.getEmail());
                            tvTelefono.setText(cliente.getTelefono());
                        }
                    }
                    cargarToolbar();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            SQLClientesController sql = new SQLClientesController(getApplicationContext());
            cliente = sql.getCliente(dni);
            tvNombre.setText(cliente.getNombre());
            tvDni.setText(cliente.getDni());
            tvEmail.setText(cliente.getEmail());
            tvTelefono.setText(cliente.getTelefono());
            cargarToolbar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clientes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarCliente.class);
            intent.putExtra("dni",cliente.getDni());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {

            if(cliente.getNombre().isEmpty()){
                cliente.setNombre("nombreDefault");
            }
            if(cliente.getEmail().isEmpty()){
                cliente.setEmail("emailDefault");
            }
            if(cliente.getTelefono().isEmpty()){
                cliente.setTelefono("telefonoDefault");
            }

            EditarCliente.aux=true;
            SQLClientesController sql = new SQLClientesController(getApplicationContext());
            if(isNetworkAvailable()){
                database = FirebaseDatabase.getInstance().getReference().child("Clientes").child(cliente.getDni());
                database.removeValue();
            }else{
                sql.borrarClienteAux(cliente);
            }
            sql.borrarCliente(dni);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}