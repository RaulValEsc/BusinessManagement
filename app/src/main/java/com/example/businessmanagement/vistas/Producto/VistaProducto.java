package com.example.businessmanagement.vistas.Producto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.vistas.Comercio.VistaComercio;
import com.example.businessmanagement.vistas.Dialog.DialogAñadir;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaProducto extends AppCompatActivity implements DialogAñadir.DialogListener {

    DatabaseReference database;

    public static String nombreproducto;
    public static double precioproducto;
    public static int stockactual;

    public static boolean borrarpedidos;

    boolean borrado = false;

    Producto p;

    Toolbar tbProducto;
    public static ImageView ivProducto;
    public static TextView tvNombre,tvStock,tvPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        borrado=false;
        borrarpedidos=false;

        Bundle b = getIntent().getExtras();
        nombreproducto= b.getString("nombre_producto");
        precioproducto= b.getDouble("precio");

        tvNombre=findViewById(R.id.tvNombreProducto);
        tvStock=findViewById(R.id.tvStock);
        tvPrecio=findViewById(R.id.tvPrecio);
        tbProducto=findViewById(R.id.tbProducto);
        ivProducto=findViewById(R.id.ivProducto);

        setSupportActionBar(tbProducto);
        getSupportActionBar().setTitle(nombreproducto);

        cargartvStock();
    }

    private void cargartvStock() {
        if(borrado!=true){
            database = FirebaseDatabase.getInstance().getReference().child("productos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!borrarpedidos) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (nombreproducto.equals(child.child("nombre").getValue().toString())) {
                                p = child.getValue(Producto.class);
                                tvNombre.setText(p.getNombre());
                                tvPrecio.setText("Precio : " + p.getPrecio());
                                tvStock.setText("Stock : " + p.getStock());
                                stockactual = p.getStock();
                                Glide.with(getApplicationContext()).load(p.getImageUri()).into(ivProducto);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_productos,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miEditarProducto) {

            Intent intent = new Intent(getApplicationContext(), EditarProducto.class);
            intent.putExtra("nombre",p.getNombre());
            intent.putExtra("precio",p.getPrecio());
            intent.putExtra("stock",p.getStock());
            startActivity(intent);

        } else if (id == R.id.miBorrarProducto) {

            EditarProducto.aux=true;

            finish();
            borrarpedidos=true;
            database = FirebaseDatabase.getInstance().getReference().child("productos").child(nombreproducto+"_"+VistaComercio.nombrecomercio);
            database.removeValue();
            borrarpedidos=false;
            database = FirebaseDatabase.getInstance().getReference().child("pedidos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!borrarpedidos){
                        for (DataSnapshot child:snapshot.getChildren()){
                            if (child.child("idProd").getValue().toString().equals(nombreproducto)){
                                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                borrarpedidos=true;
                                database.removeValue();
                            }
                        }borrarpedidos=false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if (id == R.id.miAnadirStock){

            DialogAñadir d = new DialogAñadir();
            d.show(getSupportFragmentManager(),"Dialog");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ingresarStock(int stock) {

        stockactual = stockactual+stock;

        database = FirebaseDatabase.getInstance().getReference().child("productos").child(p.getNombre()+"_"+VistaComercio.nombrecomercio);

        borrado=true;

        database.removeValue();

        p.setStock(stockactual);

        database = FirebaseDatabase.getInstance().getReference().child("productos").child(p.getNombre()+"_"+VistaComercio.nombrecomercio);

        database.setValue(p);

        Toast.makeText(getApplicationContext(), "Añadido "+stock+" al stock de "+p.getNombre(), Toast.LENGTH_SHORT).show();
    }
}