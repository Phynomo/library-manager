package com.mycompany.bibliotecadigital.model;

public class Estudiante extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String carrera;

    public Estudiante(String nombre, String email, String identificacion, String carrera) {
        super(nombre, email, identificacion, 3, 14); 
        this.carrera = carrera;
    }

    @Override
    public double calcularMulta(int diasRetraso) {
    
        return diasRetraso * 1.0;
    }

    @Override
    public boolean puedeRenovar() {
     
        return true;
    }

    public String getcarrera() { return carrera; }
    public void setcarrera(String carrera) { this.carrera = carrera; }
}