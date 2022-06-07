package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Trabajador;

import java.util.ArrayList;

public class SQLTrabajadoresController extends SqLiteController{

    public SQLTrabajadoresController(Context context) {
        super(context);
    }

    public long anadirTrabajador(Trabajador trabajador){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("dni", trabajador.getDni());
            cv.put("nombre",trabajador.getNombre());
            cv.put("telefono",trabajador.getTelefono());
            cv.put("email",trabajador.getEmail());
            id = db.insert(SqLiteController.TABLA_TRABAJADORES,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarTrabajador(String dni){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_TRABAJADORES,"dni=" + dni,null);
        }
        db.close();
        return nLineas;
    }

    public long modificaTrabajador(Trabajador trabajador) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("dni", trabajador.getDni());
            cv.put("nombre",trabajador.getNombre());
            cv.put("telefono",trabajador.getTelefono());
            cv.put("email",trabajador.getEmail());
            nLineas = db.update(SqLiteController.TABLA_TRABAJADORES, cv, "dni="+trabajador.getDni(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Trabajador> cargarTrabajadores(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Trabajador> listaTrabajadores = new ArrayList<Trabajador>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_TRABAJADORES+" ORDER BY nombre ASC , dni ASC",null);
        while (cursor.moveToNext()){
            Trabajador newTrabajador = new Trabajador();
            newTrabajador.setDni(cursor.getString(0));
            newTrabajador.setNombre(cursor.getString(1));
            newTrabajador.setTelefono(cursor.getString(2));
            newTrabajador.setEmail(cursor.getString(3));

            listaTrabajadores.add(newTrabajador);

        }
        return listaTrabajadores;
    }

    public Trabajador getTrabajador(String dni){
        SQLiteDatabase db = getReadableDatabase();
        Trabajador newTrabajador = new Trabajador();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_TRABAJADORES+" WHERE dni = "+dni,null);
        cursor.moveToFirst();

        newTrabajador.setDni(cursor.getString(0));
        newTrabajador.setNombre(cursor.getString(1));
        newTrabajador.setTelefono(cursor.getString(2));
        newTrabajador.setEmail(cursor.getString(3));

        return newTrabajador;
    }
}

