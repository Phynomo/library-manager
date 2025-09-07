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
    private JTextField txtidusuario, txtidrecurso;
    private JTextField txtbuscusua;
    private SimpleDateFormat formatofecha = new SimpleDateFormat("dd/MM/yyyy");
    private JTextField txtisrecurso;

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

        gbc.gridx = 0; gbc.gridy = 0;
        panelNuevoPrestamo.add(new JLabel("ID Usuario:"), gbc);
        gbc.gridx = 1;
        txtidusuario = new JTextField(15);
        panelNuevoPrestamo.add(txtidusuario, gbc);

        gbc.gridx = 2;
        JButton btnBuscarUsuario = new JButton("Buscar Usuario");
        btnBuscarUsuario.addActionListener(this::buscarUsuario);
        panelNuevoPrestamo.add(btnBuscarUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelNuevoPrestamo.add(new JLabel("ID Recurso:"), gbc);
        gbc.gridx = 1;
        txtidrecurso = new JTextField(15);
        panelNuevoPrestamo.add(txtidrecurso, gbc);

        gbc.gridx = 2;
        JButton btnBuscarRecurso = new JButton("Buscar Recurso");
        btnBuscarRecurso.addActionListener(this::buscarRecurso);
        panelNuevoPrestamo.add(btnBuscarRecurso, gbc);

        // Botones de acción
        JPanel panelBotones = new JPanel();
        JButton btnPrestar = new JButton("Realizar Préstamo");
        JButton btnDevolver = new JButton("Procesar Devolución");
        JButton btnActualizar = new JButton("Actualizar Lista");

        btnPrestar.addActionListener(this::realizarPrestamo);
        btnDevolver.addActionListener(this::procesarDevolucion);
        btnActualizar.addActionListener(e -> cargarPrestamos());

        panelBotones.add(btnPrestar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnActualizar);

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

    private void realizarPrestamo(ActionEvent e) {
        String idUsuario = txtidusuario.getText().trim();
        String idRecurso = txtidrecurso.getText().trim();

        if (idUsuario.isEmpty() || idRecurso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID de usuario y recurso");
            return;
        }

        if (gestor.realizarPrestamo(idUsuario, idRecurso)) {
            JOptionPane.showMessageDialog(this, "Prestamo realizado exitosamente");
            txtidusuario.setText("");
            txtidrecurso.setText("");
            cargarPrestamos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al realizar prestamo:\n" +
                "- Verifique que el usuario y recurso existan\n" +
                "- El usuario puede haber alcanzado su limite\n" +
                "- El recurso puede no estar disponible");
        }
    }

    private void procesarDevolucion(ActionEvent e) {
        String idUsuario = txtidusuario.getText().trim();
        String idRecurso = txtidrecurso.getText().trim();

        if (idUsuario.isEmpty() || idRecurso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar ID de usuario y recurso");
            return;
        }

        if (gestor.procesarDevolucion(idUsuario, idRecurso)) {
            JOptionPane.showMessageDialog(this, "Devolución procesada exitosamente");
            txtidusuario.setText("");
            txtidrecurso.setText("");
            cargarPrestamos();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se encontro el prestamo activo");
        }
    }

    private void buscarUsuario(ActionEvent e) {
        String id = txtidusuario.getText().trim();
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
        String id = txtidrecurso.getText().trim();
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
        modelotabla.setRowCount(0);
        for (Prestamo prestamo : gestor.getprestamos()) {
            if (prestamo.isActivo()) {
                agregarFilaPrestamo(prestamo);
            }
        }
    }

    private void agregarFilaPrestamo(Prestamo prestamo) {
        String estado = prestamo.estaVencido() ? "VENCIDO" : "ACTIVO";
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