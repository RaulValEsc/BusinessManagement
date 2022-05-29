package com.example.businessmanagement.vistas.Comercio;

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
import com.example.businessmanagement.modelos.Comercio;
import com.example.businessmanagement.modelos.Pedido;
import com.example.businessmanagement.modelos.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class EditarComercio extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivComercio;
    EditText etNombre, etDescripcion;
    Button bEditar;

    Producto p,newProducto;

    Pedido pedido,newPedido;

    boolean comercioexiste = false, borrado = false,borrarProductos=false;

    Comercio c, newComercio;

    ArrayList<Producto> arrayProductos;

    String nombreanterior,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comercio);

        borrado= false;
        aux = false;
        borrarProductos=false;

        arrayProductos = new ArrayList<Producto>();

        storage = FirebaseStorage.getInstance();

        Bundle b = getIntent().getExtras();

        ivComercio = findViewById(R.id.ivComercio);
        etDescripcion = findViewById(R.id.tvEditarDescripcion);
        etNombre = findViewById(R.id.tvEditarNombre);
        bEditar = findViewById(R.id.bModificar);

        nombreanterior = b.getString("nombre");

        cargarComercio();

        setup();
    }

    private void cargarComercio() {
        database = FirebaseDatabase.getInstance().getReference().child("comercios");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!aux) {
                    if (borrado != true) {
                        c = snapshot.child(nombreanterior).getValue(Comercio.class);

                        Glide.with(getApplicationContext()).load(c.getImageUri()).into(ivComercio);
                        etNombre.setText(c.getNombre());
                        etDescripcion.setText(c.getDescripcion());
                    } else {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (etNombre.getText().toString().equals(child.child("nombre").getValue().toString())) {
                                comercioexiste = true;
                            }
                        }
                        if (comercioexiste == false) {
                            database = FirebaseDatabase.getInstance().getReference().child("comercios").child(newComercio.getNombre());
                            database.setValue(newComercio);
                            VistaComercio.nombrecomercio = newComercio.getNombre();
                            Toast.makeText(getApplicationContext(), "Comercio Modificado Correctamente", Toast.LENGTH_LONG).show();
                            VistaComercio.tvDescripcion.setText(newComercio.getDescripcion());
                            VistaComercio.tvNombre.setText(newComercio.getNombre());
                            finish();
                        } else {
                            etNombre.setText("");
                            etDescripcion.setText("");
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
                if(etDescripcion.getText().toString().equals("")||etNombre.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Rellene los campos vacios", Toast.LENGTH_LONG).show();
                }else {
                    if(imageUri==null) {
                        newComercio = new Comercio(etNombre.getText().toString(), etDescripcion.getText().toString(), Uri.parse(c.getImageUri()), Uri.parse(c.getImgStorage()));
                        database = FirebaseDatabase.getInstance().getReference().child("comercios").child(nombreanterior);
                        borrado = true;
                        database.removeValue();
                        editarProductos();


                    }else{
                        newComercio = new Comercio(etNombre.getText().toString(), etDescripcion.getText().toString(), imageUri, postStorage);
                        database = FirebaseDatabase.getInstance().getReference().child("comercios").child(nombreanterior);
                        borrado = true;
                        database.removeValue();
                        editarProductos();
                    }
                }
            }
        });

        ivComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });
    }

    private void editarProductos() {

        database = FirebaseDatabase.getInstance().getReference().child("productos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!borrarProductos) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.child("idComercio").getValue().toString().equals(nombreanterior)) {
                            p = child.getValue(Producto.class);
                            newProducto = new Producto(p.getNombre(),p.getPrecio(),p.getStock(),newComercio.getNombre(), Uri.parse(p.getImageUri()), Uri.parse(p.getImgStorage()));
                            arrayProductos.add(newProducto);
                            database= FirebaseDatabase.getInstance().getReference();
                            borrarProductos=true;
                            database.child("productos").child(p.getNombre()+"_"+nombreanterior).removeValue();
                            database.child("productos").child(newProducto.getNombre()+"_"+newComercio.getNombre()).setValue(newProducto);
                        }
                    }
                    database = FirebaseDatabase.getInstance().getReference().child("pedidos");
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    for (int i = 0; i < arrayProductos.size(); i++) {
                                        if (child.child("idProd").getValue().toString().equals(arrayProductos.get(i).getNombre()) && child.child("idCom").getValue().toString().equals(nombreanterior)) {
                                            pedido = child.getValue(Pedido.class);
                                            pedido.setIdCom(newComercio.getNombre());
                                            database= FirebaseDatabase.getInstance().getReference();
                                            database.child("pedidos").child(nombreanterior+"_"+pedido.getIdUser()+"_"+pedido.getIdProd()).removeValue();
                                            database.child("pedidos").child(newComercio.getNombre()+"_"+pedido.getIdUser()+"_"+pedido.getIdProd()).setValue(pedido);
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivComercio);

    }
}