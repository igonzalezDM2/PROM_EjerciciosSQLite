package com.ejercicio.ejerciciosqlite1;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdaptadorLibros extends ArrayAdapter<Libro> {
    public AdaptadorLibros(@NonNull Context context, @NonNull List<Libro> objects) {
        super(context, R.layout.activity_main, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Libro libro = getItem(position);
        ViewHolder holder;

        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.layout_libro, null);
            holder = new ViewHolder();
            holder.img = item.findViewById(R.id.ivImg);
            holder.nom = item.findViewById(R.id.tvNombre);
            holder.isbn = item.findViewById(R.id.tvIsbn);
            holder.genero = item.findViewById(R.id.tvGenero);
            holder.asignado = item.findViewById(R.id.tvAsignado);
            holder.btnLBorrar = item.findViewById(R.id.btnBorrar);
            holder.btnLBorrar.setOnTouchListener((a1,a2) -> {
                if(a2.getAction() == MotionEvent.ACTION_DOWN) {
                    Context ctx = getContext();
                    if (ctx instanceof MainActivity) {
                        ((MainActivity) ctx).operarBD(db -> {
                            int del = db.delete("libro", "id = ?", new String[]{Integer.toString(libro.getId())});
                            if (del > 0) {
                                ((MainActivity) ctx).limpiarBusqueda();
                                ((MainActivity) ctx).actualizarLista();
                            }
                        });
                    }
                }
                return true;
            });

            item.setOnTouchListener((a1,a2) -> {
                if(a2.getAction() == MotionEvent.ACTION_DOWN) {
                    DialogoAnadirLibro dialogo = new DialogoAnadirLibro(libro);
                    MainActivity contexto = (MainActivity)getContext();
                    dialogo.show(contexto.fragmentManager, "MODIFICAR CONTACTO");
                }
                return true;
            });


            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        ImageView img = holder.img;
        byte[] datosImg = libro.getImg();
        if (datosImg != null) {
            BitmapDrawable imagen = new BitmapDrawable(getContext().getResources(), BitmapFactory.decodeByteArray(datosImg, 0, datosImg.length));
            img.setImageDrawable(imagen);
        } else {
            img.setImageResource(R.drawable.libro);
        }

        TextView nom = holder.nom;
        nom.setText(libro.getNom());
        TextView isbn = holder.isbn;
        isbn.setText(libro.getIsbn());
        TextView genero = holder.genero;
        genero.setText(libro.getGenero());
        TextView asignado = holder.asignado;
        asignado.setText(libro.getUsuario() != null && libro.getUsuario().getDni() != null ? libro.getUsuario().getDni() : "");
        return item;
    }

    private static class ViewHolder {
        ImageView img;
        TextView nom, genero, isbn, asignado;
        ImageButton btnLBorrar;

    }
}