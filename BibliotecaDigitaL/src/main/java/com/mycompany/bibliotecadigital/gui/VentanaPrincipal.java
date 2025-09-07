package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private GestorBiblioteca gestor;
    private JTabbedPane pestañas;
    private PanelUsuarios panelUsuarios;
    private PanelRecursos panelRecursos;
    private PanelPrestamos panelPrestamos;

    public VentanaPrincipal() {
        this.gestor = new GestorBiblioteca();
        inicializar();
        cargarDatosPrueba();
    }

    private void inicializar() {
        setTitle("Sistema de Gestion de Biblioteca Digital");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JMenuBar menuBar = new JMenuBar();
        
        
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        
        
        JMenu menuGestion = new JMenu("Gestion");
        JMenuItem itemEstadisticas = new JMenuItem("Ver Estadisticas");
        itemEstadisticas.addActionListener(e -> mostrarEstadisticas());
        menuGestion.add(itemEstadisticas);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuGestion);
        setJMenuBar(menuBar);

      
        pestañas = new JTabbedPane();
        
        panelUsuarios = new PanelUsuarios(gestor);
        panelRecursos = new PanelRecursos(gestor);  
        panelPrestamos = new PanelPrestamos(gestor);
        
        pestañas.addTab("👥 Usuarios", panelUsuarios);
        pestañas.addTab("📚 Recursos", panelRecursos);
        pestañas.addTab("📋 Préstamos", panelPrestamos);
    
        JPanel panelEstadisticas = crearPanelEstadisticas();
     
        add(panelEstadisticas, BorderLayout.NORTH);
        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Estadísticas en Tiempo Real"));
        panel.setBackground(new Color(240, 248, 255));
        
        JLabel lblUsuarios = new JLabel("Usuarios: " + gestor.getusuarios().size());
        JLabel lblRecursos = new JLabel("Recursos: " + gestor.getcatalogo().size());  
        JLabel lblPrestamos = new JLabel("Prestamos Activos: " + gestor.getprestamos().size());
        
        panel.add(lblUsuarios);
        panel.add(new JLabel(" | "));
        panel.add(lblRecursos);
        panel.add(new JLabel(" | "));
        panel.add(lblPrestamos);
        
        return panel;
    }

    private void mostrarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADISTICAS DEL SISTEMA ===\n\n");
        stats.append("📊 Usuarios Registrados: ").append(gestor.getusuarios().size()).append("\n");
        stats.append("📚 Recursos en Catálogo: ").append(gestor.getcatalogo().size()).append("\n");
        stats.append("📋 Prestamos Activos: ").append(gestor.getprestamos().size()).append("\n");
        stats.append("📜 Historial de Préstamos: ").append(gestor.gethistorialpresta().size()).append("\n\n");
        
        stats.append("--- Desglose por Tipo de Usuario ---\n");
        int estudiantes = 0, profesores = 0, administrativos = 0;
        for (Usuario u : gestor.getusuarios()) {
            if (u instanceof Estudiante) estudiantes++;
            else if (u instanceof Profesor) profesores++;
            else if (u instanceof Administrativo) administrativos++;
        }
        stats.append("Estudiantes: ").append(estudiantes).append("\n");
        stats.append("Profesores: ").append(profesores).append("\n");
        stats.append("Administrativos: ").append(administrativos).append("\n");
        
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Estadisticas del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosPrueba() {
        
        gestor.registrarUsuario(new Estudiante("Julissa Pérez", "julissa_perez@unitec.edu", "INGE2001", "Ingeniería en Sistemas"));
        gestor.registrarUsuario(new Estudiante("Maria Jose González", "maria_gonzalez@unitec.edu", "INGE2002", "Ingeniería en Gestión Logística"));
        gestor.registrarUsuario(new Profesor("Carlos Ramírez", "carlos_ramirez@unitec.edu", "DEP001", "Departamento de Informática"));
        gestor.registrarUsuario(new Profesor("Elisabet López", "elisa_lopez@unitec.edu", "DEP002", "Departamento de Matemáticas"));
        gestor.registrarUsuario(new Administrativo("Faty Perez", "faty_perez@unitec.edu", "LIC101", "Licenciatura en Mercadotecnia"));
        gestor.registrarUsuario(new Estudiante("Ashly Cruz", "ashly_cruz@unitec.edu", "INFO301", "Ingeniería en Sistemas"));
        gestor.registrarUsuario(new Estudiante("Nicol González", "nicol_gonzalez@unitec.edu", "EST002", "Derecho"));
        gestor.registrarUsuario(new Profesor("Edras Ramírez", "carlos_ramirez@unitec.edu", "EST001", "Acompañamiento Estudiantil"));
        gestor.registrarUsuario(new Profesor("Liliana Mancia", "liliana_mancia@unitec.edu", "ADM0202", "Administrador del CRAI"));
        gestor.registrarUsuario(new Administrativo("Oscar Sanchez", "oscar_sanchez@unitec.edu", "INFO2001", "Ingeniería en Gestión Logística"));
  
        gestor.agregarRecurso(new LibroFisico("Java: The Complete Reference", "LF001", "Herbert Schildt", 
            "978-0071808552", "McGraw-Hill Education", 2020, "ProgramaciónI"));
        gestor.agregarRecurso(new LibroFisico("Clean Code", "LF002", "Robert C. Martin", 
            "978-0132350884", "Prentice Hall", 2008, "Aplicaiones Web"));
        gestor.agregarRecurso(new LibroFisico("Design Patterns", "LF003", "Gang of Four", 
            "978-0201633612", "Addison-Wesley", 1994, "ProgramaciónII"));
        
        gestor.agregarRecurso(new LibroDigital("Effective Java", "LD001", "Ing. Claudio Gutierrez", 
            "978-0134685991", "Clase y Objetos", 2018, "Programación", 
            "file:///C:/Users/User/Downloads/Clase%20-%20Clases%20y%20Objetos-1.pdf", "PDF", 15.2));
        gestor.agregarRecurso(new LibroDigital("Effective Java", "LD002", "Spring Team", 
            "978-2468013570", "Arreglos en Java", 2023, "Frameworks", 
            "file:///C:/Users/User/Downloads/Clase%2010%20-%20Arreglos%20-%20Parte%20II-1.pdf", "PDF", 25.8));
 gestor.agregarRecurso(new LibroDigital("Licenciatura en Mercadotecnia", "LD002", "Diego Monferrer Tirado", 
            "456-0000000000", "Fundamentos del Marketing", 2023, "Frameworks", 
            "https://drive.google.com/file/d/153-PXxCP-O0pu105IgozE1HPbeWJDd5J/view?pli=1", "HTML", 25.8));

        gestor.agregarRecurso(new Audiobook("The Pragmatic Programmer", "AB001", "Andy Hunt & Dave Thomas", 
            "978-0201616224", "Addison-Wesley", 1999, "Desarrollo de Software", "Various Narrators", 540));

        
        gestor.realizarPrestamo("EST001", "LF001"); 
        gestor.realizarPrestamo("PROF001", "LF002"); 
        gestor.realizarPrestamo("EST002", "AB001"); 

        System.out.println("✅ Datos de prueba cargados exitosamente:");
        System.out.println("   - " + gestor.getusuarios().size() + " usuarios registrados");
        System.out.println("   - " + gestor.getcatalogo().size() + " recursos en catalogo");  
        System.out.println("   - " + gestor.getprestamos().size() + " prestamos activos");
    }
}