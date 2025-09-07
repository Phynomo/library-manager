package com.mycompany.bibliotecadigital.persistencia;

import com.mycompany.bibliotecadigital.model.Prestamo;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ArchivoPrestamos {
    private static final String ARCHIVO_PRESTAMOS = "prestamos.dat";
    
    public static void guardarPrestamos(List<Prestamo> prestamos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_PRESTAMOS))) {
            oos.writeObject(prestamos);
            System.out.println("Préstamos guardados en " + ARCHIVO_PRESTAMOS);
        } catch (IOException e) {
            System.err.println("Error al guardar préstamos: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Prestamo> cargarPrestamos() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_PRESTAMOS))) {
            return (List<Prestamo>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de préstamos no encontrado, creando lista nueva");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar préstamos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}