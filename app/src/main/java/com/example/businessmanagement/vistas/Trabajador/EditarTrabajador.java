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
import com.example.businessmanagement.controladores.bdLocal.SQLAcreedoresController;
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

public class EditarTrabajador extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivTrabajador;
    EditText etNombre, etDni, etEmail, etTelefono;
    Button bEditar;

    boolean trabajadorexiste = false, editado = false;

    Trabajador c, newTrabajador;

    String dni,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_trabajador);

        editado= false;
        aux = false;

        if(isNetworkAvailable()){
            storage = FirebaseStorage.getInstance();
        }

        Bundle b = getIntent().getExtras();

        ivTrabajador = findViewById(R.id.ivTrabajador);

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
        if(isNetworkAvailable()) {
            database = FirebaseDatabase.getInstance().getReference().child("Trabajadores");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!aux) {
                        if (editado != true) {
                            c = snapshot.child(dni).getValue(Trabajador.class);

                            Glide.with(getApplicationContext()).load(c.getImageUri()).into(ivTrabajador);
                            etNombre.setText(c.getNombre());
                            etDni.setText(c.getDni());
                            etTelefono.setText(c.getTelefono());
                            etEmail.setText(c.getEmail());
                        } else {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (etDni.getText().toString().equals(child.child("dni").getValue().toString())) {
                                    trabajadorexiste = true;
                                }
                            }
                            if (trabajadorexiste == false) {
                                database = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(newTrabajador.getDni());
                                database.setValue(newTrabajador);
                                VistaTrabajador.dni = newTrabajador.getNombre();
                                Toast.makeText(getApplicationContext(), "Trabajador Modificado Correctamente", Toast.LENGTH_LONG).show();
                                VistaTrabajador.tvDni.setText(newTrabajador.getDni());
                                VistaTrabajador.tvNombre.setText(newTrabajador.getNombre());
                                VistaTrabajador.tvTelefono.setText(newTrabajador.getTelefono());
                                VistaTrabajador.tvEmail.setText(newTrabajador.getEmail());
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
        }else{
            SQLTrabajadoresController sql = new SQLTrabajadoresController(getApplicationContext());
            Trabajador a = sql.getTrabajador(dni);
            etNombre.setText(a.getNombre());
            etDni.setText(a.getDni());
            etTelefono.setText(a.getTelefono());
            etEmail.setText(a.getEmail());
        }
    }

    private void setup() {
        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDni.getText().toString().isEmpty()||etNombre.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Rellene el dni y nombre", Toast.LENGTH_LONG).show();
                }else {
                    newTrabajador = new Trabajador(etNombre.getText().toString(), etDni.getText().toString(), etEmail.getText().toString(), etTelefono.getText().toString(), Uri.parse(c.getImageUri()), Uri.parse(c.getImgStorage()));
                    SQLTrabajadoresController sql = new SQLTrabajadoresController(getApplicationContext());
                    if(isNetworkAvailable()){
                        database = FirebaseDatabase.getInstance().getReference().child("Trabajadores").child(dni);
                        editado = true;
                        database.removeValue();
                    }else{
                        sql.borrarTrabajadorAux(newTrabajador);
                        sql.anadirTrabajadorAux(newTrabajador);
                    }
                    sql.modificaTrabajador(newTrabajador);
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

            StorageReference fileReference = storage.getReference("imagenes_trabajadors").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivTrabajador);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
