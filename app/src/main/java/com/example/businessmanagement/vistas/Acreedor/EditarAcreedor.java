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
import com.example.businessmanagement.vistas.Cliente.VistaCliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditarAcreedor extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivAcreedor;
    EditText etNombre, etNif, etEmail, etTelefono;
    Button bEditar;

    boolean acreedorexiste = false, editado = false;

    Acreedor a, newAcreedor;

    String nif,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_acreedor);

        editado= false;
        aux = false;

        if(isNetworkAvailable()){
            storage = FirebaseStorage.getInstance();
        }

        Bundle b = getIntent().getExtras();

        ivAcreedor = findViewById(R.id.ivAcreedor);

        etNif = findViewById(R.id.tvEditarNIF);
        etNombre = findViewById(R.id.tvEditarNombre);
        etEmail = findViewById(R.id.tvEditarEmail);
        etTelefono = findViewById(R.id.tvEditarTelefono);

        bEditar = findViewById(R.id.bModificar);

        nif = b.getString("nif");

        cargarAcreedor();

        setup();
    }

    private void cargarAcreedor() {
        if(isNetworkAvailable()){
            database = FirebaseDatabase.getInstance().getReference().child("Acreedores");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!aux) {
                        if (editado != true) {
                            a = snapshot.child(nif).getValue(Acreedor.class);

                            Glide.with(getApplicationContext()).load(a.getImageUri()).into(ivAcreedor);
                            etNombre.setText(a.getNombre());
                            etNif.setText(a.getNif());
                            etTelefono.setText(a.getTelefono());
                            etEmail.setText(a.getEmail());
                        } else {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (etNif.getText().toString().equals(child.child("nif").getValue().toString())) {
                                    acreedorexiste = true;
                                }
                            }
                            if (acreedorexiste == false) {
                                database = FirebaseDatabase.getInstance().getReference().child("Acreedores").child(newAcreedor.getNif());
                                database.setValue(newAcreedor);
                                VistaCliente.dni = newAcreedor.getNombre();
                                Toast.makeText(getApplicationContext(), "Acreedor Modificado Correctamente", Toast.LENGTH_LONG).show();
                                VistaCliente.tvDni.setText(newAcreedor.getNif());
                                VistaCliente.tvNombre.setText(newAcreedor.getNombre());
                                VistaCliente.tvTelefono.setText(newAcreedor.getTelefono());
                                VistaCliente.tvEmail.setText(newAcreedor.getEmail());
                                finish();
                            } else {
                                etNombre.setText("");
                                etNif.setText("");
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
            SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
            Acreedor a = sql.getAcreedor(nif);
            etNombre.setText(a.getNombre());
            etNif.setText(a.getNif());
            etTelefono.setText(a.getTelefono());
            etEmail.setText(a.getEmail());
        }
    }

    private void setup() {
        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNif.getText().toString().isEmpty()||etNombre.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Rellene el nif y nombre", Toast.LENGTH_LONG).show();
                }else {
                    newAcreedor = new Acreedor(etNombre.getText().toString(), etNif.getText().toString(), etEmail.getText().toString(), etTelefono.getText().toString(), Uri.parse(a.getImageUri()), Uri.parse(a.getImgStorage()));
                    SQLAcreedoresController sql = new SQLAcreedoresController(getApplicationContext());
                    if(isNetworkAvailable()){
                        database = FirebaseDatabase.getInstance().getReference().child("Acreedores").child(nif);
                        editado = true;
                        database.removeValue();
                    }else{
                        sql.borrarAcreedorAux(newAcreedor);
                        sql.anadirAcreedorAux(newAcreedor);
                    }
                    sql.modificaAcreedor(newAcreedor);
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

            if (postStorage == null){
                postStorage = Uri.EMPTY;
            }

            Uri uri = data.getData();

            imgStorage = uri.toString();

            StorageReference fileReference = storage.getReference("imagenes_acreedores").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivAcreedor);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

