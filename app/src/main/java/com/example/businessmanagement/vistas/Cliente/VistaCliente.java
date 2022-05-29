package com.example.businessmanagement.vistas.Cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Comercio;
import com.example.businessmanagement.vistas.Comercio.EditarComercio;
import com.example.businessmanagement.vistas.Producto.ActivityProductos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VistaCliente extends AppCompatActivity {

    DatabaseReference database;

    public static String nombrecliente, dnicliente;

    Bundle b;

    boolean aux=false;

    Toolbar tbComercio;
    public static TextView tvNombre,tvDescripcion;
    Button bProductos;
    ImageView ivComercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_comercio);

        productos = new ArrayList<String>();
        productosNombre = new ArrayList<String>();
        pedidos = new ArrayList<String>();

        b = getIntent().getExtras();
        nombrecliente = b.getString("nombre_comercio");
        dnicliente = b.getString("descripcion");

        tvNombre = findViewById(R.id.tvNombreComercio);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        bProductos = findViewById(R.id.bProductos);
        ivComercio = findViewById(R.id.ivComercio);
        tbComercio = findViewById(R.id.tbComercio);

        cargarComercio();

        tvNombre.setText(nombrecliente);
        tvDescripcion.setText(dnicliente);

        setSupportActionBar(tbComercio);
        getSupportActionBar().setTitle(nombrecliente);

        setup();

    }

    private void cargarComercio() {
        database = FirebaseDatabase.getInstance().getReference().child("comercios");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    if(child.child("nombre").getValue().toString().equals(nombrecliente)){
                        Comercio c = child.getValue(Comercio.class);
                        Glide.with(getApplicationContext()).load(c.getImageUri()).into(ivComercio);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setup() {
        bProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityProductos.class);
                intent.putExtra("comercio", nombrecliente);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_comercios,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuEditar) {

            Intent intent = new Intent(getApplicationContext(), EditarComercio.class);
            intent.putExtra("nombre",tvNombre.getText().toString());
            intent.putExtra("descripcion",tvDescripcion.getText().toString());
            startActivity(intent);

        } else if (id == R.id.menuBorrar) {
            EditarComercio.aux=true;
            borrarproductos=false;
            aux=false;
            finish();
            database = FirebaseDatabase.getInstance().getReference().child("comercios").child(nombrecliente);
            database.removeValue();
            database = FirebaseDatabase.getInstance().getReference().child("productos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!borrarproductos) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.child("idComercio").getValue().toString().equals(nombrecliente)) {
                                productos.add(child.getKey());
                                productosNombre.add(child.child("nombre").getValue().toString());
                            }
                        }
                        for (int i = 0; i < productos.size(); i++) {
                            database = FirebaseDatabase.getInstance().getReference().child("productos").child(productos.get(i));
                            database.removeValue();
                        }
                        for (int i = 0; i < productosNombre.size(); i++) {
                            database = FirebaseDatabase.getInstance().getReference().child("pedidos");
                            int finalI = i;
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!borrarproductos) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            if (child.child("idProd").getValue().toString().equals(productosNombre.get(finalI))) {
                                                pedidos.add(child.getKey());
                                            }
                                        }
                                        borrarproductos=true;
                                        for (int i = 0; i < pedidos.size(); i++) {
                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(pedidos.get(i));
                                            database.removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
}