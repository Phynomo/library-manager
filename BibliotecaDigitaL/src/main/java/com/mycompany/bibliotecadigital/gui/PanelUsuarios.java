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
    private JTextArea infoArea;

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


        String[] columnas = {"ID", "Nombre", "Email", "Tipo", "Carrera/Departamento/Puesto", "Límite Libros", "Días Préstamo", "Activo"};
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


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Establece el padding para todos los componentes

        // --- FILA 0: Nombre Completo y Campo ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Alinea la etiqueta a la izquierda
        gbc.fill = GridBagConstraints.NONE; // Asegura que la etiqueta no se estire
        panel.add(new JLabel("Nombre completo:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que el campo de texto se estire
        txtnombre = new JTextField(20);
        panel.add(txtnombre, gbc);

        // --- FILA 1: Email Institucional y Campo ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email institucional:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtemail = new JTextField(20);
        panel.add(txtemail, gbc);

        // --- FILA 2: ID de Identificación y Campo ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("ID de identificacion:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtId = new JTextField(20);
        panel.add(txtId, gbc);

        // --- FILA 3: Tipo de Usuario y ComboBox ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tipo de usuario:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbtipousua = new JComboBox<>(new String[]{"Estudiante", "Profesor", "Administrativo"});
        cmbtipousua.addActionListener(this::cambiarTipoUsuario);
        panel.add(cmbtipousua, gbc);

        // --- FILA 4: Campo Extra (Carrera o Departamento) ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        lblextra = new JLabel("Carrera:");
        panel.add(lblextra, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtextra = new JTextField(20);
        panel.add(txtextra, gbc);

        // --- FILA 5: Área de Información y Límites del Usuario ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Ocupa 2 columnas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permite que el JTextArea se estire
        infoArea = new JTextArea(3, 25);
        infoArea.setEditable(false);
        infoArea.setBackground(panel.getBackground());
        infoArea.setFont(infoArea.getFont().deriveFont(Font.ITALIC));
        infoArea.setText("Estudiante: Max. 3 libros por 14 dias");
        infoArea.setBorder(BorderFactory.createTitledBorder("Límites del Usuario"));
        panel.add(infoArea, gbc);

        // --- ESPACIO VACÍO PARA EMPUJAR LOS BOTONES HACIA ABAJO ---
        // Esto es crucial para que los botones no se corten.
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; // Se expande en ambas direcciones
        gbc.weighty = 1.0; // Pesa 1, lo que hace que ocupe todo el espacio extra
        panel.add(new JPanel(), gbc); // Se añade un panel vacío que se estira

        // --- FILA 7: Panel de Botones ---
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2; // Ocupa ambas columnas para que los botones se centren
        gbc.fill = GridBagConstraints.NONE; // Desactiva el estiramiento del panel de botones
        gbc.anchor = GridBagConstraints.CENTER; // Centra el panel en su celda
        gbc.weighty = 0; // Desactiva el peso vertical para que esta fila no se estire

        // Crear un JPanel interno con FlowLayout para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Declaración e inicialización de los botones (si no están ya a nivel de clase)
        JButton btnRegistrar = new JButton("✅ Registrar Usuario");
        JButton btnLimpiar = new JButton("🔄 Limpiar Campos");
        JButton btnEliminar = new JButton("❌ Des/activar Usuario");
        JButton btnModificar = new JButton("✎ Modificar Usuario");

        btnRegistrar.addActionListener(this::registrarUsuario);
        btnLimpiar.addActionListener(this::limpiarFormulario);
        btnEliminar.addActionListener(this::eliminarUsuario);
        btnModificar.addActionListener(this::modificarUsuario);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnModificar);
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
                infoArea.setText("Estudiante: Max. 3 libros por 14 dias");
                break;
            case "Profesor":
                lblextra.setText("Departamento:");
                txtextra.setToolTipText("Ej: Departamento de Informatica o Matematicas");
                infoArea.setText("Profeso: Max. 5 libros por 30 dias");
                break;
            case "Administrativo":
                lblextra.setText("Puesto:");
                txtextra.setToolTipText("Ej: Bibliotecario, Asistente");
                infoArea.setText("Administrativo: Max. 2 libros por 7 dias");
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
                "¿Esta seguro de realizar esta operación al usuario:\n" + nombreUsuario + " (" + idUsuario + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {

            Usuario usuario = gestor.buscarUsuario(idUsuario); // usa el método que ya tienes
            if (usuario != null) {
                // Actualiza los campos
                usuario.setActivo(!usuario.getActivo());
                gestor.guardartodosdatos();
            }

            JOptionPane.showMessageDialog(this,
                    "Operación realizada",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            cargarUsuarios();
        }
    }

private void modificarUsuario(ActionEvent e) {
    int filaSeleccionada = tablausua.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
                "⚠ Seleccione un usuario de la tabla para modificar",
                "Sin Selección", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener datos actuales
    String idUsuario = (String) modelotab.getValueAt(filaSeleccionada, 0);
    String nombreActual = (String) modelotab.getValueAt(filaSeleccionada, 1);
    String correoActual = (String) modelotab.getValueAt(filaSeleccionada, 2);
    String extraActual = (String) modelotab.getValueAt(filaSeleccionada, 4);

    // Pedir nuevos valores al usuario
    String nuevoNombre = JOptionPane.showInputDialog(this, "Modificar Nombre:", nombreActual);
    if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

    String nuevoCorreo = JOptionPane.showInputDialog(this, "Modificar Correo:", correoActual);
    if (nuevoCorreo == null || nuevoCorreo.trim().isEmpty()) return;

    String nuevoExtra = JOptionPane.showInputDialog(this,
            "Modificar Carrera/Departamento/Puesto:", extraActual);
    if (nuevoExtra == null || nuevoExtra.trim().isEmpty()) return;

    // Actualizar en la tabla
    modelotab.setValueAt(nuevoNombre, filaSeleccionada, 1);
    modelotab.setValueAt(nuevoCorreo, filaSeleccionada, 2);
    modelotab.setValueAt(nuevoExtra, filaSeleccionada, 4);

    // Actualizar en el objeto
    Usuario usuario = gestor.buscarUsuario(idUsuario);
    if (usuario != null) {
        usuario.setNombre(nuevoNombre);
        usuario.setEmail(nuevoCorreo);

        if (usuario instanceof Estudiante) {
            ((Estudiante) usuario).setcarrera(nuevoExtra);
        } else if (usuario instanceof Profesor) {
            ((Profesor) usuario).setDepartamento(nuevoExtra);
        } else if (usuario instanceof Administrativo) {
            ((Administrativo) usuario).setPuesto(nuevoExtra);
        }

        gestor.guardartodosdatos();
        JOptionPane.showMessageDialog(this, "✅ Usuario modificado correctamente.");
    } else {
        System.err.println("⚠ No se encontró el usuario con id " + idUsuario);
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
                usuario.getDiasPrestamo(),
                (usuario.getActivo() == true) ? "Sí" : "No",

        };
        modelotab.addRow(fila);
    }

    private boolean validarEmail(String email) {
        String patronEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(patronEmail).matcher(email).matches();
    }
}