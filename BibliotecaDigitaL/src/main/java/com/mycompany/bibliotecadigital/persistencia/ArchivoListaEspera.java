package com.mycompany.bibliotecadigital.persistencia;
import com.mycompany.bibliotecadigital.model.ListaEspera;
import java.io.*;

public class ArchivoListaEspera {
    private static final String ARCHIVO_ESPERA = "lista_espera.dat";

    public static void guardarListaEspera(ListaEspera listaEspera) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_ESPERA))) {
            oos.writeObject(listaEspera);
        } catch (IOException e) {
            System.err.println("Error al guardar listado espera: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ListaEspera cargarListaEspera() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_ESPERA))) {
            return (ListaEspera) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de espera no encontrado, creando lista nueva");
            return new ListaEspera();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar lista espera: " + e.getMessage());
            return new ListaEspera();
        }
    }
}
