package com.mycompany.bibliotecadigital.model;

public class Profesor extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String departamento;

    public Profesor(String nombre, String email, String identificacion, String departamento) {
        super(nombre, email, identificacion, 5, 30); // 5 libros, 30 días según instrucciones
        this.departamento = departamento;
    }

    @Override
    public double calcularMulta(int diasRetraso) {
        // Multa de $0.50 por día para profesores
        return diasRetraso * 0.5;
    }

    @Override
    public boolean puedeRenovar() {
        // Los profesores siempre pueden renovar
        return true;
    }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}