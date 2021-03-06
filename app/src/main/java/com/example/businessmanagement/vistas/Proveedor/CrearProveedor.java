package com.example.businessmanagement.vistas.Proveedor;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.businessmanagement.controladores.bdLocal.SQLProveedoresController;
import com.example.businessmanagement.modelos.Proveedor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CrearProveedor extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    private EditText nombreProveedor, nifProveedor, emailProveedor, telefonoProveedor;
    private Button crear;
    private boolean proveedorexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;
    private ImageView ivProveedor;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_proveedor);

        crear = findViewById(R.id.bCrearProveedor);

        nombreProveedor = findViewById(R.id.etnombreProveedor);
        nifProveedor = findViewById(R.id.nifProveedor);
        emailProveedor = findViewById(R.id.emailProveedor);
        telefonoProveedor = findViewById(R.id.telefonoProveedor);

        ivProveedor = findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance().getReference();

        setup();
    }

    private void setup() {
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreProveedor.getText().toString().isEmpty()||nifProveedor.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El nombre y nif son campos obligatorios", Toast.LENGTH_LONG).show();
                }else {
                    if(isNetworkAvailable()) {
                        database.child("Proveedores").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (imageUri != null) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (nifProveedor.getText().toString().equals(child.child("nif").getValue().toString())) {
                                            proveedorexiste = true;
                                            break;
                                        }
                                    }
                                    if (proveedorexiste == false) {
                                        crearProveedor();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Este proveedor ya est?? registrado", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Tienes que introducir una imagen", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    }else{
                        SQLProveedoresController sql = new SQLProveedoresController(getApplicationContext());
                        crearProveedor();
                    }
                }
            }
        });

        ivProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escogerFoto();
            }
        });
    }

    private void escogerFoto() {

        if(isNetworkAvailable()){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

            try {

                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            } catch (ActivityNotFoundException e) {
            }
        }else {
            Toast.makeText(getApplicationContext(), "Sin conexi??n a internet no se puede establecer una foto", Toast.LENGTH_LONG).show();
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

            StorageReference fileReference = storage.getReference("imagenes_proveedores").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivProveedor);

    }

    private void crearProveedor() {
        SQLProveedoresController sql = new SQLProveedoresController(getApplicationContext());
        if(isNetworkAvailable()) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (imageUri != null) {
                        Proveedor a = new Proveedor(nombreProveedor.getText().toString(), nifProveedor.getText().toString(), telefonoProveedor.getText().toString(), emailProveedor.getText().toString(), imageUri, postStorage);
                        database.child("Proveedores").child(a.getNif()).setValue(a);
                    } else {
                        Proveedor a = new Proveedor(nombreProveedor.getText().toString(), nifProveedor.getText().toString(), telefonoProveedor.getText().toString(), emailProveedor.getText().toString(), "", "");
                        database.child("Proveedores").child(a.getNif()).setValue(a);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Proveedor a = new Proveedor(nombreProveedor.getText().toString(), nifProveedor.getText().toString(), telefonoProveedor.getText().toString(), emailProveedor.getText().toString(), "", "");
            sql.anadirProveedorAux(a);
        }

        long check = sql.anadirProveedor(new Proveedor(nombreProveedor.getText().toString(), nifProveedor.getText().toString(), telefonoProveedor.getText().toString(), emailProveedor.getText().toString(), "", ""));

        if(check == -1){
            Toast.makeText(getApplicationContext(), "Este proveedor ya est?? registrado", Toast.LENGTH_LONG).show();
        }

        onBackPressed();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

