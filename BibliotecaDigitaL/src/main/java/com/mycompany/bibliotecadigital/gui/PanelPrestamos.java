package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;

public class PanelPrestamos extends JPanel {
    private GestorBiblioteca gestor;
    private JTable tablapresta;
    private DefaultTableModel modelotabla;
    private JTextField txtbuscusua;
    private SimpleDateFormat formatofecha = new SimpleDateFormat("dd/MM/yyyy");
    private JComboBox<String> cmbUsuarios, cmbRecursos;
    private JCheckBox chckbxActivo;

    public PanelPrestamos(GestorBiblioteca gestor) {
        this.gestor = gestor;
        initComponents();
        cargarPrestamos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior - Nuevo préstamo
        JPanel panelNuevoPrestamo = new JPanel(new GridBagLayout());
        panelNuevoPrestamo.setBorder(BorderFactory.createTitledBorder("Realizar Préstamo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // --- Usuario ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelNuevoPrestamo.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        cmbUsuarios = new JComboBox<>();
        cargarUsuarios();
        panelNuevoPrestamo.add(cmbUsuarios, gbc);

        gbc.gridx = 2;
        JButton btnBuscarUsuario = new JButton("Más información");
        btnBuscarUsuario.addActionListener(this::buscarUsuario);
        panelNuevoPrestamo.add(btnBuscarUsuario, gbc);

        // --- Recurso ---
        gbc.gridx = 0; gbc.gridy = 1;
        panelNuevoPrestamo.add(new JLabel("Recurso:"), gbc);
        gbc.gridx = 1;
        cmbRecursos = new JComboBox<>();
        cargarRecursos();
        panelNuevoPrestamo.add(cmbRecursos, gbc);

        gbc.gridx = 2;
        JButton btnBuscarRecurso = new JButton("Más información");
        btnBuscarRecurso.addActionListener(this::buscarRecurso);
        panelNuevoPrestamo.add(btnBuscarRecurso, gbc);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnPrestar = new JButton("Realizar Préstamo");
        JButton btnDevolver = new JButton("Procesar Devolución");
        JButton btnActualizar = new JButton("Actualizar");
        chckbxActivo  = new JCheckBox("Solo activos");
        chckbxActivo.setSelected(true);

        btnPrestar.addActionListener(this::realizarPrestamo);
        btnDevolver.addActionListener(this::procesarDevolucion);
        btnActualizar.addActionListener(e -> cargarPrestamos());

        panelBotones.add(btnPrestar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnActualizar);
        panelBotones.add(chckbxActivo);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        panelNuevoPrestamo.add(panelBotones, gbc);

        add(panelNuevoPrestamo, BorderLayout.NORTH);

        String[] columnas = {"Usuario", "Recurso", "Titulo", "Fecha Prestamo", "Fecha Vencimiento", "Estado", "Dias Restantes", "Multa"};
        modelotabla = new DefaultTableModel(columnas, 0);
        tablapresta = new JTable(modelotabla);
        JScrollPane scrollPane = new JScrollPane(tablapresta);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prestamos Activos"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Consultar por Usuario"));
        panelBusqueda.setPreferredSize(new Dimension(250, 0));

      txtbuscusua = new JTextField();
        JButton btnConsultarUsuario = new JButton("Ver Prestamos");
        btnConsultarUsuario.addActionListener(this::consultarPrestamosUsuario);

        panelBusqueda.add(new JLabel("ID Usuario:"));
        panelBusqueda.add(txtbuscusua);
        panelBusqueda.add(Box.createVerticalStrut(10));
        panelBusqueda.add(btnConsultarUsuario);

        add(panelBusqueda, BorderLayout.EAST);
    }

    private void cargarUsuarios() {
        cmbUsuarios.removeAllItems();
        for (Usuario u : gestor.getusuarios()) {
            if (u.getActivo()) {
                cmbUsuarios.addItem(u.getIdentificacion() + " - " + u.getNombre());
            }
        }
    }

    private void cargarRecursos() {
        cmbRecursos.removeAllItems();
        for (Recurso r : gestor.getcatalogo()) {
            cmbRecursos.addItem(r.getIdRecurso() + " - " + r.getTitulo());
        }
    }

    private void realizarPrestamo(ActionEvent e) {
        String usuarioSel = (String) cmbUsuarios.getSelectedItem();
        String recursoSel = (String) cmbRecursos.getSelectedItem();

        String idUsuario = usuarioSel.split(" - ")[0];
        String idRecurso = recursoSel.split(" - ")[0];

        if (idUsuario.isEmpty() || idRecurso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID de usuario y recurso");
            return;
        }

        if (gestor.realizarPrestamo(idUsuario, idRecurso)) {
            JOptionPane.showMessageDialog(this, "Prestamo realizado exitosamente");
            cmbUsuarios.setSelectedIndex(-1);
            cmbRecursos.setSelectedIndex(-1);
            cargarPrestamos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al realizar prestamo:\n" +
                "- Verifique que el usuario y recurso existan\n" +
                "- El usuario puede haber alcanzado su limite\n" +
                "- El recurso puede no estar disponible");
        }
    }

    private void procesarDevolucion(ActionEvent e) {
        int filaSeleccionada = tablapresta.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un prestamo de la tabla para poder realizar la revolución",
                    "Sin Selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modelotabla.getValueAt(filaSeleccionada, 0);
        String idRecurso = (String) modelotabla.getValueAt(filaSeleccionada, 1);

        if (idUsuario.isEmpty() || idRecurso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID de usuario y recurso");
            return;
        }

        if (gestor.procesarDevolucion(idUsuario, idRecurso)) {
            JOptionPane.showMessageDialog(this, "Devolución procesada exitosamente");
            cmbUsuarios.setSelectedIndex(-1);
            cmbRecursos.setSelectedIndex(-1);
            cargarPrestamos();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se encontro el prestamo activo");
        }
    }

    private void buscarUsuario(ActionEvent e) {
        String usuarioSel = (String) cmbUsuarios.getSelectedItem();

        String id = usuarioSel.split(" - ")[0];
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de usuario");
            return;
        }

        Usuario usuario = gestor.buscarUsuario(id);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, 
                "Usuario encontrado:\n" +
                "Nombre: " + usuario.getNombre() + "\n" +
                "Tipo: " + usuario.getClass().getSimpleName() + "\n" +
                "Límite de prestamos: " + usuario.getLimitePrestamos());
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
        }
    }

    private void buscarRecurso(ActionEvent e) {
        String recursoSel = (String) cmbRecursos.getSelectedItem();

        String txtidrecurso = recursoSel.split(" - ")[0];
        String id = txtidrecurso.trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de recurso");
            return;
        }

        for (Recurso recurso : gestor.getcatalogo()) {
            if (recurso.getIdRecurso().equals(id)) {
                JOptionPane.showMessageDialog(this,
                    "Recurso encontrado:\n" +
                    "Titulo: " + recurso.getTitulo() + "\n" +
                    "Autor: " + recurso.getAutor() + "\n" +
                    "Disponible: " + (recurso.isDisponible() ? "Sí" : "No"));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Recurso no encontrado");
    }

    private void consultarPrestamosUsuario(ActionEvent e) {
        String idUsuario = txtbuscusua.getText().trim();
        if (idUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de usuario");
            return;
        }

        modelotabla.setRowCount(0);
        for (Prestamo prestamo : gestor.getPrestamosUsuario(idUsuario)) {
            agregarFilaPrestamo(prestamo);
        }
    }

    private void cargarPrestamos() {
        cargarRecursos();
        cargarUsuarios();

        modelotabla.setRowCount(0);
        for (Prestamo prestamo : gestor.getprestamos()) {
            if (!chckbxActivo.isSelected() || prestamo.isActivo()) {
                agregarFilaPrestamo(prestamo);
            }
        }
    }

    private void agregarFilaPrestamo(Prestamo prestamo) {
        String estado = prestamo.estado();
        long diferenciaMilis = prestamo.getFechaVencimiento().getTime() - System.currentTimeMillis();
        int diasRestantes = (int) (diferenciaMilis / (1000 * 60 * 60 * 24));
        
        Object[] fila = {
            prestamo.getUsuario().getIdentificacion(),
            prestamo.getRecurso().getIdRecurso(),
            prestamo.getRecurso().getTitulo(),
            formatofecha.format(prestamo.getFechaPrestamo()),
            formatofecha.format(prestamo.getFechaVencimiento()),
            estado,
            diasRestantes,
            String.format("$%.2f", prestamo.getMulta())
        };
        modelotabla.addRow(fila);
    }
}