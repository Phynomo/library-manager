package com.mycompany.bibliotecadigital.model;

import java.io.Serializable;

public abstract class Recurso implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String titulo;
    protected String idRecurso;
    protected String autor;
    protected String isbn;
    protected String editorial;
    protected int año;
    protected String categoria;
    protected boolean disponible;

    public Recurso(String titulo, String idRecurso, String autor, String isbn, String editorial, int año, String categoria) {
        this.titulo = titulo;
        this.idRecurso = idRecurso;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.año = año;
        this.categoria = categoria;
        this.disponible = true;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getIdRecurso() { return idRecurso; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getEditorial() { return editorial; }
    public int getAño() { return año; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }

   
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Recurso recurso = (Recurso) obj;
        return idRecurso.equals(recurso.idRecurso);
    }
    
    @Override
    public int hashCode() {
        return idRecurso.hashCode();
    }
    
    @Override
    public String toString() {
        return titulo + " (" + idRecurso + ")";
    }
}