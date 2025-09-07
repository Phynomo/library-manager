package com.mycompany.bibliotecadigital.model;

public class Audiobook extends Recurso {
    private String narrador;
    private int duracionmin;

    public Audiobook(String titulo, String idRecurso, String autor, String isbn, String editorial, int año, String categoria, String narrador, int duracionmin) {
        super(titulo, idRecurso, autor, isbn, editorial, año, categoria);
        this.narrador = narrador;
        this.duracionmin = duracionmin;
    }

    public String getnarrador() { return narrador; }
    public int getduracionmin() { return duracionmin; }
    
    public String getduracionformateada() {
        int horas = duracionmin / 60;
        int minutos = duracionmin % 60;
        return String.format("%d:%02d", horas, minutos);
    }
}
