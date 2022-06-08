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
import com.example.businessmanagement.controladores.bdLocal.SQLAcreedoresController;
import com.example.businessmanagement.controladores.bdLocal.SQLProveedoresController;
import com.example.businessmanagement.modelos.Proveedor;
import com.example.businessmanagement.vistas.Cliente.VistaCliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class EditarProveedor extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivProveedor;
    EditText etNombre, etNif, etEmail, etTelefono;
    Button bEditar;

    boolean proveedorexiste = false, editado = false;

    Proveedor a, newProveedor;

    String nif,imgStorage;

    Uri imageUri, postStorage;

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proveedor);

        editado= false;
        aux = false;

        if(isNetworkAvailable()){
            storage = FirebaseStorage.getInstance();
        }

        Bundle b = getIntent().getExtras();

        ivProveedor = findViewById(R.id.ivProveedor);

        etNif = findViewById(R.id.tvEditarNIF);
        etNombre = findViewById(R.id.tvEditarNombre);
        etEmail = findViewById(R.id.tvEditarEmail);
        etTelefono = findViewById(R.id.tvEditarTelefono);

        bEditar = findViewById(R.id.bModificar);

        nif = b.getString("nif");

        cargarProveedor();

        setup();
    }

    private void cargarProveedor() {
        if(isNetworkAvailable()) {
            database = FirebaseDatabase.getInstance().getReference().child("Proveedores");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!aux) {
                        if (editado != true) {
                            a = snapshot.child(nif).getValue(Proveedor.class);

                            Glide.with(getApplicationContext()).load(a.getImageUri()).into(ivProveedor);
                            etNombre.setText(a.getNombre());
                            etNif.setText(a.getNif());
                            etTelefono.setText(a.getTelefono());
                            etEmail.setText(a.getEmail());
                        } else {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (etNif.getText().toString().equals(child.child("nif").getValue().toString())) {
                                    proveedorexiste = true;
                                }
                            }
                            if (proveedorexiste == false) {
                                database = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(newProveedor.getNif());
                                database.setValue(newProveedor);
                                VistaCliente.dni = newProveedor.getNombre();
                                Toast.makeText(getApplicationContext(), "Proveedor Modificado Correctamente", Toast.LENGTH_LONG).show();
                                VistaCliente.tvDni.setText(newProveedor.getNif());
                                VistaCliente.tvNombre.setText(newProveedor.getNombre());
                                VistaCliente.tvTelefono.setText(newProveedor.getTelefono());
                                VistaCliente.tvEmail.setText(newProveedor.getEmail());
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
            SQLProveedoresController sql = new SQLProveedoresController(getApplicationContext());
            Proveedor a = sql.getProveedor(nif);
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
                    newProveedor = new Proveedor(etNombre.getText().toString(), etNif.getText().toString(), etEmail.getText().toString(), etTelefono.getText().toString(), Uri.parse(a.getImageUri()), Uri.parse(a.getImgStorage()));
                    SQLProveedoresController sql = new SQLProveedoresController(getApplicationContext());
                    if(isNetworkAvailable()){
                        database = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(nif);
                        editado = true;
                        database.removeValue();
                    }else{
                        sql.borrarProveedorAux(newProveedor);
                        sql.anadirProveedorAux(newProveedor);
                    }
                    sql.modificaProveedor(newProveedor);
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

            StorageReference fileReference = storage.getReference("imagenes_proveedores").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivProveedor);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}