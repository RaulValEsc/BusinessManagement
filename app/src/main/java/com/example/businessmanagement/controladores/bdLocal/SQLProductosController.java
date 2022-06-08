package com.example.businessmanagement.controladores.bdLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.modelos.Producto;
import com.example.businessmanagement.modelos.Producto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SQLProductosController extends SqLiteController{

    public SQLProductosController(Context context) {
        super(context);
    }

    public long anadirProducto(Producto producto){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("codigo", producto.getCodigo());
            cv.put("idProveedor",producto.getIdProveedor());
            cv.put("nombre",producto.getNombre());
            cv.put("stock",producto.getStock());
            cv.put("precio",producto.getPrecio());
            id = db.insert(SqLiteController.TABLA_PRODUCTOS,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarProducto(String codigo){
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete(SqLiteController.TABLA_PRODUCTOS,"codigo='" + codigo+"'",null);
        }
        db.close();
        return nLineas;
    }

    public long modificaProducto(Producto producto) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("codigo", producto.getCodigo());
            cv.put("idProveedor",producto.getIdProveedor());
            cv.put("nombre",producto.getNombre());
            cv.put("stock",producto.getStock());
            cv.put("precio",producto.getPrecio());
            nLineas = db.update(SqLiteController.TABLA_PRODUCTOS, cv, "codigo='"+producto.getCodigo()+"'", null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Producto> cargarProductos(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_PRODUCTOS+" ORDER BY nombre ASC , codigo ASC",null);
        while (cursor.moveToNext()){
            Producto newProducto = new Producto();
            newProducto.setCodigo(cursor.getString(0));
            newProducto.setIdProveedor(cursor.getString(1));
            newProducto.setNombre(cursor.getString(2));
            newProducto.setStock(cursor.getInt(3));
            newProducto.setPrecio(cursor.getInt(4));

            listaProductos.add(newProducto);

        }
        return listaProductos;
    }

    public Producto getProducto(String codigo){
        SQLiteDatabase db = getReadableDatabase();
        Producto newProducto = new Producto();
        Cursor cursor = db.rawQuery("SELECT * FROM "+SqLiteController.TABLA_PRODUCTOS+" WHERE codigo = '"+codigo+"'",null);
        cursor.moveToFirst();

        newProducto.setCodigo(cursor.getString(0));
        newProducto.setIdProveedor(cursor.getString(1));
        newProducto.setNombre(cursor.getString(2));
        newProducto.setStock(cursor.getInt(3));
        newProducto.setPrecio(cursor.getInt(4));

        return newProducto;
    }

    // AUXILIARES

    public long anadirProductoAux(Producto producto){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("codigo", producto.getCodigo());
            cv.put("idProveedor", producto.getIdProveedor());
            cv.put("nombre",producto.getNombre());
            cv.put("stock",producto.getStock());
            cv.put("precio",producto.getPrecio());
            id = db.insert(TABLA_AUX_PRODUCTOS,null,cv);
        }
        db.close();
        return id;
    }

    public long borrarProductoAux(Producto producto){
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if(db != null){
            ContentValues cv = new ContentValues();
            cv.put("codigo", producto.getCodigo());
            cv.put("idProveedor", producto.getIdProveedor());
            cv.put("nombre",producto.getNombre());
            cv.put("stock",producto.getStock());
            cv.put("precio",producto.getPrecio());
            id = db.insert(TABLA_AUX_BORRAR_PRODUCTOS,null,cv);
        }
        db.close();
        return id;
    }

    public ArrayList<Producto> cargarProductosAux(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLA_AUX_PRODUCTOS+" ORDER BY nombre ASC , codigo ASC",null);
        while (cursor.moveToNext()){
            Producto newProducto = new Producto();
            newProducto.setCodigo(cursor.getString(0));
            newProducto.setIdProveedor(cursor.getString(1));
            newProducto.setNombre(cursor.getString(2));
            newProducto.setStock(cursor.getInt(3));
            newProducto.setPrecio(cursor.getInt(4));

            listaProductos.add(newProducto);

        }
        return listaProductos;
    }

    public ArrayList<Producto> cargarProductosBorrarAux(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLA_AUX_BORRAR_PRODUCTOS+" ORDER BY nombre ASC , codigo ASC",null);
        while (cursor.moveToNext()){
            Producto newProducto = new Producto();
            newProducto.setCodigo(cursor.getString(0));
            newProducto.setIdProveedor(cursor.getString(1));
            newProducto.setNombre(cursor.getString(2));
            newProducto.setStock(cursor.getInt(3));
            newProducto.setPrecio(cursor.getInt(4));

            listaProductos.add(newProducto);

        }
        return listaProductos;
    }

    // SINCRONIZAR
    public void sincronizarCambios(){
        ArrayList<Producto> productosBorrarAux = cargarProductosBorrarAux();
        ArrayList<Producto> productosAux = cargarProductosAux();

        for (Producto a:productosBorrarAux) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Productos").child(a.getCodigo());
            database.removeValue();
        }

        for (Producto a:productosAux) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Productos").child(a.getCodigo());
            database.setValue(a);
        }

        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_PRODUCTOS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_PRODUCTOS);
        db.execSQL(createProductosAux);db.execSQL(createProductosAuxBorrar);

    }
}
