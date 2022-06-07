package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Venta;

import java.util.ArrayList;

public class SQLVentasController extends SqLiteController{

    public SQLVentasController(Context context) {
        super(context);
    }

    public long anadirVenta(Venta venta){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("idProducto", venta.getIdProducto());
            cv.put("stock",venta.getStock());
            cv.put("precio",venta.getPrecio());
            id = db.insert(SqLiteController.TABLA_VENTAS,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarVenta(String idProducto){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_VENTAS,"idProducto=" + idProducto,null);
        }
        db.close();
        return nLineas;
    }

    public long modificaVenta(Venta venta) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("idProducto", venta.getIdProducto());
            cv.put("stock",venta.getStock());
            cv.put("precio",venta.getPrecio());
            nLineas = db.update(SqLiteController.TABLA_VENTAS, cv, "idProducto="+venta.getIdProducto(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Venta> cargarVentas(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Venta> listaVentas = new ArrayList<Venta>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_VENTAS+" ORDER BY idProducto ASC",null);
        while (cursor.moveToNext()){
            Venta newVenta = new Venta();
            newVenta.setIdProducto(cursor.getString(0));
            newVenta.setStock(cursor.getInt(1));
            newVenta.setPrecio(cursor.getInt(2));

            listaVentas.add(newVenta);

        }
        return listaVentas;
    }

    public Venta getVenta(String idProducto){
        SQLiteDatabase db = getReadableDatabase();
        Venta newVenta = new Venta();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_VENTAS+" WHERE idProducto = "+idProducto,null);
        cursor.moveToFirst();

        newVenta.setIdProducto(cursor.getString(0));
        newVenta.setStock(cursor.getInt(1));
        newVenta.setPrecio(cursor.getInt(2));

        return newVenta;
    }
}
