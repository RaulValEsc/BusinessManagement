package com.example.businessmanagement.vistas.Producto;

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
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.vistas.Producto.EditarProducto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaProducto extends AppCompatActivity {

    DatabaseReference database;

    public static Producto producto;

    public static String codigo;

    Bundle b;

    Toolbar tbProducto;
    public static TextView tvNombre,tvCodigo,tvEmail,tvTelefono;
    ImageView ivComercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        b = getIntent().getExtras();
        codigo = b.getString("codigo");

        tvNombre = findViewById(R.id.tvNombreProducto);
        tvCodigo = findViewById(R.id.tvCodigo);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);

        ivComercio = findViewById(R.id.ivProducto);
        tbProducto = findViewById(R.id.tbProducto);

        cargarProducto();

        setSupportActionBar(tbProducto);
        getSupportActionBar().setTitle(producto.getNombre());
    }

    private void cargarProducto() {
        database = FirebaseDatabase.getInstance().getReference().child("Productos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    if(child.child("codigo").getValue().toString().equals(codigo)){
                        producto = child.getValue(Producto.class);

                        Glide.with(getApplicationContext()).load(producto.getImageUri()).into(ivComercio);
                        tvNombre.setText(producto.getNombre());
                        tvCodigo.setText(producto.getCodigo());
                        tvEmail.setText(producto.getEmail());
                        tvTelefono.setText(producto.getTelefono());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarProducto.class);
            intent.putExtra("codigo",producto.getCodigo());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {

            EditarProducto.aux=true;
            database = FirebaseDatabase.getInstance().getReference().child("Productos").child(producto.getCodigo());
            database.removeValue();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}