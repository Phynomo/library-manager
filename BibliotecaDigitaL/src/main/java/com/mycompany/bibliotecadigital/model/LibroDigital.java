package com.mycompany.bibliotecadigital.model;

public class LibroDigital extends Recurso {
    private String urldescarga;
    private String formato; 
    private double tamañoMB;

    public LibroDigital(String titulo, String idRecurso, String autor, String isbn, String editorial, int año, String categoria, String urlDescarga, String formato, double tamañoMB) {
        super(titulo, idRecurso, autor, isbn, editorial, año, categoria);
        this.urldescarga = urlDescarga;
        this.formato = formato;
        this.tamañoMB = tamañoMB;
    }

   
    public boolean puededescargar() {
        return disponible;
    }

    public String geturldescarga() { return urldescarga; }
    public String getformato() { return formato; }
    public double gettamañoMB() { return tamañoMB; }
}