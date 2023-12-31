package com.ejercicio.ejerciciosqlite1;

public interface OnDialogoConfirmacionListener {
    <T> void onPossitiveButtonClick(T objeto); //Eventos Botón Positivos
    void onNegativeButtonClick(); //Eventos Botón Negativo
}
