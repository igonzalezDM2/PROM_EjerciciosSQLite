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

    String sqlCreate =
            "CREATE TABLE contacto (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " nombre TEXT, " +
                    " imagen BLOB, " +
                    "numero TEXT)";

    public ContactosSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] campos = new String[] {"id", "nombre", "imagen", "numero"};
        List<Contacto> contactos = new LinkedList<>();

        Cursor cur = db.query("contacto", campos, null, null, null, null, null);
        while (cur.moveToNext()) {
            int id = cur.getInt(0);
            String nom = cur.getString(1);
            byte[] imagen = cur.getBlob(2);
            String numero = cur.getString(3);
            contactos.add(new Contacto(id, imagen, nom, numero));
        }
        cur.close();
        db.execSQL("DROP TABLE IF EXISTS `contacto`");
        db.execSQL(sqlCreate);

        contactos.forEach(c -> {
            ContentValues cv = new ContentValues();
            cv.put("id", c.getId());
            cv.put("nombre", c.getNom());
            cv.put("imagen", c.getImg());
            cv.put("numero", c.getTlf());

            db.insert("contacto", null, cv);
        });

    }
}
