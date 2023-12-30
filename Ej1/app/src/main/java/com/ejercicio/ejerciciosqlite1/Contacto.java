package com.ejercicio.ejerciciosqlite1;

public class Contacto {
    private byte[] img;
    private Integer id;
    private String nom, tlf;


    public Contacto(Integer id, byte[] img, String nom, String tlf) {
        this.id = id;
        this.img = img;
        this.nom = nom;
        this.tlf = tlf;
    }

    public Contacto() {}

    public byte[] getImg() {
        return img;
    }

    public Contacto setImg(byte[] img) {
        this.img = img;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Contacto setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getTlf() {
        return tlf;
    }

    public Contacto setTlf(String tlf) {
        this.tlf = tlf;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Contacto setId(Integer id) {
        this.id = id;
        return this;
    }
}
