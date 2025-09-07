package com.mycompany.bibliotecadigital.model;

import com.mycompany.bibliotecadigital.persistencia.*;
import java.util.*;

public class GestorBiblioteca {
    private List<Usuario> usuarios;
    private List<Recurso> catalogo;
    private List<Prestamo> prestamos;
    private List<Prestamo> historialpresta;
    private ListaEspera listaespera;

    public GestorBiblioteca() {
        cargarDatos();
    }

    
    private void cargarDatos() {
        this.usuarios = ArchivoUsuarios.cargarUsuarios();
        this.prestamos = ArchivoPrestamos.cargarPrestamos();
        this.historialpresta = new ArrayList<>();
        this.listaespera = new ListaEspera();
        this.catalogo = new ArrayList<>();
        
        
        List<String> lineasCatalogo = ArchivoCatalogo.cargarCatalogo();
       
    }

    
    public boolean registrarUsuario(Usuario usuario) {
       
        for (Usuario u : usuarios) {
            if (u.getIdentificacion().equals(usuario.getIdentificacion())) {
                return false; 
            }
        }
        
        boolean agregado = usuarios.add(usuario);
        if (agregado) {
            ArchivoUsuarios.guardarUsuarios(usuarios); 
        }
        return agregado;
    }

    public Usuario buscarUsuario(String identificacion) {
        return usuarios.stream()
                .filter(u -> u.getIdentificacion().equals(identificacion))
                .findFirst()
                .orElse(null);
    }

  
    public boolean agregarRecurso(Recurso recurso) {
        
        for (Recurso r : catalogo) {
            if (r.getIdRecurso().equals(recurso.getIdRecurso())) {
                return false; 
            }
        }
        
        boolean agregado = catalogo.add(recurso);
        if (agregado) {
            ArchivoCatalogo.guardarCatalogo(catalogo); 
        }
        return agregado;
    }

    public List<Recurso> buscarRecursos(String criterio) {
        List<Recurso> resultados = new ArrayList<>();
        String criterioBajo = criterio.toLowerCase();
        
        for (Recurso r : catalogo) {
            if (r.getTitulo().toLowerCase().contains(criterioBajo) ||
                r.getAutor().toLowerCase().contains(criterioBajo) ||
                r.getCategoria().toLowerCase().contains(criterioBajo)) {
                resultados.add(r);
            }
        }
        return resultados;
    }

    
    public boolean realizarPrestamo(String idUsuario, String idRecurso) {
        Usuario usuario = buscarUsuario(idUsuario);
        Recurso recurso = buscarRecursoPorId(idRecurso);

        if (usuario == null || recurso == null) {
            return false;
        }

       
        long prestamosActivos = prestamos.stream()
                .filter(p -> p.isActivo() && p.getUsuario().equals(usuario))
                .count();

        if (prestamosActivos >= usuario.getLimitePrestamos()) {
            return false;
        }

       
        if (recurso instanceof Prestable) {
            Prestable prestable = (Prestable) recurso;
            if (prestable.estaDisponible()) {
                prestable.prestar(usuario);
                Prestamo prestamo = new Prestamo(usuario, recurso);
                prestamos.add(prestamo);
                
                
                ArchivoPrestamos.guardarPrestamos(prestamos);
                ArchivoCatalogo.guardarCatalogo(catalogo);
                
                return true;
            } else {
              
                listaespera.agregarAEspera(idRecurso, usuario);
                System.out.println("Recurso no disponible. Usuario agregado a lista de espera.");
                return false;
            }
        }
        return false;
    }

    public boolean procesarDevolucion(String idUsuario, String idRecurso) {
        Prestamo prestamo = prestamos.stream()
                .filter(p -> p.isActivo() && 
                           p.getUsuario().getIdentificacion().equals(idUsuario) &&
                           p.getRecurso().getIdRecurso().equals(idRecurso))
                .findFirst()
                .orElse(null);

        if (prestamo != null) {
            prestamo.devolver();
            if (prestamo.getRecurso() instanceof Prestable) {
                ((Prestable) prestamo.getRecurso()).devolver();
            }
            historialpresta.add(prestamo);
            
          
            ArchivoPrestamos.guardarPrestamos(prestamos);
            ArchivoCatalogo.guardarCatalogo(catalogo);
            
           
            procesarListaEspera(idRecurso);
            
            return true;
        }
        return false;
    }

    
    private void procesarListaEspera(String idRecurso) {
        Usuario siguienteUsuario = listaespera.siguienteEnEspera(idRecurso);
        if (siguienteUsuario != null) {
            System.out.println("Notificando a " + siguienteUsuario.getNombre() + 
                             " que el recurso " + idRecurso + " está disponible.");
        }
    }

    public List<Prestamo> getPrestamosUsuario(String idUsuario) {
        return prestamos.stream()
                .filter(p -> p.getUsuario().getIdentificacion().equals(idUsuario))
                .toList();
    }

    private Recurso buscarRecursoPorId(String id) {
        return catalogo.stream()
                .filter(r -> r.getIdRecurso().equals(id))
                .findFirst()
                .orElse(null);
    }

  
    public ListaEspera getListaEspera() { 
        return listaespera; 
    }
    
    public boolean agregarAListaEspera(String idRecurso, String idUsuario) {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario != null) {
            listaespera.agregarAEspera(idRecurso, usuario);
            return true;
        }
        return false;
    }
    
    public List<Usuario> verListaEsperaRecurso(String idRecurso) {
        return listaespera.verListaEspera(idRecurso);
    }
    

    public void guardartodosdatos() {
        ArchivoUsuarios.guardarUsuarios(usuarios);
        ArchivoCatalogo.guardarCatalogo(catalogo);
        ArchivoPrestamos.guardarPrestamos(prestamos);
    }

    
    public List<Usuario> getusuarios() { return usuarios; }
    public List<Recurso> getcatalogo() { return catalogo; }
    public List<Prestamo> getprestamos() { return prestamos; }
    public List<Prestamo> gethistorialpresta() { return historialpresta; }
}