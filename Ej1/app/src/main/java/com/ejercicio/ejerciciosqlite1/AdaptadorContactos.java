package com.ejercicio.ejerciciosqlite1;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

public class AdaptadorContactos extends ArrayAdapter<Contacto> {
    public AdaptadorContactos(@NonNull Context context, @NonNull List<Contacto> objects) {
        super(context, R.layout.activity_main, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contacto contacto = getItem(position);
        ViewHolder holder;

        View item = convertView;
        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.layout_contacto, null);
            holder = new ViewHolder();
            holder.img = item.findViewById(R.id.ivImg);
            holder.nom = item.findViewById(R.id.tvNombre);
            holder.tlf = item.findViewById(R.id.tvTlf);
            holder.btnLBorrar = item.findViewById(R.id.btnBorrar);
            holder.btnLBorrar.setOnClickListener(v -> {
                Context ctx = getContext();
                if (ctx instanceof MainActivity) {
                    ((MainActivity) ctx).operarBD(db -> {
                        db.delete("contacto", "id = ?", new String[]{Integer.toString(contacto.getId())});
                        this.remove(contacto);
                    });
                }
            });


            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        ImageView img = holder.img;
        byte[] datosImg = contacto.getImg();
        if (datosImg != null) {
            BitmapDrawable imagen = new BitmapDrawable(getContext().getResources(), BitmapFactory.decodeByteArray(datosImg, 0, datosImg.length));
            img.setImageDrawable(imagen);
        } else {
            img.setImageResource(R.drawable.contacto);
        }

        TextView nom = holder.nom;
        nom.setText(contacto.getNom());
        TextView tlf = holder.tlf;
        tlf.setText(contacto.getTlf());
        return item;
    }

    private static class ViewHolder {
        ImageView img;
        TextView nom, tlf;
        ImageButton btnLBorrar;
    }
}