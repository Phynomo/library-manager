package com.mycompany.bibliotecadigital.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

public class Prestamo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuario;
    private Recurso recurso;
    private Date fechaPrestamo;
    private Date fechaVencimiento;
    private Date fechaDevolucion;
    private boolean activo;
    private double multa;

    public Prestamo(Usuario usuario, Recurso recurso) {
        this.usuario = usuario;
        this.recurso = recurso;
        this.fechaPrestamo = new Date();
        this.activo = true;
        this.multa = 0.0;
        calcularFechaVencimiento();
    }

    private void calcularFechaVencimiento() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaPrestamo);
        cal.add(Calendar.DAY_OF_MONTH, usuario.getDiasPrestamo());
        this.fechaVencimiento = cal.getTime();
    }

    public void devolver() {
        this.fechaDevolucion = new Date();
        this.activo = false;
        
        // Calcular multa si está vencido
        if (estaVencido()) {
            int diasRetraso = getDiasRetraso();
            this.multa = usuario.calcularMulta(diasRetraso);
        }
    }

    public boolean estaVencido() {
        return activo && new Date().after(fechaVencimiento);
    }

    public int getDiasRetraso() {
        if (!activo || !estaVencido()) return 0;
        long diferenciaMilis = new Date().getTime() - fechaVencimiento.getTime();
        return (int) (diferenciaMilis / (1000 * 60 * 60 * 24));
    }

    // Getters
    public Usuario getUsuario() { return usuario; }
    public Recurso getRecurso() { return recurso; }
    public Date getFechaPrestamo() { return fechaPrestamo; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public boolean isActivo() { return activo; }
    public double getMulta() { return multa; }
    
    // Setters para casos especiales
    public void setMulta(double multa) { this.multa = multa; }
}