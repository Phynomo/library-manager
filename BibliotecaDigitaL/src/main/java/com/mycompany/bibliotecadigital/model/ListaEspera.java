package com.mycompany.bibliotecadigital.model;

import java.util.*;

public class ListaEspera {
    private Map<String, Queue<EntradaEspera>> listasPorRecurso;

    public ListaEspera() {
        this.listasPorRecurso = new HashMap<>();
    }

    public boolean agregarAEspera(String idRecurso, Usuario usuario) {
        listasPorRecurso.putIfAbsent(idRecurso, new LinkedList<>());
        Queue<EntradaEspera> cola = listasPorRecurso.get(idRecurso);

        // Verificar duplicados
        boolean yaExiste = cola.stream()
                .anyMatch(e -> e.getUsuario().equals(usuario));
        if (yaExiste) {
            return false;
        }

        cola.offer(new EntradaEspera(usuario, new Date()));
        return true;
    }

    public EntradaEspera siguienteEnEspera(String idRecurso) {
        Queue<EntradaEspera> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? cola.poll() : null;
    }

    public List<EntradaEspera> verListaEspera(String idRecurso) {
        System.out.println(idRecurso);
        System.out.println(listasPorRecurso);
        Queue<EntradaEspera> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? new ArrayList<>(cola) : Collections.emptyList();
    }

    public boolean removerDeEspera(String idRecurso, Usuario usuario) {
        Queue<EntradaEspera> cola = listasPorRecurso.get(idRecurso);
        return cola != null && cola.removeIf(e -> e.getUsuario().equals(usuario));
    }

    public int getTamañoLista(String idRecurso) {
        Queue<EntradaEspera> cola = listasPorRecurso.get(idRecurso);
        return cola != null ? cola.size() : 0;
    }

    public Map<String, Integer> getEstadisticasEspera() {
        Map<String, Integer> estadisticas = new HashMap<>();
        for (Map.Entry<String, Queue<EntradaEspera>> entry : listasPorRecurso.entrySet()) {
            estadisticas.put(entry.getKey(), entry.getValue().size());
        }
        return estadisticas;
    }
}