package com.mycompany.bibliotecadigital.persistencia;

import com.mycompany.bibliotecadigital.model.Recurso;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ArchivoCatalogo {
    private static final String ARCHIVO_CATALOGO = "catalogo.dat";

    public static void guardarCatalogo(List<Recurso> catalogo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_CATALOGO))) {
            oos.writeObject(catalogo);
        } catch (IOException e) {
            System.err.println("Error al guardar catalogo: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Recurso> cargarCatalogo() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_CATALOGO))) {
            return (List<Recurso>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de catalogo, creando lista nueva");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar catalogo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}