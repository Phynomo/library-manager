package com.mycompany.bibliotecadigital.persistencia;

import com.mycompany.bibliotecadigital.model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVManager {
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
    public static boolean exportarUsuarios(List<Usuario> usuarios, String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
           
            pw.println("ID,Nombre,Email,Tipo,Carrera/Departamento/Puesto,LimiteLibros,DiasPrestamo");
            
            for (Usuario usuario : usuarios) {
                String tipo = usuario.getClass().getSimpleName();
                String extra = "";
                
                if (usuario instanceof Estudiante) {
                    extra = ((Estudiante) usuario).getcarrera();
                } else if (usuario instanceof Profesor) {
                    extra = ((Profesor) usuario).getDepartamento();
                } else if (usuario instanceof Administrativo) {
                    extra = ((Administrativo) usuario).getPuesto();
                }
                
                pw.printf("%s,%s,%s,%s,%s,%d,%d%n",
                    escaparCSV(usuario.getIdentificacion()),
                    escaparCSV(usuario.getNombre()),
                    escaparCSV(usuario.getEmail()),
                    tipo,
                    escaparCSV(extra),
                    usuario.getLimitePrestamos(),
                    usuario.getDiasPrestamo()
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exportando usuarios: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean exportarRecursos(List<Recurso> recursos, String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
         
            pw.println("ID,Titulo,Autor,ISBN,Editorial,Año,Categoria,Tipo,Disponible,InfoAdicional");
            
            for (Recurso recurso : recursos) {
                String tipo = recurso.getClass().getSimpleName();
                String infoAdicional = "";
                
                if (recurso instanceof LibroDigital) {
                    LibroDigital ld = (LibroDigital) recurso;
                    infoAdicional = String.format("Formato:%s;Tamaño:%.2fMB", 
                        ld.getformato(), ld.gettamañoMB());
                } else if (recurso instanceof Audiobook) {
                    Audiobook ab = (Audiobook) recurso;
                    infoAdicional = String.format("Narrador:%s;Duracion:%dmin", 
                        ab.getnarrador(), ab.getduracionmin());
                }
                
                pw.printf("%s,%s,%s,%s,%s,%d,%s,%s,%s,%s%n",
                    escaparCSV(recurso.getIdRecurso()),
                    escaparCSV(recurso.getTitulo()),
                    escaparCSV(recurso.getAutor()),
                    escaparCSV(recurso.getIsbn()),
                    escaparCSV(recurso.getEditorial()),
                    recurso.getAño(),
                    escaparCSV(recurso.getCategoria()),
                    tipo,
                    recurso.isDisponible() ? "SI" : "NO",
                    escaparCSV(infoAdicional)
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exportando recursos: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean exportarPrestamos(List<Prestamo> prestamos, String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // Cabeceras
            pw.println("UsuarioID,RecursoID,FechaPrestamo,FechaVencimiento,FechaDevolucion,Estado,Multa");
            
            for (Prestamo prestamo : prestamos) {
                String fechaDevolucion = prestamo.getFechaDevolucion() != null ? 
                    FORMATO_FECHA.format(prestamo.getFechaDevolucion()) : "";
                String estado = prestamo.isActivo() ? "ACTIVO" : "DEVUELTO";
                
                pw.printf("%s,%s,%s,%s,%s,%s,%.2f%n",
                    escaparCSV(prestamo.getUsuario().getIdentificacion()),
                    escaparCSV(prestamo.getRecurso().getIdRecurso()),
                    FORMATO_FECHA.format(prestamo.getFechaPrestamo()),
                    FORMATO_FECHA.format(prestamo.getFechaVencimiento()),
                    fechaDevolucion,
                    estado,
                    prestamo.getMulta()
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exportando prestamos: " + e.getMessage());
            return false;
        }
    }
    
   
    public static List<String[]> importarCSV(String archivo) {
        List<String[]> datos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; 
                }
                
                String[] campos = parsearLineaCSV(linea);
                if (campos.length > 0) {
                    datos.add(campos);
                }
            }
        } catch (IOException e) {
            System.err.println("Error importando CSV: " + e.getMessage());
        }
        return datos;
    }
    

    private static String escaparCSV(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
    
    private static String[] parsearLineaCSV(String linea) {
        List<String> campos = new ArrayList<>();
        boolean dentroComillas = false;
        StringBuilder campoActual = new StringBuilder();
        
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            
            if (c == '"') {
                if (dentroComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    campoActual.append('"');
                    i++; // Saltar la siguiente comilla
                } else {
                    dentroComillas = !dentroComillas;
                }
            } else if (c == ',' && !dentroComillas) {
                campos.add(campoActual.toString());
                campoActual.setLength(0);
            } else {
                campoActual.append(c);
            }
        }
        
        campos.add(campoActual.toString());
        return campos.toArray(new String[0]);
    }
    
   
    public static String generarReporteCompleto(GestorBiblioteca gestor) {
        StringBuilder reporte = new StringBuilder();
        reporte.append("REPORTE COMPLETO DEL SISTEMA - ").append(new Date()).append("\n");
        reporte.append("=".repeat(60)).append("\n\n");
        
        reporte.append("ESTADISTICAS GENERALES:\n");
        reporte.append("- Usuarios registrados: ").append(gestor.getusuarios().size()).append("\n");
        reporte.append("- Recursos en catalogo: ").append(gestor.getcatalogo().size()).append("\n");
        reporte.append("- Préstamos activos: ").append(gestor.getprestamos().size()).append("\n");
        reporte.append("- Historial de prestamos: ").append(gestor.gethistorialpresta().size()).append("\n\n");
        
      
        Map<String, Integer> tiposUsuario = new HashMap<>();
        for (Usuario u : gestor.getusuarios()) {
            String tipo = u.getClass().getSimpleName();
            tiposUsuario.put(tipo, tiposUsuario.getOrDefault(tipo, 0) + 1);
        }
        
        reporte.append("USUARIOS POR TIPO:\n");
        tiposUsuario.forEach((tipo, cantidad) -> 
            reporte.append("- ").append(tipo).append(": ").append(cantidad).append("\n"));
        
        return reporte.toString();
    }
}