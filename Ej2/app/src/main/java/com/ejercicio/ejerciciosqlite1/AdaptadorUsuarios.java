package com.ejercicio.ejerciciosqlite1;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdaptadorUsuarios extends ArrayAdapter<Usuario> {
    public AdaptadorUsuarios(@NonNull Context context, @NonNull List<Usuario> objects) {
        super(context, R.layout.activity_main, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Usuario usuario = getItem(position);
        ViewHolder holder;

        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.layout_contacto, null);
            holder = new ViewHolder();
            holder.img = item.findViewById(R.id.ivImg);
            holder.nom = item.findViewById(R.id.tvNombre);
            holder.tlf = item.findViewById(R.id.tvTlf);
            holder.dni = item.findViewById(R.id.tvDni);
            holder.btnLBorrar = item.findViewById(R.id.btnBorrar);
            holder.btnLBorrar.setOnClickListener(v -> {
                Context ctx = getContext();
                if (ctx instanceof UsuariosActivity) {
                    ((UsuariosActivity) ctx).operarBD(db -> {
                        int del = db.delete("usuario", "dni = ?", new String[]{usuario.getDni()});
                        if (del > 0) {
                            this.remove(usuario);
                        }
                    });
                }
            });


            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        ImageView img = holder.img;
        byte[] datosImg = usuario.getImg();
        if (datosImg != null) {
            BitmapDrawable imagen = new BitmapDrawable(getContext().getResources(), BitmapFactory.decodeByteArray(datosImg, 0, datosImg.length));
            img.setImageDrawable(imagen);
        } else {
            img.setImageResource(R.drawable.persona);
        }

        TextView nom = holder.nom;
        nom.setText(usuario.getNom());
        TextView tlf = holder.tlf;
        tlf.setText(usuario.getTlf());
        TextView dni = holder.dni;
        dni.setText(usuario.getDni());
        return item;
    }

    private static class ViewHolder {
        ImageView img;
        TextView nom, tlf, dni;
        ImageButton btnLBorrar;
    }
}