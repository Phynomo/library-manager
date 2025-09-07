package com.mycompany.bibliotecadigital.model;

public class RevistaCientifica extends Recurso {
    private String issn;
    private int numero;
    private String volumen;

    public RevistaCientifica(String titulo, String idRecurso, String autor, String isbn, String editorial, int año, String categoria, String issn, int numero, String volumen) {
        super(titulo, idRecurso, autor, isbn, editorial, año, categoria);
        this.issn = issn;
        this.numero = numero;
        this.volumen = volumen;
    }

    public String getIssn() { return issn; }
    public int getNumero() { return numero; }
    public String getVolumen() { return volumen; }
    
    public String getReferencia() {
        return String.format("%s, Vol. %s, No. %d (%d)", titulo, volumen, numero, año);
    }
}