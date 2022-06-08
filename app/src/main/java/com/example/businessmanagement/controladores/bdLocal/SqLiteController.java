package com.example.businessmanagement.controladores.bdLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqLiteController extends SQLiteOpenHelper {
    public static final String NAME = "BD_BUSINESS_MANAGEMENT.db";

    // TABLAS AUX
    public static String TABLA_AUX_ACREEDORES = "Aux_Acreedores";
    public static String TABLA_AUX_BORRAR_ACREEDORES = "Aux_Borrar_Acreedores";

    public static String TABLA_AUX_CLIENTES = "Aux_Clientes";
    public static String TABLA_AUX_BORRAR_CLIENTES = "Aux_Borrar_Clientes";

    public static String TABLA_AUX_COMPRAS = "Aux_Compras";
    public static String TABLA_AUX_BORRAR_COMPRAS = "Aux_Borrar_Compras";

    public static String TABLA_AUX_PRODUCTOS = "Aux_Productos";
    public static String TABLA_AUX_BORRAR_PRODUCTOS = "Aux_Borrar_Productos";

    public static String TABLA_AUX_PROVEEDORES = "Aux_Proveedores";
    public static String TABLA_AUX_BORRAR_PROVEEDORES = "Aux_Borrar_Proveedores";

    public static String TABLA_AUX_TRABAJADORES = "Aux_Trabajadores";
    public static String TABLA_AUX_BORRAR_TRABAJADORES = "Aux_Borrar_Trabajadores";

    public static String TABLA_AUX_VENTAS = "Aux_Ventas";
    public static String TABLA_AUX_BORRAR_VENTAS = "Aux_Borrar_Ventas";

    // TABLAS
    public static final String TABLA_ACREEDORES = "Acreedores";
    public static final String TABLA_CLIENTES = "Clientes";
    public static final String TABLA_COMPRAS = "Compras";
    public static final String TABLA_PRODUCTOS = "Productos";
    public static final String TABLA_PROVEEDORES = "Proveedores";
    public static final String TABLA_TRABAJADORES = "Trabajadores";
    public static final String TABLA_VENTAS = "Ventas";


    public static int version = 3;

    // SENTENCIAS SQL CREATE TABLE
    public static final String createAcreedores = "CREATE TABLE "+TABLA_ACREEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createAcreedoresAux = "CREATE TABLE "+TABLA_AUX_ACREEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createAcreedoresAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_ACREEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    public static final String createClientes = "CREATE TABLE "+TABLA_CLIENTES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createClientesAux = "CREATE TABLE "+TABLA_AUX_CLIENTES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createClientesAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_CLIENTES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    public static final String createCompras = "CREATE TABLE "+TABLA_COMPRAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    public static final String createComprasAux = "CREATE TABLE "+TABLA_AUX_COMPRAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    public static final String createComprasAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_COMPRAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";

    public static final String createProductos = "CREATE TABLE "+TABLA_PRODUCTOS+" (codigo VARCHAR2(100) PRIMARY KEY, idProveedor VARCHAR2(100), nombre VARCHAR(100), " +
            "stock NUMBER(10), precio NUMBER(10))";
    public static final String createProductosAux = "CREATE TABLE "+TABLA_AUX_PRODUCTOS+" (codigo VARCHAR2(100) PRIMARY KEY, idProveedor VARCHAR2(100), nombre VARCHAR(100), " +
            "stock NUMBER(10), precio NUMBER(10))";
    public static final String createProductosAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_PRODUCTOS+" (codigo VARCHAR2(100) PRIMARY KEY, idProveedor VARCHAR2(100), nombre VARCHAR(100), " +
            "stock NUMBER(10), precio NUMBER(10))";

    public static final String createProveedores = "CREATE TABLE "+TABLA_PROVEEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createProveedoresAux = "CREATE TABLE "+TABLA_AUX_PROVEEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createProveedoresAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_PROVEEDORES+" (nif VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";


    public static final String createTrabajadores = "CREATE TABLE "+TABLA_TRABAJADORES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createTrabajadoresAux = "CREATE TABLE "+TABLA_AUX_TRABAJADORES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";
    public static final String createTrabajadoresAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_TRABAJADORES+" (dni VARCHAR2(100) PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), email VARCHAR(100))";

    public static final String createVentas = "CREATE TABLE "+TABLA_VENTAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    public static final String createVentasAux = "CREATE TABLE "+TABLA_AUX_VENTAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    public static final String createVentasAuxBorrar = "CREATE TABLE "+TABLA_AUX_BORRAR_VENTAS+" (idProducto VARCHAR2(100) PRIMARY KEY, stock NUMBER(10), precio NUMBER(10))";
    // END SENTENCIAS SQL CREATE TABLE

    public SqLiteController(Context context) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLES
        db.execSQL(createAcreedores);db.execSQL(createAcreedoresAux);db.execSQL(createAcreedoresAuxBorrar);
        db.execSQL(createClientes);db.execSQL(createClientesAux);db.execSQL(createClientesAuxBorrar);
        db.execSQL(createCompras);db.execSQL(createComprasAux);db.execSQL(createComprasAuxBorrar);
        db.execSQL(createProductos);db.execSQL(createProductosAux);db.execSQL(createProductosAuxBorrar);
        db.execSQL(createProveedores);db.execSQL(createProveedoresAux);db.execSQL(createProveedoresAuxBorrar);
        db.execSQL(createTrabajadores);db.execSQL(createTrabajadoresAux);db.execSQL(createTrabajadoresAuxBorrar);
        db.execSQL(createVentas);db.execSQL(createVentasAux);db.execSQL(createVentasAuxBorrar);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ACREEDORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_ACREEDORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_ACREEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CLIENTES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_CLIENTES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMPRAS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_COMPRAS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_PRODUCTOS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PROVEEDORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_PROVEEDORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_PROVEEDORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_TRABAJADORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_TRABAJADORES);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_TRABAJADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_VENTAS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_VENTAS);db.execSQL("DROP TABLE IF EXISTS " + TABLA_AUX_BORRAR_VENTAS);
        onCreate(db);
    }
}
