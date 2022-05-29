package com.example.businessmanagement.vistas.Pedido;

import android.graphics.Color;
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
import com.example.businessmanagement.modelos.Pedido;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.modelos.Tarjeta;
import com.example.businessmanagement.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VistaPedido extends AppCompatActivity {

    String uid,comercio,producto,stock,precio;
    TextView tvestado, tvcomercio, tvproducto, tvcantidad, tvprecio;
    ImageView ivPedido;
    Toolbar tbPedido;
    private static double saldo;

    boolean borrado=false,pedidoborrado=false;

    Pedido p,p1;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pedido);

        pedidoborrado=false;

        Bundle b = getIntent().getExtras();
        comercio=b.getString("comercio");
        producto=b.getString("producto");
        stock=b.getString("stock");
        precio=b.getString("precio");

        tbPedido=findViewById(R.id.tbPedido);
        ivPedido=findViewById(R.id.ivPedido);
        tvestado =findViewById(R.id.tvEstadoPedido);
        tvcomercio =findViewById(R.id.tvNombreComercioPedido);
        tvproducto =findViewById(R.id.tvNombrePedido);
        tvcantidad =findViewById(R.id.tvStockPedido);
        tvprecio =findViewById(R.id.tvPrecioPedido);

        setSupportActionBar(tbPedido);
        getSupportActionBar().setTitle(comercio+" "+producto);

        cargarDatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pedidos,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miConfirmado) {
            borrado=false;
            database = FirebaseDatabase.getInstance().getReference().child("usuarios");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!borrado&&!pedidoborrado){
                        if (id == R.id.miConfirmado){
                            for (DataSnapshot child:snapshot.getChildren()) {
                                if(child.getKey().equals(ActivityPedidos.nombreusuario)) {
                                    saldo = Double.parseDouble(child.child("tarjeta").child("saldo").getValue().toString());
                                }
                            }
                            if (p.isConfirmado()==false) {
                                if (saldo >= p.getSubtotal()) {
                                    if (Integer.parseInt(stock) >= p.getCantidad()) {
                                        borrado = false;
                                        database = FirebaseDatabase.getInstance().getReference().child("pedidos");
                                        database.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!borrado&&!pedidoborrado) {
                                                    for (DataSnapshot child : snapshot.getChildren()) {
                                                        if(child.getValue(Pedido.class)!=null) {
                                                            if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                                                                p1 = child.getValue(Pedido.class);
                                                                p1.setConfirmado(true);
                                                                p1.setEntregado(false);
                                                                p1.setEnviado(false);
                                                                p1.setPendiente(false);
                                                                p1.setRechazado(false);
                                                                database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                                borrado = true;
                                                                database.removeValue();
                                                                database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                                database.setValue(p1);
                                                            }
                                                        }else{
                                                            pedidoborrado=true;
                                                            finish();
                                                        }
                                                    }
                                                    borrado = false;
                                                    database = FirebaseDatabase.getInstance().getReference().child("productos");
                                                    database.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (!borrado&&!pedidoborrado) {
                                                                if (Integer.parseInt(stock) >= p.getCantidad()) {
                                                                    for (DataSnapshot child : snapshot.getChildren()) {
                                                                        if (child.child("nombre").getValue().toString().equals(p1.getIdProd())) {
                                                                            Producto newproducto = child.getValue(Producto.class);
                                                                            newproducto.setStock(Integer.parseInt(stock) - p.getCantidad());
                                                                            database = FirebaseDatabase.getInstance().getReference().child("productos").child(child.getKey());
                                                                            borrado = true;
                                                                            database.removeValue();
                                                                            database = FirebaseDatabase.getInstance().getReference().child("productos").child(child.getKey());
                                                                            database.setValue(newproducto);
                                                                        }
                                                                    }
                                                                    borrado = false;
                                                                    database = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                                                    database.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if (!borrado&&!pedidoborrado) {
                                                                                for (DataSnapshot child : snapshot.getChildren()) {
                                                                                    if (child.getKey().equals(p1.getIdUser())) {
                                                                                        Usuario newusuario = child.getValue(Usuario.class);
                                                                                        newusuario.setTarjeta(new Tarjeta(saldo - p.getSubtotal()));
                                                                                        database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(child.getKey());
                                                                                        borrado = true;
                                                                                        database.removeValue();
                                                                                        database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(child.getKey());
                                                                                        database.setValue(newusuario);
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
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "El pedido no puede ser confirmado por falta de stock, entonces se rechazará", Toast.LENGTH_LONG).show();
                                        borrado = false;
                                        database = FirebaseDatabase.getInstance().getReference().child("pedidos");
                                        database.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!borrado&&!pedidoborrado) {
                                                    for (DataSnapshot child : snapshot.getChildren()) {
                                                        if(child.getValue(Pedido.class)!=null){
                                                            if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                                                                p1 = child.getValue(Pedido.class);
                                                                p1.setConfirmado(false);
                                                                p1.setEntregado(false);
                                                                p1.setEnviado(false);
                                                                p1.setPendiente(false);
                                                                p1.setRechazado(true);
                                                                database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                                borrado = true;
                                                                database.removeValue();
                                                                database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                                database.setValue(p1);
                                                            }
                                                        }else{
                                                            pedidoborrado=true;
                                                            finish();
                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled (@NonNull DatabaseError error){

                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "El pedido no puede ser confirmado por falta de saldo, entonces se rechazará", Toast.LENGTH_LONG).show();
                                    borrado = false;
                                    database = FirebaseDatabase.getInstance().getReference().child("pedidos");
                                    database.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!borrado&&!pedidoborrado) {
                                                for (DataSnapshot child : snapshot.getChildren()) {
                                                    if(child.getValue(Pedido.class)!=null) {
                                                        if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                                                            p1 = child.getValue(Pedido.class);
                                                            p1.setConfirmado(false);
                                                            p1.setEntregado(false);
                                                            p1.setEnviado(false);
                                                            p1.setPendiente(false);
                                                            p1.setRechazado(true);
                                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                            borrado = true;
                                                            database.removeValue();
                                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                                            database.setValue(p1);
                                                        }
                                                    }else{
                                                        pedidoborrado=true;
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled (@NonNull DatabaseError error){

                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "El pedido ya esta confirmado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else if (id == R.id.miEntregado) {
            borrado = false;
            database = FirebaseDatabase.getInstance().getReference().child("pedidos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!borrado&&!pedidoborrado) {
                        if (id == R.id.miEntregado) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if(child.getValue(Pedido.class)!=null) {
                                    if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                                        if (child.child("enviado").getValue().toString().equals("true")) {
                                            p1 = child.getValue(Pedido.class);
                                            p1.setConfirmado(false);
                                            p1.setEntregado(true);
                                            p1.setEnviado(false);
                                            p1.setPendiente(false);
                                            p1.setRechazado(false);
                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                            borrado = true;
                                            database.removeValue();
                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                            database.setValue(p1);
                                            Toast.makeText(getApplicationContext(), "El pedido ha sido entregado", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "El pedido ha de estar enviado antes", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }else{
                                    pedidoborrado=true;
                                    finish();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (id == R.id.miEnviado) {
            borrado = false;
            database = FirebaseDatabase.getInstance().getReference().child("pedidos");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!borrado&&!pedidoborrado) {
                        if (id == R.id.miEnviado) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if(child.getValue(Pedido.class)!=null) {
                                    if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                                        if (child.child("confirmado").getValue().toString().equals("true")) {
                                            p1 = child.getValue(Pedido.class);
                                            p1.setConfirmado(false);
                                            p1.setEntregado(false);
                                            p1.setEnviado(true);
                                            p1.setPendiente(false);
                                            p1.setRechazado(false);
                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                            borrado = true;
                                            database.removeValue();
                                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                                            database.setValue(p1);
                                            Toast.makeText(getApplicationContext(), "El pedido ha sido enviado", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "El pedido ha de estar confirmado antes", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }else{
                                    pedidoborrado=true;
                                    finish();
                                }
                            }
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

    private void cargarDatos() {

        database = FirebaseDatabase.getInstance().getReference().child("pedidos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue(Pedido.class) != null) {
                        if (child.child("idCom").getValue().toString().equals(comercio) && child.child("idProd").getValue().toString().equals(producto) && child.child("idUser").getValue().toString().equals(ActivityPedidos.nombreusuario)) {
                            p = child.getValue(Pedido.class);
                            tvcantidad.setText("Cantidad : " + p.getCantidad());
                            tvcomercio.setText(p.getIdCom());
                            tvproducto.setText(p.getIdProd());

                            if (p.isPendiente()) {

                                tvestado.setText("Pedido pendiente de confirmación");
                                tvestado.setTextColor(Color.GRAY);

                            }

                            if (p.isConfirmado()) {

                                tvestado.setText("Pedido confirmado");
                                tvestado.setTextColor(Color.GREEN);

                            }

                            if (p.isRechazado()) {

                                tvestado.setText("Pedido rechazado");
                                tvestado.setTextColor(Color.RED);

                            }

                            if (p.isEntregado()) {

                                tvestado.setText("Pedido entregado");
                                tvestado.setTextColor(Color.MAGENTA);

                            }

                            if (p.isEnviado()) {

                                tvestado.setText("Pedido enviado");
                                tvestado.setTextColor(Color.BLUE);

                            }

                            database = FirebaseDatabase.getInstance().getReference().child("productos");
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        if (!pedidoborrado) {
                                            if (child.child("nombre").getValue().toString().equals(p.getIdProd())) {
                                                Glide.with(getApplicationContext()).load(child.child("imageUri").getValue().toString()).into(ivPedido);
                                                precio = child.child("precio").getValue().toString();
                                                tvprecio.setText("Precio : " + child.child("precio").getValue().toString());
                                                stock = child.child("stock").getValue().toString();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }else{
                        pedidoborrado=true;
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
