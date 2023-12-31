package com.ejercicio.ejerciciosqlite1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ContactosSQLiteHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_USUARIO =
            "CREATE TABLE usuario (dni TEXT PRIMARY KEY NOT NULL," +
                    " nombre TEXT, " +
                    " imagen BLOB, " +
                    "numero TEXT)";

    private static final String SQL_CREATE_LIBRO =
            "CREATE TABLE libro (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " nombre TEXT, " +
                    " imagen BLOB, " +
                    " genero TEXT, " +
                    " isbn TEXT, " +
                    "usuario TEXT, " +
                    "FOREIGN KEY(usuario) REFERENCES usuario(dni))";

    public ContactosSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USUARIO);
        db.execSQL(SQL_CREATE_LIBRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] campos = new String[] {"dni", "nombre", "imagen", "numero"};
        List<Usuario> contactos = new LinkedList<>();

        Cursor cur = db.query("usuario", campos, null, null, null, null, null);
        while (cur.moveToNext()) {
            String dni = cur.getString(0);
            String nom = cur.getString(1);
            byte[] imagen = cur.getBlob(2);
            String numero = cur.getString(3);
            contactos.add(new Usuario(dni, imagen, nom, numero));
        }
        cur.close();
        db.execSQL("DROP TABLE IF EXISTS `usuario`");
        db.execSQL(SQL_CREATE_USUARIO);

        contactos.forEach(c -> {
            ContentValues cv = new ContentValues();
            cv.put("dni", c.getDni());
            cv.put("nombre", c.getNom());
            cv.put("imagen", c.getImg());
            cv.put("numero", c.getTlf());

            db.insert("contacto", null, cv);
        });

        List<Libro> libros = new LinkedList<>();

        cur = db.rawQuery("SELECT " +
                "libro.id, " +
                "libro.nombre as nombrelibro, " +
                "libro.imagen as imagenlibro, " +
                "libro.genero, " +
                "libro.isbn, " +
                "usuario.dni, " +
                "usuario.nombre as nombreusuario, " +
                "usuario.imagen as imagenusuario, " +
                "usuario.numero " +

                " FROM libro INNER JOIN usuario ON usuario.dni = libro.usuario", new String[]{});

        while (cur.moveToNext()) {
            Integer id = cur.getInt(0);
            String nom = cur.getString(1);
            byte[] imagen = cur.getBlob(2);
            String genero = cur.getString(3);
            String isbn = cur.getString(4);

            String dni = cur.getString(5);
            String nomusr = cur.getString(6);
            byte[] imagenusr = cur.getBlob(7);
            String numero = cur.getString(8);

            libros.add(
                new Libro()
                    .setGenero(genero)
                    .setId(id)
                    .setImg(imagen)
                    .setNom(nom)
                    .setIsbn(isbn)
                    .setUsuario(new Usuario(dni, imagen, nom, numero))
            );

        }
        cur.close();

        db.execSQL("DROP TABLE IF EXISTS `libro`");
        db.execSQL(SQL_CREATE_LIBRO);

        libros.forEach(l -> {
            ContentValues cv = new ContentValues();
            cv.put("genero", l.getGenero());
            cv.put("isbn", l.getIsbn());
            cv.put("id", l.getId());
            cv.put("nombre", l.getNom());
            cv.put("imagen", l.getImg());
            cv.put("usuario", l.getUsuario() != null ? l.getUsuario().getDni() : null);

            db.insert("libro", null, cv);
        });

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
