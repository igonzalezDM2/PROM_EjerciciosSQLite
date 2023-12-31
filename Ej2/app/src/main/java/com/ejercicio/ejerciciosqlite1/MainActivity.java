package com.ejercicio.ejerciciosqlite1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements OnDialogoConfirmacionListener {

    private ListView lvContactos;
    private Button btnAnadir, btnUsuarios;
    private EditText etSearch;

    private AdaptadorLibros adaptador;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvContactos = findViewById(R.id.lvContactos);
        btnAnadir = findViewById(R.id.btnAnadir);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        etSearch = findViewById(R.id.etSearch);

        adaptador = new AdaptadorLibros(this, obtenerLibros());
        lvContactos.setAdapter(adaptador);


        btnAnadir.setOnClickListener(v -> {
            DialogoAnadirLibro dialogo = new DialogoAnadirLibro();
            dialogo.show(fragmentManager, "AÑADIR CONTACTO");
        });

        btnUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(this, UsuariosActivity.class);
            activityResultLauncher.launch(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarLista(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected List<Libro> obtenerLibros() {
        return obtenerLibros("");
    }
    protected List<Libro> obtenerLibros(String search) {
        List<Libro> libros = new LinkedList<>();
        operarBD(db -> {
            Cursor cur = db.rawQuery("SELECT " +
                    "libro.id, " +
                    "libro.nombre as nombrelibro, " +
                    "libro.imagen as imagenlibro, " +
                    "libro.genero, " +
                    "libro.isbn, " +
                    "usuario.dni, " +
                    "usuario.nombre as nombreusuario, " +
                    "usuario.imagen as imagenusuario, " +
                    "usuario.numero " +

                    " FROM libro LEFT JOIN usuario ON usuario.dni = libro.usuario WHERE LOWER(nombrelibro) LIKE ?", new String[]{search != null ? "%" + search.toLowerCase() +"%" : "%"});

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
        });
        return libros;
    }

    protected void operarBD(Consumer<SQLiteDatabase> callback) {
        ContactosSQLiteHelper helper = new ContactosSQLiteHelper(this, "DBUsuarios", null, 1);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            callback.accept(db);
        }
    }

    @Override
    public <T> void onPossitiveButtonClick(T objeto) {
        Libro libro = (Libro) objeto;
        operarBD(db -> {
            ContentValues cv = new ContentValues();
            cv.put("nombre", libro.getNom());
            cv.put("isbn", libro.getIsbn());
            cv.put("genero", libro.getGenero());
            if (libro.getUsuario() != null && libro.getUsuario().getDni() != null && !libro.getUsuario().getDni().isEmpty()) {
                cv.put("usuario",libro.getUsuario().getDni());
            }

            long ins = -1;

            try {
                if (libro.getId() != null) {
                    ins = db.update("libro", cv, "id = ?", new String[] {Integer.toString(libro.getId())});
                } else {
                    ins = db.insert("libro", null, cv);
                }


                if (ins > 0) {
                    actualizarLista();
                }
            } catch (SQLiteConstraintException e) {
                Toast toast = Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT);
                toast.show();

            }

        });
    }

    protected void actualizarLista() {
        actualizarLista("");
    }
    protected void limpiarBusqueda() {
        etSearch.setText("");
//        actualizarLista("");
    }

    protected void actualizarLista(String search) {
        adaptador.clear();
        adaptador.addAll(obtenerLibros(search));
    }

    @Override
    public void onNegativeButtonClick() {

    }


    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> { //NO SE HACE NADA
//                if (Activity.RESULT_OK == result.getResultCode()) {
//                    Intent intent = result.getData();
//                }
            });
}