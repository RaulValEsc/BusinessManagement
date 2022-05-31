package com.example.businessmanagement.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.businessmanagement.R;
import com.example.businessmanagement.vistas.Acreedor.ActivityAcreedores;
import com.example.businessmanagement.vistas.Cliente.ActivityClientes;
import com.example.businessmanagement.vistas.Comercio.ActivityComercios;
import com.example.businessmanagement.vistas.Proveedor.ActivityProveedores;
import com.example.businessmanagement.vistas.Trabajador.ActivityTrabajadores;
import com.example.businessmanagement.vistas.Usuario.ActivityUsuarios;

public class Main extends AppCompatActivity {

    Button bCliente, bProveedor, bAcreedor, bTrabajador, bCompras, bVentas, bProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bCliente = findViewById(R.id.bCliente);
        bProveedor = findViewById(R.id.bProveedor);
        bAcreedor = findViewById(R.id.bAcreedor);
        bTrabajador = findViewById(R.id.bTrabajador);
        bCompras = findViewById(R.id.bCompras);
        bVentas = findViewById(R.id.bVentas);
        bProductos = findViewById(R.id.bProductos);

        setup();
    }

    private void setup() {
        bCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityClientes.class);
                startActivity(intent);
            }
        });

        bProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityProveedores.class);
                startActivity(intent);
            }
        });

        bAcreedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAcreedores.class);
                startActivity(intent);
            }
        });

        bTrabajador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityTrabajadores.class);
                startActivity(intent);
            }
        });

        bCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityUsuarios.class);
                startActivity(intent);
            }
        });

        bVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityUsuarios.class);
                startActivity(intent);
            }
        });

        bProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityUsuarios.class);
                startActivity(intent);
            }
        });
    }
}