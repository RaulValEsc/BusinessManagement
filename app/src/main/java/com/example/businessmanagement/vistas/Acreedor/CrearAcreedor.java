package com.example.businessmanagement.vistas.Acreedor;

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
import com.example.businessmanagement.controladores.bdLocal.SQLAcreedoresController;
import com.example.businessmanagement.modelos.Acreedor;
import com.example.businessmanagement.modelos.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CrearAcreedor extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    private EditText nombreAcreedor, nifAcreedor, emailAcreedor, telefonoAcreedor;
    private Button crear;
    private boolean acreedorexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;
    private ImageView ivAcreedor;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_acreedor);

        crear = findViewById(R.id.bCrearAcreedor);

        nombreAcreedor = findViewById(R.id.etnombreAcreedor);
        nifAcreedor = findViewById(R.id.nifAcreedor);
        emailAcreedor = findViewById(R.id.emailAcreedor);
        telefonoAcreedor = findViewById(R.id.telefonoAcreedor);

        ivAcreedor = findViewById(R.id.imageView);

        if(isNetworkAvailable()){
            database = FirebaseDatabase.getInstance().getReference();
        }

        setup();
    }

    private void setup() {
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreAcreedor.getText().toString().isEmpty()||nifAcreedor.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El nombre y nif son campos obligatorios", Toast.LENGTH_LONG).show();
                }else {
                    if(isNetworkAvailable()) {
                        database.child("Acreedores").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (imageUri != null) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (nifAcreedor.getText().toString().equals(child.child("nif").getValue().toString())) {
                                            acreedorexiste = true;
                                            break;
                                        }
                                    }
                                    if (acreedorexiste == false) {
                                        crearAcreedor();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Este acreedor ya está registrado", Toast.LENGTH_LONG).show();
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
                        SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
                        crearAcreedor();

                    }
                }
            }
        });

        ivAcreedor.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(getApplicationContext(), "Sin conexión a internet no se puede establecer una foto", Toast.LENGTH_LONG).show();
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

            storage = FirebaseStorage.getInstance();
            StorageReference fileReference = storage.getReference("imagenes_acreedores").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivAcreedor);

    }

    private void crearAcreedor() {
        SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
        if(isNetworkAvailable()){
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (imageUri != null) {
                        Acreedor a = new Acreedor(nombreAcreedor.getText().toString(), nifAcreedor.getText().toString(), telefonoAcreedor.getText().toString(), emailAcreedor.getText().toString(), imageUri, postStorage);
                        database.child("Acreedores").child(a.getNif()).setValue(a);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Acreedor a = new Acreedor(nombreAcreedor.getText().toString(), nifAcreedor.getText().toString(), telefonoAcreedor.getText().toString(), emailAcreedor.getText().toString(), "", "");
            sql.anadirAcreedorAux(a);
        }

        long check = sql.anadirAcreedor(new Acreedor(nombreAcreedor.getText().toString(), nifAcreedor.getText().toString(), telefonoAcreedor.getText().toString(), emailAcreedor.getText().toString(), "", ""));

        if(check == -1){
            Toast.makeText(getApplicationContext(), "Este acreedor ya está registrado", Toast.LENGTH_LONG).show();
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
