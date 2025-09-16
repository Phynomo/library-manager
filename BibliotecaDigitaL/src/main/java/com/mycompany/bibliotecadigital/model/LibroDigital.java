package com.mycompany.bibliotecadigital.model;

public class LibroDigital extends Recurso {
    private String urlDescarga;
    private String formato;
    private double tamañoMB;

    public LibroDigital(String titulo, String idRecurso, String autor, String isbn,
                        String editorial, int año, String categoria,
                        String urlDescarga, String formato, double tamañoMB) {
        super(titulo, idRecurso, autor, isbn, editorial, año, categoria);
        this.urlDescarga = urlDescarga;
        this.formato = formato;
        this.tamañoMB = tamañoMB;
    }

    // Getters
    public String getUrlDescarga() { return urlDescarga; }
    public String getFormato() { return formato; }
    public double getTamañoMB() { return tamañoMB; }

    // Setters
    public void setUrlDescarga(String urlDescarga) { this.urlDescarga = urlDescarga; }
    public void setFormato(String formato) { this.formato = formato; }
    public void setTamañoMB(double tamañoMB) { this.tamañoMB = tamañoMB; }

    // Método opcional para verificar si se puede descargar
    public boolean puedeDescargar() {
        return disponible;
    }
}
