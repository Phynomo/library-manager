package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelListaEspera extends JPanel {
    private GestorBiblioteca gestor;
    private JTable tablaEspera;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbUsuarios, cmbRecursos;
    private JTextField txtBuscar;

    public PanelListaEspera(GestorBiblioteca gestor) {
        this.gestor = gestor;
        initComponents();
        cargarListas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 🔍 Panel superior de búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(new JLabel("Buscar recurso:"));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        JButton btnActualizar = new JButton("Actualizar");

        btnBuscar.addActionListener(this::buscarLista);
        btnMostrarTodos.addActionListener(e -> cargarListas());
        btnActualizar.addActionListener(e -> {
            cargarListas();
            cargarRecursos();
        });

        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnMostrarTodos);
        panelSuperior.add(btnActualizar);
        add(panelSuperior, BorderLayout.NORTH);

        // 📋 Tabla de listas de espera
        String[] columnas = {"ID Recurso", "ID Usuario", "Nombre Usuario", "Fecha Solicitud"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEspera = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEspera);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listas de Espera"));
        add(scrollPane, BorderLayout.CENTER);

        // 📝 Panel formulario lateral
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Lista de Espera"));
        panelFormulario.setPreferredSize(new Dimension(300, 0));

        panelFormulario.add(crearCampo("Recurso:", cmbRecursos = new JComboBox<>()));
        panelFormulario.add(crearCampo("Usuario:", cmbUsuarios = new JComboBox<>()));

        cargarUsuarios();
        cargarRecursos();
        cargarListas();

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar a Espera");
        JButton btnRemover = new JButton("Remover de Espera");
        JButton btnSiguiente = new JButton("Realizar prestamo");

        btnAgregar.addActionListener(this::agregarAEspera);
        btnRemover.addActionListener(this::removerDeEspera);
        btnSiguiente.addActionListener(this::asignarSiguiente);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnRemover);
        panelBotones.add(btnSiguiente);

        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.EAST);
    }

    private void cargarUsuarios() {
        cmbUsuarios.removeAllItems();
        for (Usuario u : gestor.getusuarios()) {
            if (u.getActivo()) { // solo usuarios activos
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

    private JPanel crearCampo(String etiqueta, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(etiqueta), BorderLayout.WEST);
        panel.add(campo, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return panel;
    }

    // 👤 Agregar usuario a lista de espera
    private void agregarAEspera(ActionEvent e) {
        String usuarioSel = (String) cmbUsuarios.getSelectedItem();
        String recursoSel = (String) cmbRecursos.getSelectedItem();

        if (usuarioSel == null || recursoSel == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario y un recurso");
            return;
        }

        String idUsuario = usuarioSel.split(" - ")[0];
        String idRecurso = recursoSel.split(" - ")[0];

        Usuario usuario = gestor.buscarUsuario(idUsuario);
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");
            return;
        }

        boolean agregado = gestor.getListaEspera().agregarAEspera(idRecurso, usuario);
        if (agregado) {
            gestor.guardartodosdatos();
            JOptionPane.showMessageDialog(this, "Usuario agregado a lista de espera");
            cargarListas();
        } else {
            JOptionPane.showMessageDialog(this, "El usuario ya estaba en la lista");
        }
    }

    // ❌ Remover de lista
    private void removerDeEspera(ActionEvent e) {
        int fila = tablaEspera.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila");
            return;
        }

        String idRecurso = (String) modeloTabla.getValueAt(fila, 0);
        String idUsuario = (String) modeloTabla.getValueAt(fila, 1);

        Usuario usuario = gestor.buscarUsuario(idUsuario);
        if (gestor.getListaEspera().removerDeEspera(idRecurso, usuario)) {
            JOptionPane.showMessageDialog(this, "Usuario removido de lista");
            cargarListas();
        }
    }

    // ⏭️ Asignar al siguiente en la espera (validando orden FIFO y confirmando si se fuerza)
    private void asignarSiguiente(ActionEvent e) {
        int fila = tablaEspera.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila");
            return;
        }

        String idRecurso = (String) modeloTabla.getValueAt(fila, 0);
        String idUsuarioSeleccionado = (String) modeloTabla.getValueAt(fila, 1);

        // Obtener lista actualizada de espera para el recurso
        List<EntradaEspera> lista = gestor.getListaEspera().verListaEspera(idRecurso);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay usuarios en espera para este recurso");
            return;
        }

        EntradaEspera primero = lista.get(0);
        Usuario usuarioPrimero = primero.getUsuario();
        String idUsuarioPrimero = usuarioPrimero.getIdentificacion(); // o getIdUsuario() según tu modelo

        Usuario usuarioSeleccionado = gestor.buscarUsuario(idUsuarioSeleccionado);
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Usuario seleccionado no encontrado");
            return;
        }

        // Si el seleccionado NO es el primero, pedir confirmación
        if (!idUsuarioPrimero.equals(idUsuarioSeleccionado)) {
            // Formatear las fechas para mostrar (asume getFechaSolicitud() existe)
            String fechaPrimero = primero.getFechaSolicitud() != null ? primero.getFechaSolicitud().toString() : "fecha desconocida";
            String fechaSel = "";
            for (EntradaEspera ent : lista) {
                if (ent.getUsuario().getIdentificacion().equals(idUsuarioSeleccionado)) {
                    fechaSel = ent.getFechaSolicitud() != null ? ent.getFechaSolicitud().toString() : "fecha desconocida";
                    break;
                }
            }

            String msg = "El primer usuario en la lista para el recurso (ID: " + idRecurso + ") es:\n"
                    + usuarioPrimero.getNombre() + " (ID: " + idUsuarioPrimero + ") — solicitado: " + fechaPrimero + "\n\n"
                    + "Usuario seleccionado:\n"
                    + usuarioSeleccionado.getNombre() + " (ID: " + idUsuarioSeleccionado + ") — solicitado: " + fechaSel + "\n\n"
                    + "¿Desea asignar el recurso al usuario seleccionado en vez del primero en la lista?";

            int opcion = JOptionPane.showConfirmDialog(this, msg, "Confirmar asignación fuera de orden", JOptionPane.YES_NO_OPTION);
            if (opcion != JOptionPane.YES_OPTION) {
                // El usuario decidió no forzar la asignación
                return;
            }
            // Si confirma, seguimos y asignamos al usuario seleccionado (saltando al primero)
        }

        // Intentar realizar préstamo al usuario seleccionado
        boolean resultado = gestor.realizarPrestamo(idUsuarioSeleccionado, idRecurso);

        if (resultado) {
            JOptionPane.showMessageDialog(this, "Préstamo realizado con éxito a " + usuarioSeleccionado.getNombre());
            // Remover del listado de espera al usuario que recibió el préstamo
            gestor.getListaEspera().removerDeEspera(idRecurso, usuarioSeleccionado);

            // Opcional: si quieres persistir inmediatamente la lista de espera:
            // ArchivoListaEspera.guardarListaEspera(gestor.getListaEspera());
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo realizar el préstamo. No se ha modificado la lista de espera.");
        }

        cargarListas();
    }


    // 🔎 Buscar
    private void buscarLista(ActionEvent e) {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            cargarListas();
            return;
        }
        cargarListaDeRecurso(criterio);
    }

    // 🔄 Cargar todas las listas
    private void cargarListas() {
        modeloTabla.setRowCount(0);
        for (String idRecurso : gestor.getListaEspera().getEstadisticasEspera().keySet()) {
            cargarListaDeRecurso(idRecurso);
        }
    }

    // 📥 Cargar lista específica
    private void cargarListaDeRecurso(String idRecurso) {
        List<EntradaEspera> lista = gestor.getListaEspera().verListaEspera(idRecurso);
        for (EntradaEspera entrada : lista) {
            modeloTabla.addRow(new Object[]{
                    idRecurso,
                    entrada.getUsuario().getIdentificacion(),
                    entrada.getUsuario().getNombre(),
                    entrada.getFechaSolicitud()
            });
        }
    }

}
