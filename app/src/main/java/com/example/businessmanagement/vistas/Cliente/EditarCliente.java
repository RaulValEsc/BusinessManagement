package com.example.businessmanagement.vistas.Cliente;

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
import com.example.businessmanagement.modelos.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditarCliente extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivCliente;
    EditText etNombre, etDni, etEmail, etTelefono;
    Button bEditar;

    boolean clienteexiste = false, editado = false;

    Cliente c, newCliente;

    String dni,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cliente);

        editado= false;
        aux = false;

        storage = FirebaseStorage.getInstance();

        Bundle b = getIntent().getExtras();

        ivCliente = findViewById(R.id.ivCliente);

        etDni = findViewById(R.id.tvEditarDNI);
        etNombre = findViewById(R.id.tvEditarNombre);
        etEmail = findViewById(R.id.tvEditarEmail);
        etTelefono = findViewById(R.id.tvEditarTelefono);

        bEditar = findViewById(R.id.bModificar);

        dni = b.getString("dni");

        cargarComercio();

        setup();
    }

    private void cargarComercio() {
        database = FirebaseDatabase.getInstance().getReference().child("Clientes");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!aux) {
                    if (editado != true) {
                        c = snapshot.child(dni).getValue(Cliente.class);

                        Glide.with(getApplicationContext()).load(c.getImageUri()).into(ivCliente);
                        etNombre.setText(c.getNombre());
                        etDni.setText(c.getDni());
                        etTelefono.setText(c.getTelefono());
                        etEmail.setText(c.getEmail());
                    } else {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (etDni.getText().toString().equals(child.child("dni").getValue().toString())) {
                                clienteexiste = true;
                            }
                        }
                        if (clienteexiste == false) {
                            database = FirebaseDatabase.getInstance().getReference().child("Clientes").child(newCliente.getDni());
                            database.setValue(newCliente);
                            VistaCliente.dni = newCliente.getNombre();
                            Toast.makeText(getApplicationContext(), "Cliente Modificado Correctamente", Toast.LENGTH_LONG).show();
                            VistaCliente.tvDni.setText(newCliente.getDni());
                            VistaCliente.tvNombre.setText(newCliente.getNombre());
                            VistaCliente.tvTelefono.setText(newCliente.getTelefono());
                            VistaCliente.tvEmail.setText(newCliente.getEmail());
                            finish();
                        } else {
                            etNombre.setText("");
                            etDni.setText("");
                            etEmail.setText("");
                            etTelefono.setText("");
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
                if(etDni.getText().toString().isEmpty()||etNombre.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Rellene el dni y nombre", Toast.LENGTH_LONG).show();
                }else {
                    newCliente = new Cliente(etNombre.getText().toString(), etDni.getText().toString(), etEmail.getText().toString(), etTelefono.getText().toString(), Uri.parse(c.getImageUri()), Uri.parse(c.getImgStorage()));
                    database = FirebaseDatabase.getInstance().getReference().child("Clientes").child(dni);
                    editado = true;
                    database.removeValue();
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

            StorageReference fileReference = storage.getReference("imagenes_clientes").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivCliente);

    }
}
