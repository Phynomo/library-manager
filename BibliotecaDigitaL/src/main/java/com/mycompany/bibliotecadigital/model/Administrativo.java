package com.mycompany.bibliotecadigital.model;

public class Administrativo extends Usuario {
    private static final long serialVersionUID = 1L;
    
    private String puesto;

    public Administrativo(String nombre, String email, String identificacion, String puesto) {
        super(nombre, email, identificacion, 2, 7); 
        this.puesto = puesto;
    }

    @Override
    public double calcularMulta(int diasRetraso) {
       
        return diasRetraso * 2.0;
    }

    @Override
    public boolean puedeRenovar() {
      
        return true;
    }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
}