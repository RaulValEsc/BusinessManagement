package com.example.businessmanagement.vistas.Producto;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Pedido;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.vistas.Comercio.VistaComercio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditarProducto extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivProducto;
    EditText etNombre, etPrecio, etStock;
    Button bEditar,bQuitarFoto;

    boolean comercioexiste = false, borrado = false, quitarfoto= false,borrarPedido=false;

    Producto p, newProducto;

    String nombreanterior,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        borrado= false;
        aux= false;
        quitarfoto= false;
        borrarPedido=false;

        storage = FirebaseStorage.getInstance();

        Bundle b = getIntent().getExtras();

        bQuitarFoto = findViewById(R.id.bQuitarFoto);
        ivProducto = findViewById(R.id.ivProductoEdit);
        etPrecio = findViewById(R.id.tvEditarPrecio);
        etNombre = findViewById(R.id.tvEditarNombreProduc);
        etStock = findViewById(R.id.tvEditarStock);
        bEditar = findViewById(R.id.bModificarProduc);

        nombreanterior = b.getString("nombre");

        cargarComercio();

        setup();
    }

    private void cargarComercio() {
        database = FirebaseDatabase.getInstance().getReference().child("productos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!borrarPedido) {
                    if (!aux) {
                        if (borrado != true) {
                            p = snapshot.child(nombreanterior + "_" + VistaComercio.nombrecomercio).getValue(Producto.class);

                            Glide.with(getApplicationContext()).load(p.getImageUri()).into(ivProducto);
                            etNombre.setText(p.getNombre());
                            etPrecio.setText("" + p.getPrecio());
                            etStock.setText("" + p.getStock());
                        } else {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (p.getNombre().equals(child.child("nombre").getValue().toString())) {
                                    comercioexiste = true;
                                }
                            }
                            if (comercioexiste == false) {
                                database = FirebaseDatabase.getInstance().getReference().child("productos").child(newProducto.getNombre() + "_" + VistaComercio.nombrecomercio);
                                database.setValue(newProducto);
                                Toast.makeText(getApplicationContext(), "Comercio Modificado Correctamente", Toast.LENGTH_LONG).show();
                                if (!aux) {
                                    Glide.with(getApplicationContext()).load(newProducto.getImageUri()).into(VistaProducto.ivProducto);
                                    VistaProducto.nombreproducto = newProducto.getNombre();
                                    VistaProducto.tvPrecio.setText("" + newProducto.getPrecio());
                                    VistaProducto.tvStock.setText("" + newProducto.getStock());
                                    VistaProducto.tvNombre.setText(newProducto.getNombre());
                                    onBackPressed();
                                }
                            } else {
                                etNombre.setText("");
                                etPrecio.setText("");
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

    private void setup() {
        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPrecio.getText().toString().equals("")||etNombre.getText().toString().equals("")||etStock.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Rellene los campos vacios", Toast.LENGTH_LONG).show();
                }else {
                    if(quitarfoto==true){

                        newProducto = new Producto(etNombre.getText().toString(), Double.parseDouble(etPrecio.getText().toString()), Integer.parseInt(etStock.getText().toString()),VistaComercio.nombrecomercio, Uri.parse("https://firebasestorage.googleapis.com/v0/b/practicacomercios.appspot.com/o/imagenes_usuarios%2F58119?alt=media&token=85da76f2-d008-4a4e-a40e-7644d3882f2a"), Uri.parse("content://media/external/images/media/58119"));
                        database = FirebaseDatabase.getInstance().getReference().child("productos").child(nombreanterior+"_"+VistaComercio.nombrecomercio);
                        borrado = true;
                        database.removeValue();
                        editarPedido();

                    }else{

                        if(imageUri==null){
                            newProducto = new Producto(etNombre.getText().toString(), Double.parseDouble(etPrecio.getText().toString()), Integer.parseInt(etStock.getText().toString()),VistaComercio.nombrecomercio, Uri.parse(p.getImageUri()), Uri.parse(p.getImgStorage()));
                            database = FirebaseDatabase.getInstance().getReference().child("productos").child(nombreanterior+"_"+VistaComercio.nombrecomercio);
                            borrado = true;
                            database.removeValue();
                            editarPedido();
                        } else {
                            newProducto = new Producto(etNombre.getText().toString(), Double.parseDouble(etPrecio.getText().toString()), Integer.parseInt(etStock.getText().toString()), VistaComercio.nombrecomercio, imageUri, postStorage);
                            database = FirebaseDatabase.getInstance().getReference().child("productos").child(nombreanterior + "_" + VistaComercio.nombrecomercio);
                            borrado = true;
                            database.removeValue();
                            editarPedido();
                        }

                    }
                }
            }
        });

        bQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quitarfoto==true){
                    quitarfoto=false;
                    Toast.makeText(getApplicationContext(), "Se mantendrá la foto actual", Toast.LENGTH_SHORT).show();

                    Glide.with(getApplicationContext()).load(p.getImageUri()).into(ivProducto);
                }else{
                    quitarfoto=true;
                    Toast.makeText(getApplicationContext(), "Se eliminará la foto de perfil", Toast.LENGTH_SHORT).show();

                    Glide.with(getApplicationContext()).load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/practicacomercios.appspot.com/o/imagenes_usuarios%2F58119?alt=media&token=85da76f2-d008-4a4e-a40e-7644d3882f2a")).into(ivProducto);
                }
            }
        });

        ivProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });
    }

    private void editarPedido() {
        database = FirebaseDatabase.getInstance().getReference().child("pedidos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!borrarPedido) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.child("idProd").getValue().toString().equals(nombreanterior)) {
                            Pedido newpedido = child.getValue(Pedido.class);
                            newpedido.setIdProd(newProducto.getNombre());
                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(child.getKey());
                            database.removeValue();
                            database = FirebaseDatabase.getInstance().getReference().child("pedidos").child(newpedido.getIdCom()+"_"+newpedido.getIdUser()+"_"+newpedido.getIdProd());
                            database.setValue(newpedido);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void escogerFoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        try {

            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        } catch (ActivityNotFoundException e) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            assert data != null;

            if (postStorage == null){
                postStorage = Uri.EMPTY;
            }

            Uri uri = data.getData();

            imgStorage = uri.toString();

            StorageReference fileReference = storage.getReference("imagenes_comercios").child(uri.getLastPathSegment());
            fileReference.putFile(uri).continueWithTask(task -> {

                if (!task.isSuccessful()) {

                    throw Objects.requireNonNull(task.getException());

                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {

                if (task.isSuccessful()){

                    imageUri = Objects.requireNonNull(task.getResult());
                    putImage(imageUri);

                }

            });

            imageUri = data.getData();
            postStorage = imageUri;

        } else {

            Toast.makeText(getApplicationContext(),"Imagen no seleccionada", Toast.LENGTH_LONG).show();

        }

    }

    private void putImage(Uri imageUri) {

        Glide.with(getApplicationContext()).load(imageUri).into(ivProducto);

    }
}