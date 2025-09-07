package com.mycompany.bibliotecadigital.persistencia;

import com.mycompany.bibliotecadigital.model.Usuario;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class ArchivoUsuarios {
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    
    public static void guardarUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
            System.out.println("Usuarios guardados en " + ARCHIVO_USUARIOS);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Usuario> cargarUsuarios() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_USUARIOS))) {
            return (List<Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de usuarios no encontrado, creando lista nueva");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}