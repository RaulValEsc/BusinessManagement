package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Compra;

import java.util.ArrayList;

public class SQLComprasController extends SqLiteController{

    public SQLComprasController(Context context) {
        super(context);
    }

    public long anadirCompra(Compra compra){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("idProducto", compra.getIdProducto());
            cv.put("stock",compra.getStock());
            cv.put("precio",compra.getPrecio());
            id = db.insert(SqLiteController.TABLA_COMPRAS,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarCompra(String idProducto){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_COMPRAS,"idProducto=" + idProducto,null);
        }
        db.close();
        return nLineas;
    }

    public long modificaCompra(Compra compra) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("idProducto", compra.getIdProducto());
            cv.put("stock",compra.getStock());
            cv.put("precio",compra.getPrecio());
            nLineas = db.update(SqLiteController.TABLA_COMPRAS, cv, "idProducto="+compra.getIdProducto(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Compra> cargarCompras(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Compra> listaCompras = new ArrayList<Compra>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_COMPRAS+" ORDER BY idProducto ASC",null);
        while (cursor.moveToNext()){
            Compra newCompra = new Compra();
            newCompra.setIdProducto(cursor.getString(0));
            newCompra.setStock(cursor.getInt(1));
            newCompra.setPrecio(cursor.getInt(2));

            listaCompras.add(newCompra);

        }
        return listaCompras;
    }

    public Compra getCompra(String idProducto){
        SQLiteDatabase db = getReadableDatabase();
        Compra newCompra = new Compra();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_COMPRAS+" WHERE idProducto = "+idProducto,null);
        cursor.moveToFirst();

        newCompra.setIdProducto(cursor.getString(0));
        newCompra.setStock(cursor.getInt(1));
        newCompra.setPrecio(cursor.getInt(2));

        return newCompra;
    }
}
