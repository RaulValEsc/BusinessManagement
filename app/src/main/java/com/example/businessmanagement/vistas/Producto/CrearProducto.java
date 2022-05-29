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

public class CrearProducto extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    private EditText nombreProducto, stockProducto, precioProducto;
    private Button bCrearProducto;
    private boolean productoexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;
    private ImageView ivComercio;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        storage = FirebaseStorage.getInstance();

        bCrearProducto = findViewById(R.id.bCrearProducto);
        nombreProducto = findViewById(R.id.etnombreProducto);
        stockProducto = findViewById(R.id.etStockProducto);
        precioProducto = findViewById(R.id.etPrecioProducto);
        ivComercio = findViewById(R.id.imageView);

        database = FirebaseDatabase.getInstance().getReference();

        setup();
    }

    private void setup() {
        bCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("productos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (imageUri!=null) {
                            if (!VistaComercio.borrarproductos) {
                                if (nombreProducto.getText().toString().isEmpty() || stockProducto.getText().toString().isEmpty() || precioProducto.getText().toString().isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();
                                } else {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        String aux = nombreProducto + "_" + VistaComercio.nombrecomercio;
                                        if (aux.equals(child.getValue().toString())) {
                                            productoexiste = true;
                                            break;
                                        }
                                    }
                                    if (productoexiste == false) {
                                        crearProducto();
                                        VistaComercio.borrarproductos = true;
                                    } else {
                                        nombreProducto.setText("");
                                        stockProducto.setText("");
                                        precioProducto.setText("");
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
        });

        ivComercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });
    }

    private void escogerFoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        try {

            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

        } catch(ActivityNotFoundException e){
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

    private void crearProducto(){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (imageUri!=null) {
                    Producto p = new Producto(nombreProducto.getText().toString(), Double.parseDouble(precioProducto.getText().toString()), Integer.parseInt(stockProducto.getText().toString()), VistaComercio.nombrecomercio, imageUri, postStorage);
                    database.child("productos").child(p.getNombre() + "_" + p.getIdComercio()).setValue(p);
                    finish();
                    VistaComercio.borrarproductos = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onBackPressed();
    }
}
