package com.example.businessmanagement.modelos;

public class Compra {
    private String idProducto;
    private int stock, precio;

    public Compra(){}

    public Compra(String idProducto, int stock, int precio){
        this.idProducto = idProducto;
        this.stock = stock;
        this.precio = precio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
