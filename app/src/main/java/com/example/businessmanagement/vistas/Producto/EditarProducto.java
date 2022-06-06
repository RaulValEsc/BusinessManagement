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
import com.example.businessmanagement.controladores.SpinnerProveedoresAdapter;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.modelos.Proveedor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class EditarProducto extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE = 100;

    FirebaseStorage storage;

    ImageView ivProducto;
    EditText etNombre, etCodigo, etPrecio, etStock;
    Button bEditar;

    boolean productoexiste = false, editado = false;

    Producto c, newProducto;

    String codigo,imgStorage;

    Uri imageUri, postStorage;

    String codigoProveedor;
    Spinner pickerProveedores;
    SpinnerProveedoresAdapter spinnerAdapter;

    ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();

    DatabaseReference database;

    public static boolean aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        editado= false;
        aux = false;

        storage = FirebaseStorage.getInstance();

        Bundle b = getIntent().getExtras();

        ivProducto = findViewById(R.id.ivProducto);

        etCodigo = findViewById(R.id.tvEditarDNI);
        etNombre = findViewById(R.id.tvEditarNombre);
        etPrecio = findViewById(R.id.tvEditarPrecio);
        etStock = findViewById(R.id.tvEditarStock);

        bEditar = findViewById(R.id.bModificar);

        codigo = b.getString("codigo");

        cargarProducto();

        cargarSpinnerProveedores();

        setup();
    }

    private void cargarSpinnerProveedores() {

        database.child("Proveedores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    proveedores.add((Proveedor) child.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Proveedor[] proveedoresList = (Proveedor[]) proveedores.toArray();

        spinnerAdapter = new SpinnerProveedoresAdapter(this.getApplicationContext(), android.R.layout.simple_spinner_item, proveedoresList);

        pickerProveedores = findViewById(R.id.pickProveedor);

        pickerProveedores.setAdapter(spinnerAdapter);

        pickerProveedores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                Proveedor proveedor = spinnerAdapter.getItem(position);

                codigoProveedor = proveedor.getNif();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  };
        });
    }

    private void cargarProducto() {
        database = FirebaseDatabase.getInstance().getReference().child("Productos");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!aux) {
                    if (editado != true) {
                        c = snapshot.child(codigo).getValue(Producto.class);

                        Glide.with(getApplicationContext()).load(c.getImageUri()).into(ivProducto);
                        etNombre.setText(c.getNombre());
                        etCodigo.setText(c.getCodigo());
                        etStock.setText(c.getStock());
                        etPrecio.setText(c.getPrecio());
                    } else {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (etCodigo.getText().toString().equals(child.child("codigo").getValue().toString())) {
                                productoexiste = true;
                            }
                        }
                        if (productoexiste == false) {
                            database = FirebaseDatabase.getInstance().getReference().child("Productos").child(newProducto.getCodigo());
                            database.setValue(newProducto);
                            VistaProducto.codigo = newProducto.getNombre();
                            Toast.makeText(getApplicationContext(), "Producto Modificado Correctamente", Toast.LENGTH_LONG).show();
                            VistaProducto.tvCodigo.setText(newProducto.getCodigo());
                            VistaProducto.tvNombre.setText(newProducto.getNombre());
                            VistaProducto.tvStock.setText(newProducto.getStock());
                            VistaProducto.tvPrecio.setText(newProducto.getPrecio());
                            finish();
                        } else {
                            etNombre.setText("");
                            etCodigo.setText("");
                            etPrecio.setText("");
                            etStock.setText("");
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
                if(etCodigo.getText().toString().isEmpty()||etNombre.getText().toString().isEmpty()||codigoProveedor.isEmpty()){
                    Toast.makeText(getApplicationContext(),"El nombre, codigo y proveedor son campos obligatorios", Toast.LENGTH_LONG).show();
                }else {
                    newProducto = new Producto(etNombre.getText().toString(), etCodigo.getText().toString(),codigoProveedor, Integer.parseInt(etPrecio.getText().toString()), Integer.parseInt(etStock.getText().toString()), Uri.parse(c.getImageUri()), Uri.parse(c.getImgStorage()));
                    database = FirebaseDatabase.getInstance().getReference().child("Productos").child(codigo);
                    editado = true;
                    database.removeValue();
                }
            }
        });

        ivProducto.setOnClickListener(new View.OnClickListener() {
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

            StorageReference fileReference = storage.getReference("imagenes_productos").child(uri.getLastPathSegment());
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

        Glide.with(getApplicationContext()).load(imageUri).into(ivProducto);

    }
}
