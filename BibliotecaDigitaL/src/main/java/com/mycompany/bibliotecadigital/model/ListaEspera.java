package com.mycompany.bibliotecadigital.model;

import java.util.*;

public class ListaEspera {
    private Map<String, Queue<Usuario>> listasPorRecurso;
    
    public ListaEspera() {
        this.listasPorRecurso = new HashMap<>();
    }
    
    public void agregarAEspera(String idRecurso, Usuario usuario) {
        // Crear cola si no existe
        listasPorRecurso.putIfAbsent(idRecurso, new LinkedList<>());
        
        Queue<Usuario> cola = listasPorRecurso.get(idRecurso);
        
        // Verificar que el usuario no esté ya en la lista
        if (!cola.contains(usuario)) {
            cola.offer(usuario);
            System.out.println("Usuario " + usuario.getNombre() + " agregado a lista de espera para recurso " + idRecurso);
        }
    }
    
    public Usuario siguienteEnEspera(String idRecurso) {
        Queue<Usuario> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? cola.poll() : null;
    }
    
    public List<Usuario> verListaEspera(String idRecurso) {
        Queue<Usuario> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? new ArrayList<>(cola) : new ArrayList<>();
    }
    
    public int getTamañoLista(String idRecurso) {
        Queue<Usuario> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? cola.size() : 0;
    }
    
    public boolean removerDeEspera(String idRecurso, Usuario usuario) {
        Queue<Usuario> cola = listasPorRecurso.get(idRecurso);
        return cola != null && cola.remove(usuario);
    }
    
    public Map<String, Integer> getEstadisticasEspera() {
        Map<String, Integer> estadisticas = new HashMap<>();
        for (Map.Entry<String, Queue<Usuario>> entry : listasPorRecurso.entrySet()) {
            estadisticas.put(entry.getKey(), entry.getValue().size());
        }
        return estadisticas;
    }
}