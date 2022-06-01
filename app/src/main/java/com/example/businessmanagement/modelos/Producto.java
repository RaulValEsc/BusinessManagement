package com.example.businessmanagement.modelos;

import android.net.Uri;

public class Producto {
    private String imageUri, imgStorage;
    private String nombre,codigo,idProveedor;
    private int precio, stock;

    public Producto(){};

    public Producto(String nombre, String codigo, String idProveedor, int precio, int stock, String imageUri, String imgStorage){
        this.nombre = nombre;
        this.codigo = codigo;
        this.idProveedor = idProveedor;
        this.imageUri = imageUri;
        this.imgStorage = imgStorage;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(String nombre, String codigo, String idProveedor, int precio, int stock, Uri imageUri, Uri imgStorage){
        this.nombre = nombre;
        this.codigo = codigo;
        this.idProveedor = idProveedor;
        this.precio = precio;
        this.stock = stock;
        if(imageUri != null){
            this.imageUri = imageUri.toString();
        }
        if(imgStorage != null){
            this.imgStorage = imgStorage.toString();
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImgStorage() {
        return imgStorage;
    }

    public void setImgStorage(String imgStorage) {
        this.imgStorage = imgStorage;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void addStock(int add) {
        this.stock += add;
    }
}
