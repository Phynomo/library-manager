package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.EntradaEspera;
import com.mycompany.bibliotecadigital.model.GestorBiblioteca;
import com.mycompany.bibliotecadigital.model.Recurso;
import com.mycompany.bibliotecadigital.model.Usuario;

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

        btnBuscar.addActionListener(this::buscarLista);
        btnMostrarTodos.addActionListener(e -> cargarListas());

        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnMostrarTodos);
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

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar a Espera");
        JButton btnRemover = new JButton("Remover de Espera");
        JButton btnSiguiente = new JButton("Asignar al siguiente");

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

    // ⏭️ Asignar al siguiente en la espera
    private void asignarSiguiente(ActionEvent e) {
        String recursoSel = (String) cmbRecursos.getSelectedItem();

        String idRecurso = recursoSel.split(" - ")[0];
        EntradaEspera siguiente = gestor.getListaEspera().siguienteEnEspera(idRecurso);

        if (siguiente != null) {
            JOptionPane.showMessageDialog(this,
                    "Siguiente en espera: " + siguiente.getUsuario().getNombre());
            // Aquí podrías directamente crear un préstamo con ese usuario y recurso
        } else {
            JOptionPane.showMessageDialog(this, "No hay usuarios en la lista de espera");
        }
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
