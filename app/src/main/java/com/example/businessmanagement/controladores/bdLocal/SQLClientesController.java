package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Cliente;

import java.util.ArrayList;

public class SQLClientesController extends SqLiteController{

    public SQLClientesController(Context context) {
        super(context);
    }

    public long anadirCliente(Cliente cliente){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("dni", cliente.getDni());
            cv.put("nombre",cliente.getNombre());
            cv.put("telefono",cliente.getTelefono());
            cv.put("email",cliente.getEmail());
            id = db.insert(SqLiteController.TABLA_CLIENTES,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarCliente(String dni){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_CLIENTES,"dni=" + dni,null);
        }
        db.close();
        return nLineas;
    }

    public long modificaCliente(Cliente cliente) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("dni", cliente.getDni());
            cv.put("nombre",cliente.getNombre());
            cv.put("telefono",cliente.getTelefono());
            cv.put("email",cliente.getEmail());
            nLineas = db.update(SqLiteController.TABLA_CLIENTES, cv, "dni="+cliente.getDni(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Cliente> cargarClientes(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_CLIENTES+" ORDER BY nombre ASC , dni ASC",null);
        while (cursor.moveToNext()){
            Cliente newCliente = new Cliente();
            newCliente.setDni(cursor.getString(0));
            newCliente.setNombre(cursor.getString(1));
            newCliente.setTelefono(cursor.getString(2));
            newCliente.setEmail(cursor.getString(3));

            listaClientes.add(newCliente);

        }
        return listaClientes;
    }

    public Cliente getCliente(String dni){
        SQLiteDatabase db = getReadableDatabase();
        Cliente newCliente = new Cliente();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_CLIENTES+" WHERE dni = "+dni,null);
        cursor.moveToFirst();

        newCliente.setDni(cursor.getString(0));
        newCliente.setNombre(cursor.getString(1));
        newCliente.setTelefono(cursor.getString(2));
        newCliente.setEmail(cursor.getString(3));

        return newCliente;
    }
}

