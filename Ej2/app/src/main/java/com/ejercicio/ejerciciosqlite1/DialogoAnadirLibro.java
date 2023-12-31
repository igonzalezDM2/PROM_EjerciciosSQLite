package com.ejercicio.ejerciciosqlite1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoAnadirLibro extends DialogFragment {

    Libro libro;

    public DialogoAnadirLibro() {

    }

    public DialogoAnadirLibro(Libro libro) {
        this.libro = libro;
    }
    private OnDialogoConfirmacionListener listener;

    EditText etNombre, etIsbn, etGenero, etAsignado;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_anadir_libro, null);
        builder.setView(view);

        etNombre = view.findViewById(R.id.etNombre);
        etIsbn = view.findViewById(R.id.etIsbn);
        etGenero = view.findViewById(R.id.etGenero);
        etAsignado = view.findViewById(R.id.etAsignado);

        if (libro == null) {
            libro = new Libro();
        } else {
            etNombre.setText(libro.getNom() != null ? libro.getNom() : "");
            etIsbn.setText(libro.getIsbn() != null ? libro.getIsbn() : "");
            etGenero.setText(libro.getGenero() != null ? libro.getGenero() : "");
            etAsignado.setText(libro.getUsuario() != null && libro.getUsuario().getDni() != null ? libro.getUsuario().getDni() : "");
        }


        builder.setPositiveButton("Añadir", (dialog, which) -> {
            listener.onPossitiveButtonClick(libro
                    .setGenero(etGenero.getText().toString())
                    .setNom(etNombre.getText().toString())
                    .setIsbn(etIsbn.getText().toString())
                    .setUsuario(new Usuario().setDni(etAsignado.getText().toString())));
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.cancel();
        });

        Dialog dialogo =  builder.create();
        dialogo.setCancelable(false);
        dialogo.setCanceledOnTouchOutside(false);
        return dialogo;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnDialogoConfirmacionListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" no implementó OnDialogoConfirmacionListener");
        }
    }
}
