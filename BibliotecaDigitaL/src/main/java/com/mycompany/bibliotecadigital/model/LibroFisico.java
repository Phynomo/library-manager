package com.mycompany.bibliotecadigital.model;

public class LibroFisico extends Recurso implements Prestable, Renovable {
    private boolean prestado;
    private Usuario usuarioactual;
    private int renovacionesusadas;
    private final int maxrenova = 2;

    public LibroFisico(String titulo, String idRecurso, String autor, String isbn, String editorial, int año, String categoria) {
        super(titulo, idRecurso, autor, isbn, editorial, año, categoria);
        this.prestado = false;
        this.renovacionesusadas = 0;
    }

    @Override
    public boolean estaDisponible() {
        return !prestado && disponible;
    }

    @Override
    public void prestar(Usuario usuario) {
        if (estaDisponible()) {
            this.prestado = true;
            this.usuarioactual = usuario;
            this.disponible = false;
        }
    }

    @Override
    public void devolver() {
        this.prestado = false;
        this.usuarioactual = null;
        this.disponible = true;
        this.renovacionesusadas = 0;
    }

    @Override
    public int getDiasPermitidos() {
        return usuarioactual != null ? usuarioactual.getDiasPrestamo() : 14;
    }

    @Override
    public boolean puedeRenovarse() {
        return prestado && renovacionesusadas < maxrenova;
    }

    @Override
    public void renovar() {
        if (puedeRenovarse()) {
            renovacionesusadas++;
        }
    }

    @Override
    public int getRenovacionesRestantes() {
        return maxrenova - renovacionesusadas;
    }

   
    public boolean isPrestado() { return prestado; }
    public Usuario getusuarioactual() { return usuarioactual; }
}