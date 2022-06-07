package com.example.businessmanagement.controladores.bdLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqLiteController extends SQLiteOpenHelper {
    public static final String NAME = "BD_BUSINESS_MANAGEMENT.db";

    // TABLAS
    public static final String TABLA_ACREEDORES = "Acreedores";
    public static final String TABLA_CLIENTES = "Clientes";
    public static final String TABLA_COMPRAS = "Compras";
    public static final String TABLA_PRODUCTOS = "Productos";
    public static final String TABLA_PROVEEDORES = "Proveedores";
    public static final String TABLA_TRABAJADORES = "Trabajadores";
    public static final String TABLA_VENTAS = "Ventas";


    public static int version = 1;

    // SENTENCIAS SQL CREATE TABLE
    private static final String createAcreedores = "CREATE TABLE "+TABLA_ACREEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    private static final String createClientes = "CREATE TABLE "+TABLA_CLIENTES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    private static final String createCompras = "CREATE TABLE "+TABLA_COMPRAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";

    private static final String createProductos = "CREATE TABLE "+TABLA_PRODUCTOS+" (codigo VARCHAR2(100) PRIMARY KEY, idProveedor VARCHAR2(100), nombre VARCHAR(100), " +
            "stock NUMBER(10), precio NUMBER(10))";

    private static final String createProveedores = "CREATE TABLE "+TABLA_PROVEEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    private static final String createTrabajadores = "CREATE TABLE "+TABLA_TRABAJADORES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    private static final String createVentas = "CREATE TABLE "+TABLA_VENTAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    // END SENTENCIAS SQL CREATE TABLE

    public SqLiteController(Context context) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLES
        db.execSQL(createAcreedores);
        db.execSQL(createClientes);
        db.execSQL(createCompras);
        db.execSQL(createProductos);
        db.execSQL(createProveedores);
        db.execSQL(createTrabajadores);
        db.execSQL(createVentas);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ACREEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PROVEEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TRABAJADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_VENTAS);
        onCreate(db);
    }
}
