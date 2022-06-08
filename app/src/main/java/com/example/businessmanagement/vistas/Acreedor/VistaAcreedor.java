package com.example.businessmanagement.vistas.Acreedor;

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
import com.example.businessmanagement.controladores.bdLocal.SQLAcreedoresController;
import com.example.businessmanagement.modelos.Acreedor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaAcreedor extends AppCompatActivity {

    DatabaseReference database;

    public static Acreedor acreedor;

    public static String nif;

    Bundle b;

    Toolbar tbAcreedor;
    public static TextView tvNombre, tvNif,tvEmail,tvTelefono;
    ImageView ivAcreedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_acreedor);

        b = getIntent().getExtras();
        nif = b.getString("nif");

        tvNombre = findViewById(R.id.tvNombreAcreedor);
        tvNif = findViewById(R.id.tvNif);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        ivAcreedor = findViewById(R.id.ivAcreedor);
        tbAcreedor = findViewById(R.id.tbAcreedor);

        cargarAcreedor();
    }

    private void cargarToolbar(){
        setSupportActionBar(tbAcreedor);
        getSupportActionBar().setTitle(acreedor.getNombre());
    }

    private void cargarAcreedor() {
        if(isNetworkAvailable()){
            database = FirebaseDatabase.getInstance().getReference().child("Acreedores");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()){
                        if(child.child("nif").getValue().toString().equals(nif)){
                            acreedor = child.getValue(Acreedor.class);

                            Glide.with(getApplicationContext()).load(acreedor.getImageUri()).into(ivAcreedor);
                            tvNombre.setText(acreedor.getNombre());
                            tvNif.setText(acreedor.getNif());
                            tvEmail.setText(acreedor.getEmail());
                            tvTelefono.setText(acreedor.getTelefono());
                        }
                    }
                    cargarToolbar();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
            acreedor = sql.getAcreedor(nif);
            tvNombre.setText(acreedor.getNombre());
            tvNif.setText(acreedor.getNif());
            tvEmail.setText(acreedor.getEmail());
            tvTelefono.setText(acreedor.getTelefono());
            cargarToolbar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acreedores,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarAcreedor.class);
            intent.putExtra("nif", acreedor.getNif());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {

            if(acreedor.getNombre().isEmpty()){
                acreedor.setNombre("nombreDefault");
            }
            if(acreedor.getEmail().isEmpty()){
                acreedor.setEmail("emailDefault");
            }
            if(acreedor.getTelefono().isEmpty()){
                acreedor.setTelefono("telefonoDefault");
            }

            EditarAcreedor.aux=true;
            SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
            if(isNetworkAvailable()){
                database = FirebaseDatabase.getInstance().getReference().child("Acreedores").child(acreedor.getNif());
                database.removeValue();
            }else{
                sql.borrarAcreedorAux(acreedor);
            }
            sql.borrarAcreedor(nif);
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
