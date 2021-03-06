package com.example.businessmanagement.vistas.Cliente;

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
import com.example.businessmanagement.controladores.bdLocal.SQLClientesController;
import com.example.businessmanagement.modelos.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class CrearCliente extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    private EditText nombreCliente, dniCliente, emailCliente, telefonoCliente;
    private Button crear;
    private boolean clienteexiste = false;
    DatabaseReference database;
    Uri imageUri, postStorage;
    String imgStorage;
    private ImageView ivCliente;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        crear = findViewById(R.id.bCrearCliente);

        nombreCliente = findViewById(R.id.etnombreCliente);
        dniCliente = findViewById(R.id.dniCliente);
        emailCliente = findViewById(R.id.emailCliente);
        telefonoCliente = findViewById(R.id.telefonoCliente);

        ivCliente = findViewById(R.id.imageView);

        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance().getReference();

        setup();
    }

    private void setup() {
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombreCliente.getText().toString().isEmpty()||dniCliente.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El nombre y dni son campos obligatorios", Toast.LENGTH_LONG).show();
                }else {
                    if(isNetworkAvailable()) {
                        database.child("Clientes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (imageUri != null) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (dniCliente.getText().toString().equals(child.child("dni").getValue().toString())) {
                                            clienteexiste = true;
                                            break;
                                        }
                                    }
                                    if (clienteexiste == false) {
                                        crearCliente();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Este cliente ya est?? registrado", Toast.LENGTH_LONG).show();
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
                        SQLClientesController sql = new SQLClientesController(getApplicationContext());
                        crearCliente();
                    }
                }
            }
        });

        ivCliente.setOnClickListener(new View.OnClickListener() {
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

            StorageReference fileReference = storage.getReference("imagenes_clientes").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivCliente);

    }

    private void crearCliente() {
        SQLClientesController sql = new SQLClientesController(getApplicationContext());
        if(isNetworkAvailable()){
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (imageUri != null) {
                        Cliente c = new Cliente(nombreCliente.getText().toString(), dniCliente.getText().toString(), telefonoCliente.getText().toString(), emailCliente.getText().toString(), imageUri, postStorage);
                        database.child("Clientes").child(c.getDni()).setValue(c);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Cliente a = new Cliente(nombreCliente.getText().toString(), dniCliente.getText().toString(), telefonoCliente.getText().toString(), emailCliente.getText().toString(), "", "");
            sql.anadirClienteAux(a);
        }

        long check = sql.anadirCliente(new Cliente(nombreCliente.getText().toString(), dniCliente.getText().toString(), telefonoCliente.getText().toString(), emailCliente.getText().toString(), "", ""));

            if(check == -1){
            Toast.makeText(getApplicationContext(), "Este cliente ya est?? registrado", Toast.LENGTH_LONG).show();
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
