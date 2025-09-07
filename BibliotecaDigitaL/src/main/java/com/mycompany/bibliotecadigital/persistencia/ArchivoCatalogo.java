package com.mycompany.bibliotecadigital.persistencia;

import com.mycompany.bibliotecadigital.model.Recurso;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ArchivoCatalogo {
    private static final String ARCHIVO_CATALOGO = "catalogo.txt";
    
    public static void guardarCatalogo(List<Recurso> catalogo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CATALOGO))) {
            pw.println("# Catálogo de Biblioteca Digital");
            pw.println("# Formato: ID|Título|Autor|ISBN|Editorial|Año|Categoría|Tipo");
            
            for (Recurso recurso : catalogo) {
                String linea = String.format("%s|%s|%s|%s|%s|%d|%s|%s",
                    recurso.getIdRecurso(),
                    recurso.getTitulo(),
                    recurso.getAutor(),
                    recurso.getIsbn(),
                    recurso.getEditorial(),
                    recurso.getAño(),
                    recurso.getCategoria(),
                    recurso.getClass().getSimpleName()
                );
                pw.println(linea);
            }
            System.out.println("Catálogo guardado en " + ARCHIVO_CATALOGO);
        } catch (IOException e) {
            System.err.println("Error al guardar catálogo: " + e.getMessage());
        }
    }
    
    public static List<String> cargarCatalogo() {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CATALOGO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.startsWith("#") && !linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de catálogo no encontrado");
        } catch (IOException e) {
            System.err.println("Error al cargar catálogo: " + e.getMessage());
        }
        return lineas;
    }
}