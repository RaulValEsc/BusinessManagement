package com.example.businessmanagement.vistas.Trabajador;

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
import com.example.businessmanagement.controladores.bdLocal.SQLTrabajadoresController;
import com.example.businessmanagement.modelos.Trabajador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CrearTrabajador extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    private EditText nombreTrabajador, dniTrabajador, emailTrabajador, telefonoTrabajador;
    private Button crear;
    private boolean trabajadorexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;
    private ImageView ivTrabajador;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_trabajador);

        crear = findViewById(R.id.bCrearTrabajador);

        nombreTrabajador = findViewById(R.id.etnombreTrabajador);
        dniTrabajador = findViewById(R.id.dniTrabajador);
        emailTrabajador = findViewById(R.id.emailTrabajador);
        telefonoTrabajador = findViewById(R.id.telefonoTrabajador);

        ivTrabajador = findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance().getReference();

        setup();
    }

    private void setup() {
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreTrabajador.getText().toString().isEmpty()||dniTrabajador.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El nombre y dni son campos obligatorios", Toast.LENGTH_LONG).show();
                }else {
                    if(isNetworkAvailable()) {
                        database.child("Trabajadores").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (imageUri != null) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (dniTrabajador.getText().toString().equals(child.child("dni").getValue().toString())) {
                                            trabajadorexiste = true;
                                            break;
                                        }
                                    }
                                    if (trabajadorexiste == false) {
                                        crearTrabajador();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Este trabajador ya está registrado", Toast.LENGTH_LONG).show();
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
                        SQLTrabajadoresController sql = new SQLTrabajadoresController(getApplicationContext());
                        crearTrabajador();
                    }
                }
            }
        });

        ivTrabajador.setOnClickListener(new View.OnClickListener() {
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

            StorageReference fileReference = storage.getReference("imagenes_trabajadores").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivTrabajador);

    }

    private void crearTrabajador() {
        SQLTrabajadoresController sql = new SQLTrabajadoresController(getApplicationContext());
        if(isNetworkAvailable()) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (imageUri != null) {
                        Trabajador c = new Trabajador(nombreTrabajador.getText().toString(), dniTrabajador.getText().toString(), telefonoTrabajador.getText().toString(), emailTrabajador.getText().toString(), imageUri, postStorage);
                        database.child("Trabajadores").child(c.getDni()).setValue(c);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Trabajador a = new Trabajador(nombreTrabajador.getText().toString(), dniTrabajador.getText().toString(), telefonoTrabajador.getText().toString(), emailTrabajador.getText().toString(), "", "");
            sql.anadirTrabajadorAux(a);
        }

        long check = sql.anadirTrabajador(new Trabajador(nombreTrabajador.getText().toString(), dniTrabajador.getText().toString(), telefonoTrabajador.getText().toString(), emailTrabajador.getText().toString(), "", ""));

        if(check == -1){
            Toast.makeText(getApplicationContext(), "Este trabajador ya está registrado", Toast.LENGTH_LONG).show();
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
