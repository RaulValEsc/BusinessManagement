package com.example.businessmanagement.modelos;

import android.net.Uri;

public class Proveedor {
    private String imageUri, imgStorage;
    private String nombre,nif,telefono,email;

    public Proveedor(){};

    public Proveedor(String nombre, String nif, String telefono, String email, String imageUri, String imgStorage){
        this.nombre = nombre;
        this.nif = nif;
        this.telefono = telefono;
        this.email = email;
        this.imageUri = imageUri;
        this.imgStorage = imgStorage;
    }

    public Proveedor(String nombre, String nif, String telefono, String email, Uri imageUri, Uri imgStorage){
        this.nombre = nombre;
        this.nif = nif;
        this.telefono = telefono;
        this.email = email;
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

    public String getNif() { return nif; }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

