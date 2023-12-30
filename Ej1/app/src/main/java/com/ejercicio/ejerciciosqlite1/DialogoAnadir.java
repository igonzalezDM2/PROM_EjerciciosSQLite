package com.ejercicio.ejerciciosqlite1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoAnadir extends DialogFragment {

    private OnDialogoConfirmacionListener listener;

    EditText etNombre, etNumero;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_anadir, null);
        builder.setView(view);

        etNombre = view.findViewById(R.id.etNombre);
        etNumero = view.findViewById(R.id.etNumero);


        builder.setPositiveButton("Añadir", (dialog, which) -> {
            listener.onPossitiveButtonClick(new Contacto().setNom(etNombre.getText().toString()).setTlf(etNumero.getText().toString()));
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
