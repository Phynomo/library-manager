package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelRecursos extends JPanel {
    private GestorBiblioteca gestor;
    private JTable tablaRecursos;
    private DefaultTableModel modelotab;
    private JTextField txttitulo, txtautor, txtId, txtisbn, txtedi, txtaño, txtcate;
    private JComboBox<String> cmbrecurso;
    private JTextField txturldescarga, txtformato, txttamaño;
    private JTextField txtbusque;

    public PanelRecursos(GestorBiblioteca gestor) {
        this.gestor = gestor;
        initComponents();
        cargarRecursos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(new JLabel("Buscar:"));
        txtbusque = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        
        btnBuscar.addActionListener(this::buscarRecursos);
        btnMostrarTodos.addActionListener(e -> cargarRecursos());
        
        panelSuperior.add(txtbusque);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnMostrarTodos);
        
        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID", "Titulo", "Autor", "Tipo", "ISBN", "Editorial", "Año", "Categoria", "Disponible"};
        modelotab = new DefaultTableModel(columnas, 0);
        tablaRecursos = new JTable(modelotab);
        JScrollPane scrollPane = new JScrollPane(tablaRecursos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Catalogo de Recursos"));
        add(scrollPane, BorderLayout.CENTER);

   
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar Recurso"));
        panelFormulario.setPreferredSize(new Dimension(300, 0));

      
        panelFormulario.add(crearCampo("Titulo:", txttitulo = new JTextField()));
        panelFormulario.add(crearCampo("Autor:", txtautor = new JTextField()));
        panelFormulario.add(crearCampo("ID:", txtId = new JTextField()));
        panelFormulario.add(crearCampo("ISBN:", txtisbn = new JTextField()));
        panelFormulario.add(crearCampo("Editorial:", txtedi = new JTextField()));
        panelFormulario.add(crearCampo("Año:", txtaño = new JTextField()));
        panelFormulario.add(crearCampo("Categoría:", txtcate = new JTextField()));

        cmbrecurso = new JComboBox<>(new String[]{"Libro Fisico", "Libro Digital"});
        panelFormulario.add(crearCampo("Tipo:", cmbrecurso));

        
        panelFormulario.add(crearCampo("URL:", txturldescarga = new JTextField()));
        panelFormulario.add(crearCampo("Formato:", txtformato = new JTextField()));
        panelFormulario.add(crearCampo("Tamaño (MB):", txttamaño = new JTextField()));

   
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnLimpiar = new JButton("Limpiar");
        
        btnAgregar.addActionListener(this::agregarRecurso);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.EAST);
    }

    private JPanel crearCampo(String etiqueta, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(etiqueta), BorderLayout.WEST);
        panel.add(campo, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return panel;
    }

    private void agregarRecurso(ActionEvent e) {
        try {
            String titulo = txttitulo.getText().trim();
            String autor = txtautor.getText().trim();
            String id = txtId.getText().trim();
            String isbn = txtisbn.getText().trim();
            String editorial = txtedi.getText().trim();
            String añoStr = txtaño.getText().trim();
            String categoria = txtcate.getText().trim();
            String tipo = (String) cmbrecurso.getSelectedItem();

            if (titulo.isEmpty() || autor.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Título, Autor e ID son obligatorios");
                return;
            }

            int año = 0;
            try {
                año = Integer.parseInt(añoStr);
            } catch (NumberFormatException ex) {
                año = 2025; 
            }

            Recurso recurso = null;
            
            if ("Libro Físico".equals(tipo)) {
                recurso = new LibroFisico(titulo, id, autor, isbn, editorial, año, categoria);
            } else if ("Libro Digital".equals(tipo)) {
                String url = txturldescarga.getText().trim();
                String formato = txtformato.getText().trim();
                String tamañoStr = txttamaño.getText().trim();
                
                double tamaño = 0.0;
                try {
                    tamaño = Double.parseDouble(tamañoStr);
                } catch (NumberFormatException ex) {
                    tamaño = 1.0; 
                }
                
                recurso = new LibroDigital(titulo, id, autor, isbn, editorial, año, categoria, 
                                         url.isEmpty() ? "https://infolibros.org/libros-pdf-gratis/#google_vignette" : url,
                                         formato.isEmpty() ? "PDF" : formato, tamaño);
            }

            if (recurso != null && gestor.agregarRecurso(recurso)) {
                JOptionPane.showMessageDialog(this, "Recurso agregado exitosamente");
                limpiarFormulario();
                cargarRecursos();
            } else {
                JOptionPane.showMessageDialog(this, "Error: ID de recurso ya existe");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar recurso: " + ex.getMessage());
        }
    }

    private void buscarRecursos(ActionEvent e) {
        String criterio = txtbusque.getText().trim();
        if (criterio.isEmpty()) {
            cargarRecursos();
            return;
        }

        modelotab.setRowCount(0);
        for (Recurso recurso : gestor.buscarRecursos(criterio)) {
            agregarFilaRecurso(recurso);
        }
    }

    private void limpiarFormulario() {
        txttitulo.setText("");
        txtautor.setText("");
        txtId.setText("");
        txtisbn.setText("");
        txtedi.setText("");
        txtaño.setText("");
        txtcate.setText("");
        txturldescarga.setText("");
        txtformato.setText("");
        txttamaño.setText("");
    }

    private void cargarRecursos() {
        modelotab.setRowCount(0);
        for (Recurso recurso : gestor.getcatalogo()) {
            agregarFilaRecurso(recurso);
        }
    }

    private void agregarFilaRecurso(Recurso recurso) {
        String tipo = recurso instanceof LibroFisico ? "Fisico" : "Digital";
        Object[] fila = {
            recurso.getIdRecurso(),
            recurso.getTitulo(),
            recurso.getAutor(),
            tipo,
            recurso.getIsbn(),
            recurso.getEditorial(),
            recurso.getAño(),
            recurso.getCategoria(),
            recurso.isDisponible() ? "Si" : "No"
        };
        modelotab.addRow(fila);
    }
}