package com.ejercicio.ejerciciosqlite1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements OnDialogoConfirmacionListener {

    private ListView lvContactos;
    private Button btnAnadir;

    private AdaptadorContactos adaptador;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvContactos = findViewById(R.id.lvContactos);
        btnAnadir = findViewById(R.id.btnAnadir);
        List<Contacto> contactos = new LinkedList<>();
        operarBD(db -> {
            String[] campos = new String[] {"id", "nombre", "imagen", "numero"};
            try (Cursor cur = db.query("contacto", campos, null, null, null, null, null)) {
                while (cur.moveToNext()) {
                    int id = cur.getInt(0);
                    String nom = cur.getString(1);
                    byte[] imagen = cur.getBlob(2);
                    String numero = cur.getString(3);
                    contactos.add(new Contacto(id, imagen, nom, numero));
                }
            }
        });

        adaptador = new AdaptadorContactos(this, contactos);
        lvContactos.setAdapter(adaptador);


        btnAnadir.setOnClickListener(v -> {
            DialogoAnadir dialogo = new DialogoAnadir();
            dialogo.show(fragmentManager, "AÑADIR CONTACTO");
        });
    }

    protected void operarBD(Consumer<SQLiteDatabase> callback) {
        ContactosSQLiteHelper helper = new ContactosSQLiteHelper(this, "DBContactos", null, 1);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            callback.accept(db);
        }
    }

    @Override
    public void onPossitiveButtonClick(Contacto contacto) {
        operarBD(db -> {
            ContentValues cv = new ContentValues();
            cv.put("nombre", contacto.getNom());
            cv.put("numero", contacto.getTlf());
            long newId = db.insert("contacto", null, cv);
            if (newId > 0) {
                adaptador.add(contacto.setId((int) newId));
            }
        });
    }

    @Override
    public void onNegativeButtonClick() {

    }
}