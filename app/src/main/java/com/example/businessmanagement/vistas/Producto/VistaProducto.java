package com.example.businessmanagement.vistas.Producto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.businessmanagement.controladores.bdLocal.SQLComprasController;
import com.example.businessmanagement.controladores.bdLocal.SQLProductosController;
import com.example.businessmanagement.controladores.bdLocal.SQLProductosController;
import com.example.businessmanagement.controladores.bdLocal.SQLVentasController;
import com.example.businessmanagement.modelos.Compra;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.modelos.Venta;
import com.example.businessmanagement.vistas.Producto.EditarProducto;
import com.example.businessmanagement.vistas.Dialog.DialogCompra;
import com.example.businessmanagement.vistas.Dialog.DialogVenta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaProducto extends AppCompatActivity implements DialogCompra.DialogListener, DialogVenta.DialogListener {

    DatabaseReference database;

    public static Producto producto;

    public static String codigo;

    public static boolean compraventarealizada = false, existente = false;

    Bundle b;

    Toolbar tbProducto;
    public static TextView tvNombre,tvCodigo,tvPrecio,tvStock;
    ImageView ivComercio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_producto);

        b = getIntent().getExtras();
        codigo = b.getString("codigo");

        tvNombre = findViewById(R.id.tvNombreProducto);
        tvCodigo = findViewById(R.id.tvCodigo);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvStock = findViewById(R.id.tvStock);

        ivComercio = findViewById(R.id.ivProducto);
        tbProducto = findViewById(R.id.tbProducto);

        cargarProducto();
    }

    private void cargarProducto() {
        if(isNetworkAvailable()) {
            database = FirebaseDatabase.getInstance().getReference().child("Productos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.child("codigo").getValue().toString().equals(codigo)) {
                            producto = child.getValue(Producto.class);

                            Glide.with(getApplicationContext()).load(producto.getImageUri()).into(ivComercio);
                            tvNombre.setText(producto.getNombre());
                            tvCodigo.setText(producto.getCodigo());
                            tvPrecio.setText("Precio: " + producto.getPrecio());
                            tvStock.setText("Stock: " + producto.getStock());

                        }
                    }

                    setupToolbar();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            SQLProductosController sql = new SQLProductosController(getApplicationContext());
            producto = sql.getProducto(codigo);
            tvNombre.setText(producto.getNombre());
            tvCodigo.setText(producto.getCodigo());
            tvPrecio.setText("Precio: " + producto.getPrecio());
            tvStock.setText("Stock: " + producto.getStock());
            setupToolbar();
        }
    }

    private void setupToolbar(){
        setSupportActionBar(tbProducto);
        getSupportActionBar().setTitle(producto.getNombre());
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

            if(producto.getNombre().isEmpty()){
                producto.setNombre("nombreDefault");
            }

            EditarProducto.aux=true;
            SQLProductosController sql = new SQLProductosController(getApplicationContext());
            if(isNetworkAvailable()){
                database = FirebaseDatabase.getInstance().getReference().child("Productos").child(producto.getCodigo());
                database.removeValue();
            }else{
                sql.borrarProductoAux(producto);
            }
            sql.borrarProducto(codigo);
            finish();

        } else if (id == R.id.menuComprar) {

            DialogCompra d = new DialogCompra();
            d.show(getSupportFragmentManager(),"Dialog");

        } else if (id == R.id.menuVender) {

            DialogVenta d = new DialogVenta();
            d.show(getSupportFragmentManager(),"Dialog");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ingresarStock(int stock) {
        SQLComprasController sql = new SQLComprasController(getApplicationContext());
        SQLProductosController sqlP = new SQLProductosController(getApplicationContext());
        if(isNetworkAvailable()) {
            database = FirebaseDatabase.getInstance().getReference().child("Productos").child(producto.getCodigo()).child("stock");
            database.setValue(producto.getStock() + stock);
            producto.setStock(producto.getStock() + stock);
            tvStock.setText("Stock: " + producto.getStock());

            existente = false;
            compraventarealizada = false;

            Compra compra = new Compra(producto.getCodigo(), stock, producto.getPrecio() * stock);
            database = FirebaseDatabase.getInstance().getReference().child("Compras");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (compraventarealizada == false) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (child.child("idProducto").getValue().toString().equals(compra.getIdProducto())) {
                                Compra compra1 = child.getValue(Compra.class);

                                compra1.setStock(compra1.getStock() + stock);

                                database.child(compra1.getIdProducto()).setValue(compra1);

                                existente = true;

                                compraventarealizada = true;

                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if(existente == false){
                database.child(compra.getIdProducto()).setValue(compra);
            }
        } else {
            producto.setStock(producto.getStock()-stock);
            tvStock.setText("Stock: " + producto.getStock());

            sqlP.borrarProductoAux(producto);
            sqlP.anadirProductoAux(producto);

            Compra compra = new Compra(producto.getCodigo(), stock, producto.getPrecio() * stock);
            sql.borrarCompraAux(compra);
            sql.anadirCompraAux(compra);
        }
        sqlP.modificaProducto(producto);

        Compra compra = new Compra(producto.getCodigo(), stock, producto.getPrecio() * stock);
        if(sql.anadirCompra(compra)==-1){
            sql.modificaCompra(compra);
        }
    }

    @Override
    public void venderStock(int stock) {
        if(producto.getStock()>stock){
            SQLVentasController sql = new SQLVentasController(getApplicationContext());
            SQLProductosController sqlP = new SQLProductosController(getApplicationContext());
            if(isNetworkAvailable()) {
                database = FirebaseDatabase.getInstance().getReference().child("Productos").child(producto.getCodigo()).child("stock");
                database.setValue(producto.getStock() - stock);
                producto.setStock(producto.getStock()-stock);
                tvStock.setText("Stock: " + producto.getStock());

                existente = false;
                compraventarealizada = false;

                Venta venta = new Venta(producto.getCodigo(), stock, producto.getPrecio() * stock);
                database = FirebaseDatabase.getInstance().getReference().child("Ventas");
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (compraventarealizada == false) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (child.child("idProducto").getValue().toString().equals(venta.getIdProducto())) {
                                    Venta venta1 = child.getValue(Venta.class);

                                    venta1.setStock(venta1.getStock() + stock);

                                    database.child(venta1.getIdProducto()).setValue(venta1);

                                    existente = true;

                                    compraventarealizada = true;

                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (existente == false) {
                    database.child(venta.getIdProducto()).setValue(venta);
                }
            } else {
                producto.setStock(producto.getStock()-stock);
                tvStock.setText("Stock: " + producto.getStock());

                sqlP.borrarProductoAux(producto);
                sqlP.anadirProductoAux(producto);

                Venta venta = new Venta(producto.getCodigo(), stock, producto.getPrecio() * stock);
                sql.borrarVentaAux(venta);
                sql.anadirVentaAux(venta);
            }
                sqlP.modificaProducto(producto);

                Venta venta = new Venta(producto.getCodigo(), stock, producto.getPrecio() * stock);
                if(sql.anadirVenta(venta)==-1){
                    sql.modificaVenta(venta);
                }
        }else{
            Toast.makeText(getApplicationContext(), "No puedes vender más unidades que el stock disponible.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}