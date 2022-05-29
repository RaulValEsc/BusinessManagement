package com.example.businessmanagement.modelos;

import android.net.Uri;

public class Comercio {

    String imageUri, imgStorage;
    String nombre,descripcion;

    public Comercio(){}

    public Comercio(String nombre, String descripcion, String imageUri, String imgStorage){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imageUri = imageUri;
        this.imgStorage = imgStorage;
    }

    public Comercio(String nombre, String descripcion, Uri imageUri, Uri imgStorage){
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}