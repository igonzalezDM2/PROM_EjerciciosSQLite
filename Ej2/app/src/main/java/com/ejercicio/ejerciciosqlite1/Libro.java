package com.ejercicio.ejerciciosqlite1;

public class Libro {
    private byte[] img;
    private Integer id;
    private String nom, isbn, genero;

    private Usuario usuario;


    public Libro(Integer id, byte[] img, String nom, String isbn, String genero) {
        this.id = id;
        this.img = img;
        this.nom = nom;
        this.isbn = isbn;
        this.genero = genero;
    }

    public Libro() {}

    public byte[] getImg() {
        return img;
    }

    public Libro setImg(byte[] img) {
        this.img = img;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Libro setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public Libro setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getGenero() {
        return genero;
    }

    public Libro setGenero(String genero) {
        this.genero = genero;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Libro setId(Integer id) {
        this.id = id;
        return this;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Libro setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }
}
