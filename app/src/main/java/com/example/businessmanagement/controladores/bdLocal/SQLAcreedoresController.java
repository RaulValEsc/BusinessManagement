package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Acreedor;

import java.util.ArrayList;

public class SQLAcreedoresController extends SqLiteController{

    public SQLAcreedoresController(Context context) {
        super(context);
    }

    public long anadirAcreedor(Acreedor acreedor){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", acreedor.getNif());
            cv.put("nombre",acreedor.getNombre());
            cv.put("telefono",acreedor.getTelefono());
            cv.put("email",acreedor.getEmail());
            id = db.insert(SqLiteController.TABLA_ACREEDORES,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarAcreedor(String nif){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_ACREEDORES,"nif=" + nif,null);
        }
        db.close();
        return nLineas;
    }

    public long modificaAcreedor(Acreedor acreedor) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", acreedor.getNif());
            cv.put("nombre",acreedor.getNombre());
            cv.put("telefono",acreedor.getTelefono());
            cv.put("email",acreedor.getEmail());
            nLineas = db.update(SqLiteController.TABLA_ACREEDORES, cv, "nif="+acreedor.getNif(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Acreedor> cargarAcreedores(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Acreedor> listaAcreedores = new ArrayList<Acreedor>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_ACREEDORES+" ORDER BY nombre ASC , nif ASC",null);
        while (cursor.moveToNext()){
            Acreedor newAcreedor = new Acreedor();
            newAcreedor.setNif(cursor.getString(0));
            newAcreedor.setNombre(cursor.getString(1));
            newAcreedor.setTelefono(cursor.getString(2));
            newAcreedor.setEmail(cursor.getString(3));

            listaAcreedores.add(newAcreedor);

        }
        return listaAcreedores;
    }

    public Acreedor getAcreedor(String nif){
        SQLiteDatabase db = getReadableDatabase();
        Acreedor newAcreedor = new Acreedor();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_ACREEDORES+" WHERE nif = "+nif,null);
        cursor.moveToFirst();

        newAcreedor.setNif(cursor.getString(0));
        newAcreedor.setNombre(cursor.getString(1));
        newAcreedor.setTelefono(cursor.getString(2));
        newAcreedor.setEmail(cursor.getString(3));

        return newAcreedor;
    }
}
