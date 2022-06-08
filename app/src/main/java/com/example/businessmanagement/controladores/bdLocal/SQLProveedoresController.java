package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Proveedor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SQLProveedoresController extends SqLiteController{

    public SQLProveedoresController(Context context) {
        super(context);
    }

    public long anadirProveedor(Proveedor proveedor){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", proveedor.getNif());
            cv.put("nombre",proveedor.getNombre());
            cv.put("telefono",proveedor.getTelefono());
            cv.put("email",proveedor.getEmail());
            id = db.insert(SqLiteController.TABLA_PROVEEDORES,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarProveedor(String nif){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_PROVEEDORES,"nif='" + nif+"'",null);
        }
        db.close();
        return nLineas;
    }

    public long modificaProveedor(Proveedor proveedor) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", proveedor.getNif());
            cv.put("nombre",proveedor.getNombre());
            cv.put("telefono",proveedor.getTelefono());
            cv.put("email",proveedor.getEmail());
            nLineas = db.update(SqLiteController.TABLA_PROVEEDORES, cv, "nif='"+proveedor.getNif()+"'", null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Proveedor> cargarProveedores(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Proveedor> listaProveedores = new ArrayList<Proveedor>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_PROVEEDORES+" ORDER BY nombre ASC , nif ASC",null);
        while (cursor.moveToNext()){
            Proveedor newProveedor = new Proveedor();
            newProveedor.setNif(cursor.getString(0));
            newProveedor.setNombre(cursor.getString(1));
            newProveedor.setTelefono(cursor.getString(2));
            newProveedor.setEmail(cursor.getString(3));

            listaProveedores.add(newProveedor);

        }
        return listaProveedores;
    }

    public Proveedor getProveedor(String nif){
        SQLiteDatabase db = getReadableDatabase();
        Proveedor newProveedor = new Proveedor();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_PROVEEDORES+" WHERE nif = '"+nif+"'",null);
        cursor.moveToFirst();

        newProveedor.setNif(cursor.getString(0));
        newProveedor.setNombre(cursor.getString(1));
        newProveedor.setTelefono(cursor.getString(2));
        newProveedor.setEmail(cursor.getString(3));

        return newProveedor;
    }

    // AUXILIARES

    public long anadirProveedorAux(Proveedor proveedor){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", proveedor.getNif());
            cv.put("nombre",proveedor.getNombre());
            cv.put("telefono",proveedor.getTelefono());
            cv.put("email",proveedor.getEmail());
            id = db.insert(TABLA_AUX_PROVEEDORES,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarProveedorAux(Proveedor proveedor){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("nif", proveedor.getNif());
            cv.put("nombre",proveedor.getNombre());
            cv.put("telefono",proveedor.getTelefono());
            cv.put("email",proveedor.getEmail());
            id = db.insert(TABLA_AUX_BORRAR_PROVEEDORES,null,cv);
        }
        db.close();
        return id;
    }

    public ArrayList<Proveedor> cargarProveedoresAux(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Proveedor> listaProveedores = new ArrayList<Proveedor>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLA_AUX_PROVEEDORES+" ORDER BY nombre ASC , nif ASC",null);
        while (cursor.moveToNext()){
            Proveedor newProveedor = new Proveedor();
            newProveedor.setNif(cursor.getString(0));
            newProveedor.setNombre(cursor.getString(1));
            newProveedor.setTelefono(cursor.getString(2));
            newProveedor.setEmail(cursor.getString(3));

            listaProveedores.add(newProveedor);

        }
        return listaProveedores;
    }

    public ArrayList<Proveedor> cargarProveedoresBorrarAux(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Proveedor> listaProveedores = new ArrayList<Proveedor>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLA_AUX_BORRAR_PROVEEDORES+" ORDER BY nombre ASC , nif ASC",null);
        while (cursor.moveToNext()){
            Proveedor newProveedor = new Proveedor();
            newProveedor.setNif(cursor.getString(0));
            newProveedor.setNombre(cursor.getString(1));
            newProveedor.setTelefono(cursor.getString(2));
            newProveedor.setEmail(cursor.getString(3));

            listaProveedores.add(newProveedor);

        }
        return listaProveedores;
    }

    // SINCRONIZAR
    public void sincronizarCambios(){
        ArrayList<Proveedor> proveedoresBorrarAux = cargarProveedoresBorrarAux();
        ArrayList<Proveedor> proveedoresAux = cargarProveedoresAux();

        for (Proveedor a:proveedoresBorrarAux) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(a.getNif());
            database.removeValue();
        }

        for (Proveedor a:proveedoresAux) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Proveedores").child(a.getNif());
            database.setValue(a);
        }

        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_PROVEEDORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_PROVEEDORES);
        db.execSQL(createProveedoresAux);db.execSQL(createProveedoresAuxBorrar);

    }
}

