package com.mycompany.bibliotecadigital.model;

import java.util.Date;

public class EntradaEspera {
    private Usuario usuario;
    private Date fechaSolicitud;

    public EntradaEspera(Usuario usuario, Date fechaSolicitud) {
        this.usuario = usuario;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    @Override
    public String toString() {
        return usuario.getNombre() + " (desde " + fechaSolicitud + ")";
    }
}