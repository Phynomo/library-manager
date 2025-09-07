package com.mycompany.bibliotecadigital.model;

public interface Prestable {
    boolean estaDisponible();
    void prestar(Usuario usuario);
    void devolver();
    int getDiasPermitidos();
}