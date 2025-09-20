package com.mycompany.bibliotecadigital.model;

import java.io.Serializable;
import java.util.Date;

public class EntradaEspera implements Serializable {
    private static final long serialVersionUID = 1L;
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