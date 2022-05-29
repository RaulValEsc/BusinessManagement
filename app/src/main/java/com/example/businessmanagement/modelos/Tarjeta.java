package com.example.businessmanagement.modelos;

public class Tarjeta {

    String id;
    double saldo;

    public Tarjeta(){

    }

    public Tarjeta(double saldo){
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
