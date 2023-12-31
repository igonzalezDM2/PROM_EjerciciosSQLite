package com.ejercicio.ejerciciosqlite1;

public class Usuario {
    private byte[] img;
    private String dni;
    private String nom, tlf;


    public Usuario(String dni, byte[] img, String nom, String tlf) {
        this.dni = dni;
        this.img = img;
        this.nom = nom;
        this.tlf = tlf;
    }

    public Usuario() {}

    public byte[] getImg() {
        return img;
    }

    public Usuario setImg(byte[] img) {
        this.img = img;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Usuario setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getTlf() {
        return tlf;
    }

    public Usuario setTlf(String tlf) {
        this.tlf = tlf;
        return this;
    }

    public String getDni() {
        return dni;
    }

    public Usuario setDni(String dni) {
        this.dni = dni;
        return this;
    }
}
