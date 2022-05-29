package com.example.businessmanagement.modelos;

import android.net.Uri;

public class Usuario {

    String imageUri, imgStorage;
    String nombre, email;
    Tarjeta tarjeta;

    public Usuario(){

    }

    public Usuario(String nombre, Tarjeta tarjeta, String email, String imageUri, String imgStorage) {
        this.imageUri = imageUri;
        this.imgStorage = imgStorage;
        this.nombre = nombre;
        this.email = email;
        this.tarjeta = tarjeta;
    }

    public Usuario(String nombre, Tarjeta tarjeta, String email, Uri imageUri, Uri imgStorage) {
        this.nombre = nombre;
        this.email = email;
        this.tarjeta = tarjeta;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }
}
