package com.mycompany.bibliotecadigital.model;

public interface Renovable {
    boolean puedeRenovarse();
    void renovar();
    int getRenovacionesRestantes();
}