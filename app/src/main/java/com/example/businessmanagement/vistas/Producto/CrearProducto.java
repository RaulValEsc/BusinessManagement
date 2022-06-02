package com.example.businessmanagement.vistas.Producto;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.businessmanagement.R;
import com.example.businessmanagement.modelos.Producto;
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

    private EditText nombreProducto, codigoProducto, precioProducto;
    private Button crear;
    private boolean productoexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;

    String codigoProveedor;

    private ImageView ivProducto;

    Spinner pickerProveedores;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);

        crear = findViewById(R.id.bCrearProducto);

        nombreProducto = findViewById(R.id.etnombreProducto);
        codigoProducto = findViewById(R.id.codigoProducto);
        precioProducto = findViewById(R.id.precioProducto);

        pickerProveedores = findViewById(R.id.pickProveedor);

        ivProducto = findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance().getReference();

        setup();
    }

    private void setup() {
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(nombreProducto.getText().toString().isEmpty()||codigoProducto.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "El nombre y codigo son campos obligatorios", Toast.LENGTH_LONG).show();
                        }else{
                            if (imageUri != null) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    if (codigoProducto.getText().toString().equals(child.child("codigo").getValue().toString())) {
                                        productoexiste = true;
                                        break;
                                    }
                                }
                                if (productoexiste == false) {
                                    crearComercio();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Este producto ya está registrado", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Tienes que introducir una imagen", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        ivProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });

        pickerProveedores.setOnItemClickListener((parent, view, position, id) -> {

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

            if (postStorage == null) {
                postStorage = Uri.EMPTY;
            }

            Uri uri = data.getData();

            imgStorage = uri.toString();

            StorageReference fileReference = storage.getReference("imagenes_productos").child(uri.getLastPathSegment());
            fileReference.putFile(uri).continueWithTask(task -> {

                if (!task.isSuccessful()) {

                    throw Objects.requireNonNull(task.getException());

                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    imageUri = Objects.requireNonNull(task.getResult());
                    putImage(imageUri);

                }

            });

            imageUri = data.getData();
            postStorage = imageUri;

        } else {

            Toast.makeText(getApplicationContext(), "Imagen no seleccionada", Toast.LENGTH_LONG).show();

        }

    }

    private void putImage(Uri imageUri) {

        Glide.with(getApplicationContext()).load(imageUri).into(ivProducto);

    }

    private void crearComercio() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (imageUri != null) {
                    Producto c = new Producto(nombreProducto.getText().toString(), codigoProducto.getText().toString(), telefonoProducto.getText().toString(), precioProducto.getText().toString(), imageUri, postStorage);
                    database.child("Productos").child(c.getCodigo()).setValue(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onBackPressed();
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

