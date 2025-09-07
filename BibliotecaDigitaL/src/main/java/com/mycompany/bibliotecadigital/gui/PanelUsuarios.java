package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

public class PanelUsuarios extends JPanel {
    private GestorBiblioteca gestor;
    private JTable tablausua;
    private DefaultTableModel modelotab;
    private JTextField txtnombre, txtemail, txtId, txtextra;
    private JComboBox<String> cmbtipousua;
    private JTextField txtbuscar;
    private JLabel lblextra;

    public PanelUsuarios(GestorBiblioteca gestor) {
        this.gestor = gestor;
        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar Usuario"));
        
        txtbuscar = new JTextField(20);
        JButton btnBuscar = new JButton("🔍 Buscar");
        JButton btnMostrarTodos = new JButton("📋 Mostrar Todos");
        
        btnBuscar.addActionListener(this::buscarUsuarios);
        btnMostrarTodos.addActionListener(e -> cargarUsuarios());
        
        panelBusqueda.add(new JLabel("Buscar por nombre o ID:"));
        panelBusqueda.add(txtbuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);
        
        add(panelBusqueda, BorderLayout.NORTH);

        
        String[] columnas = {"ID", "Nombre", "Email", "Tipo", "Carrera/Departamento/Puesto", "Límite Libros", "Días Préstamo"};
        modelotab = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablausua = new JTable(modelotab);
        tablausua.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablausua.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(tablausua);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelRegistro = crearPanelRegistro();
        add(panelRegistro, BorderLayout.EAST);
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Usuario"));
        panel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtnombre = new JTextField(20);
        panel.add(txtnombre, gbc);

       
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email institucional:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtemail = new JTextField(20);
        panel.add(txtemail, gbc);

       
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("ID de identificacion:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField(20);
        panel.add(txtId, gbc);

        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tipo de usuario:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
       cmbtipousua = new JComboBox<>(new String[]{"Estudiante", "Profesor", "Administrativo"});
       cmbtipousua.addActionListener(this::cambiarTipoUsuario);
        panel.add(cmbtipousua, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        lblextra = new JLabel("Carrera:");
        panel.add(lblextra, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtextra = new JTextField(20);
        panel.add(txtextra, gbc);

        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JTextArea infoArea = new JTextArea(3, 25);
        infoArea.setEditable(false);
        infoArea.setBackground(panel.getBackground());
        infoArea.setFont(infoArea.getFont().deriveFont(Font.ITALIC));
        infoArea.setText("Estudiante: Max. 3 libros por 14 dias");
        infoArea.setBorder(BorderFactory.createTitledBorder("Límites del Usuario"));
        panel.add(infoArea, gbc);

     
        gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnRegistrar = new JButton("✅ Registrar Usuario");
        JButton btnLimpiar = new JButton("🔄 Limpiar Campos");
        JButton btnEliminar = new JButton("❌ Eliminar Usuario");
        
        btnRegistrar.addActionListener(this::registrarUsuario);
        btnLimpiar.addActionListener(this::limpiarFormulario);
        btnEliminar.addActionListener(this::eliminarUsuario);
        
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panel.add(panelBotones, gbc);

     
        cmbtipousua.setSelectedIndex(0);
        cambiarTipoUsuario(null);

        return panel;
    }

    private void cambiarTipoUsuario(ActionEvent e) {
        String tipo = (String) cmbtipousua.getSelectedItem();
        switch (tipo) {
            case "Estudiante":
                lblextra.setText("Carrera:");
                txtextra.setToolTipText("Ej: Ingenieria en Sistemas");
                break;
            case "Profesor":
                lblextra.setText("Departamento:");
                txtextra.setToolTipText("Ej: Departamento de Informatica o Matematicas");
                break;
            case "Administrativo":
                lblextra.setText("Puesto:");
                txtextra.setToolTipText("Ej: Bibliotecario, Asistente");
                break;
        }
    }

    private void registrarUsuario(ActionEvent e) {
        try {
            
            String nombre = txtnombre.getText().trim();
            String email = txtemail.getText().trim();
            String id = txtId.getText().trim();
            String extra = txtextra.getText().trim();
            String tipo = (String) cmbtipousua.getSelectedItem();

            if (nombre.isEmpty() || email.isEmpty() || id.isEmpty() || extra.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: Todos los campos son obligatorios", 
                    "Campos Incompletos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validarEmail(email)) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: El formato del email no es valido", 
                    "Email Invalido", JOptionPane.ERROR_MESSAGE);
                return;
            }

           
            Usuario usuario = null;
            switch (tipo) {
                case "Estudiante":
                    usuario = new Estudiante(nombre, email, id, extra);
                    break;
                case "Profesor":
                    usuario = new Profesor(nombre, email, id, extra);
                    break;
                case "Administrativo":
                    usuario = new Administrativo(nombre, email, id, extra);
                    break;
            }

            
            if (gestor.registrarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Usuario registrado exitosamente:\n" +
                    "Nombre: " + nombre + "\n" +
                    "Tipo: " + tipo + "\n" +
                    "Límite de prestamos: " + usuario.getLimitePrestamos() + " libros\n" +
                    "Tiempo de prestamo: " + usuario.getDiasPrestamo() + " días", 
                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario(null);
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Error: Ya existe un usuario con el ID '" + id + "'", 
                    "ID Duplicado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error inesperado: " + ex.getMessage(), 
                "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario(ActionEvent e) {
        txtnombre.setText("");
        txtemail.setText("");
        txtId.setText("");
        txtextra.setText("");
        cmbtipousua.setSelectedIndex(0);
        txtnombre.requestFocus();
    }

    private void eliminarUsuario(ActionEvent e) {
        int filaSeleccionada = tablausua.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Seleccione un usuario de la tabla para poder eliminar", 
                "Sin Seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUsuario = (String) modelotab.getValueAt(filaSeleccionada, 0);
        String nombreUsuario = (String) modelotab.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Esta seguro de eliminar al usuario:\n" + nombreUsuario + " (" + idUsuario + ")?", 
            "Confirmar Eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
           
            Usuario usuarioAEliminar = gestor.buscarUsuario(idUsuario);
            if (usuarioAEliminar != null) {
                gestor.getusuarios().remove(usuarioAEliminar);
                cargarUsuarios();
                JOptionPane.showMessageDialog(this, 
                    "✅ Usuario eliminado exitosamente", 
                    "Eliminacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void buscarUsuarios(ActionEvent e) {
        String criterio = txtbuscar.getText().trim().toLowerCase();
        if (criterio.isEmpty()) {
            cargarUsuarios();
            return;
        }

        modelotab.setRowCount(0);
        for (Usuario usuario : gestor.getusuarios()) {
            if (usuario.getNombre().toLowerCase().contains(criterio) || 
                usuario.getIdentificacion().toLowerCase().contains(criterio) ||
                usuario.getEmail().toLowerCase().contains(criterio)) {
                agregarFilaUsuario(usuario);
            }
        }
    }

    private void cargarUsuarios() {
        modelotab.setRowCount(0);
        for (Usuario usuario : gestor.getusuarios()) {
            agregarFilaUsuario(usuario);
        }
    }

    private void agregarFilaUsuario(Usuario usuario) {
        String tipo = usuario.getClass().getSimpleName();
        String extra = "";
        
        if (usuario instanceof Estudiante) {
            extra = ((Estudiante) usuario).getcarrera();
        } else if (usuario instanceof Profesor) {
            extra = ((Profesor) usuario).getDepartamento();
        } else if (usuario instanceof Administrativo) {
            extra = ((Administrativo) usuario).getPuesto();
        }

        Object[] fila = {
            usuario.getIdentificacion(),
            usuario.getNombre(),
            usuario.getEmail(),
            tipo,
            extra,
            usuario.getLimitePrestamos(),
            usuario.getDiasPrestamo()
        };
        modelotab.addRow(fila);
    }

    private boolean validarEmail(String email) {
        String patronEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(patronEmail).matcher(email).matches();
    }
}