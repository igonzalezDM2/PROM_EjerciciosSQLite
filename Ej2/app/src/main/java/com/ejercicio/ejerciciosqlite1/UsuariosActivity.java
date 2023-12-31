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

public class UsuariosActivity extends AppCompatActivity implements OnDialogoConfirmacionListener {

    private ListView lvContactos;
    private Button btnAnadir;

    private AdaptadorUsuarios adaptador;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        lvContactos = findViewById(R.id.lvContactos);
        btnAnadir = findViewById(R.id.btnAnadir);
        List<Usuario> contactos = new LinkedList<>();
        operarBD(db -> {
            String[] campos = new String[] {"dni", "nombre", "imagen", "numero"};
            try (Cursor cur = db.query("usuario", campos, null, null, null, null, null)) {
                while (cur.moveToNext()) {
                    String dni = cur.getString(0);
                    String nom = cur.getString(1);
                    byte[] imagen = cur.getBlob(2);
                    String numero = cur.getString(3);
                    contactos.add(new Usuario(dni, imagen, nom, numero));
                }
            }
        });

        adaptador = new AdaptadorUsuarios(this, contactos);
        lvContactos.setAdapter(adaptador);


        btnAnadir.setOnClickListener(v -> {
            DialogoAnadir dialogo = new DialogoAnadir();
            dialogo.show(fragmentManager, "AÑADIR CONTACTO");
        });
    }

    protected void operarBD(Consumer<SQLiteDatabase> callback) {
        ContactosSQLiteHelper helper = new ContactosSQLiteHelper(this, "DBUsuarios", null, 1);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            callback.accept(db);
        }
    }


    @Override
    public <T> void onPossitiveButtonClick(T objeto) {
        Usuario contacto = (Usuario) objeto;
        operarBD(db -> {
            ContentValues cv = new ContentValues();
            cv.put("nombre", contacto.getNom());
            cv.put("numero", contacto.getTlf());
            cv.put("dni", contacto.getDni());
            long ins = db.insert("usuario", null, cv);

            if (ins != -1) {
                adaptador.add(contacto);
            }

        });
    }

    @Override
    public void onNegativeButtonClick() {

    }
}