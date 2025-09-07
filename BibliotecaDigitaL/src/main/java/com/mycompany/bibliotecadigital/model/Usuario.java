package com.mycompany.bibliotecadigital.model;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String nombre;
    protected String email;
    protected String identificacion;
    protected int limitePrestamos;
    protected int diasPrestamo;

    public Usuario(String nombre, String email, String identificacion, int limitePrestamos, int diasPrestamo) {
        this.nombre = nombre;
        this.email = email;
        this.identificacion = identificacion;
        this.limitePrestamos = limitePrestamos;
        this.diasPrestamo = diasPrestamo;
    }

    // Métodos abstractos según las instrucciones
    public abstract double calcularMulta(int diasRetraso);
    public abstract boolean puedeRenovar();

    // Getters
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getIdentificacion() { return identificacion; }
    public int getLimitePrestamos() { return limitePrestamos; }
    public int getDiasPrestamo() { return diasPrestamo; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    
    // Método equals para comparación en listas
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return identificacion.equals(usuario.identificacion);
    }
    
    @Override
    public int hashCode() {
        return identificacion.hashCode();
    }
}