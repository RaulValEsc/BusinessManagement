package com.example.businessmanagement.modelos;

import android.net.Uri;

public class Producto {

    String imageUri, imgStorage;
    String nombre,idComercio;
    double precio;
    int stock;

    public Producto(){

    }

    public Producto(String nombre, double precio, int stock, String idComercio, String imageUri, String imgStorage) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idComercio = idComercio;
        this.imageUri = imageUri;
        this.imgStorage = imgStorage;
    }

    public Producto(String nombre, double precio, int stock, String idComercio, Uri imageUri, Uri imgStorage) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idComercio = idComercio;
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

    public String getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(String idComercio) {
        this.idComercio = idComercio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
