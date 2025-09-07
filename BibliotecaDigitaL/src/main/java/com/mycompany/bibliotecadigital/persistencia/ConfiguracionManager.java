package com.mycompany.bibliotecadigital.persistencia;

import java.io.*;
import java.util.Properties;

public class ConfiguracionManager {
    private static final String ARCHIVO_CONFIG = "configuracion.properties";
    private static Properties propiedades = new Properties();
    
    // Valores por defecto
    private static final String[] CONFIGURACION_DEFAULT = {
        "sistema.nombre=Sistema de Biblioteca Digital",
        "sistema.version=1.0",
        "limite.estudiante.libros=3",
        "limite.profesor.libros=5", 
        "limite.administrativo.libros=2",
        "dias.prestamo.estudiante=14",
        "dias.prestamo.profesor=30",
        "dias.prestamo.administrativo=7",
        "multa.estudiante=1.00",
        "multa.profesor=0.50",
        "multa.administrativo=2.00",
        "max.renovaciones=2",
        "backup.automatico=true",
        "backup.intervalo.horas=24",
        "email.notificaciones=biblioteca@universidad.edu"
    };

    static {
        cargarConfiguracion();
    }

    public static void cargarConfiguracion() {
        try (FileInputStream fis = new FileInputStream(ARCHIVO_CONFIG)) {
            propiedades.load(fis);
            System.out.println("Configuración cargada desde " + ARCHIVO_CONFIG);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de configuración no encontrado, creando uno nuevo...");
            crearConfiguracionDefault();
        } catch (IOException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            crearConfiguracionDefault();
        }
    }

    private static void crearConfiguracionDefault() {
        propiedades.clear();
        for (String config : CONFIGURACION_DEFAULT) {
            String[] partes = config.split("=", 2);
            propiedades.setProperty(partes[0], partes[1]);
        }
        guardarConfiguracion();
    }

    public static void guardarConfiguracion() {
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_CONFIG)) {
            propiedades.store(fos, "Configuración del Sistema de Biblioteca Digital");
            System.out.println("Configuración guardada en " + ARCHIVO_CONFIG);
        } catch (IOException e) {
            System.err.println("Error al guardar configuración: " + e.getMessage());
        }
    }

    public static String getPropiedad(String clave) {
        return propiedades.getProperty(clave, "");
    }

    public static String getPropiedad(String clave, String valorDefault) {
        return propiedades.getProperty(clave, valorDefault);
    }

    public static int getPropiedadInt(String clave, int valorDefault) {
        try {
            return Integer.parseInt(propiedades.getProperty(clave, String.valueOf(valorDefault)));
        } catch (NumberFormatException e) {
            return valorDefault;
        }
    }

    public static double getPropiedadDouble(String clave, double valorDefault) {
        try {
            return Double.parseDouble(propiedades.getProperty(clave, String.valueOf(valorDefault)));
        } catch (NumberFormatException e) {
            return valorDefault;
        }
    }

    public static boolean getPropiedadBoolean(String clave, boolean valorDefault) {
        return Boolean.parseBoolean(propiedades.getProperty(clave, String.valueOf(valorDefault)));
    }

    public static void setPropiedad(String clave, String valor) {
        propiedades.setProperty(clave, valor);
    }

    public static Properties getPropiedades() {
        return new Properties(propiedades);
    }
}