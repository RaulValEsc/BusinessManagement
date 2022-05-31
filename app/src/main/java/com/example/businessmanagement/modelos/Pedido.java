package com.example.businessmanagement.modelos;

public class Pedido {

    private String idCom,idProd,idUser;
    private int cantidad;
    private double subtotal;

    public Pedido(){}

    public Pedido(String idCom, String idProd, String idUser, int cantidad, double precio) {
        this.idCom = idCom;
        this.idProd = idProd;
        this.idUser = idUser;
        this.cantidad = cantidad;

        this.subtotal = cantidad*precio;

    }

    public String getIdCom() {
        return idCom;
    }

    public void setIdCom(String idCom) {
        this.idCom = idCom;
    }

    public String getIdProd() {
        return idProd;
    }

    public void setIdProd(String idProd) {
        this.idProd = idProd;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}