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

        private Recurso recursoEditando = null; // Objeto que se está editando

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
            panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar / Editar Recurso"));
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
            JButton btnEditar = new JButton("Editar");
            JButton btnGuardar = new JButton("Guardar Cambios");
            JButton btnEliminar = new JButton("Eliminar");
            JButton btnLimpiar = new JButton("Limpiar");

            btnAgregar.addActionListener(this::agregarRecurso);
            btnEditar.addActionListener(this::editarRecurso);
            btnGuardar.addActionListener(this::guardarCambios);
            btnEliminar.addActionListener(this::eliminarRecurso);
            btnLimpiar.addActionListener(e -> limpiarFormulario());

            panelBotones.add(btnAgregar);
            panelBotones.add(btnEditar);
            panelBotones.add(btnGuardar);
            panelBotones.add(btnEliminar);
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

                // Verificar si el ID ya existe solo si no estamos editando
                if (gestor.buscarPorId(id) != null) {
                    JOptionPane.showMessageDialog(this, "Error: ID de recurso ya existe");
                    return;
                }

                int año = 2025;
                try { año = Integer.parseInt(añoStr); } catch (NumberFormatException ex) {}

                Recurso recurso = null;
                if ("Libro Fisico".equals(tipo)) {
                    recurso = new LibroFisico(titulo, id, autor, isbn, editorial, año, categoria);
                } else {
                    String url = txturldescarga.getText().trim();
                    String formato = txtformato.getText().trim();
                    double tamaño = 1.0;
                    try { tamaño = Double.parseDouble(txttamaño.getText().trim()); } catch (NumberFormatException ex) {}

                    recurso = new LibroDigital(
                            titulo, id, autor, isbn, editorial, año, categoria,
                            url.isEmpty() ? "https://infolibros.org/libros-pdf-gratis/#google_vignette" : url,
                            formato.isEmpty() ? "PDF" : formato, tamaño);
                }

                if (gestor.agregarRecurso(recurso)) {
                    JOptionPane.showMessageDialog(this, "Recurso agregado exitosamente");
                    limpiarFormulario();
                    cargarRecursos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar recurso");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar recurso: " + ex.getMessage());
            }
        }

        private void editarRecurso(ActionEvent e) {
            int fila = tablaRecursos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un recurso para editar");
                return;
            }

            String id = (String) modelotab.getValueAt(fila, 0);
            recursoEditando = gestor.buscarPorId(id);
            if (recursoEditando == null) return;

            txtId.setText(recursoEditando.getIdRecurso());
            txtId.setEditable(false);
            txttitulo.setText(recursoEditando.getTitulo());
            txtautor.setText(recursoEditando.getAutor());
            cmbrecurso.setSelectedItem(recursoEditando instanceof LibroFisico ? "Libro Fisico" : "Libro Digital");
            txtisbn.setText(recursoEditando.getIsbn());
            txtedi.setText(recursoEditando.getEditorial());
            txtaño.setText(String.valueOf(recursoEditando.getAño()));
            txtcate.setText(recursoEditando.getCategoria());

            if (recursoEditando instanceof LibroDigital) {
                LibroDigital digital = (LibroDigital) recursoEditando;
                txturldescarga.setText(digital.getUrlDescarga());
                txtformato.setText(digital.getFormato());
                txttamaño.setText(String.valueOf(digital.getTamañoMB()));
            } else {
                txturldescarga.setText("");
                txtformato.setText("");
                txttamaño.setText("");
            }
        }

        private void guardarCambios(ActionEvent e) {
            if (recursoEditando == null) {
                JOptionPane.showMessageDialog(this, "Primero seleccione un recurso y presione 'Editar'");
                return;
            }

            try {
                recursoEditando.setTitulo(txttitulo.getText().trim());
                recursoEditando.setAutor(txtautor.getText().trim());
                recursoEditando.setIsbn(txtisbn.getText().trim());
                recursoEditando.setEditorial(txtedi.getText().trim());
                recursoEditando.setCategoria(txtcate.getText().trim());

                int año = 2025;
                if (!txtaño.getText().trim().isEmpty()) {
                    try { año = Integer.parseInt(txtaño.getText().trim()); } catch (NumberFormatException ex) {}
                }
                recursoEditando.setAño(año);

                if (recursoEditando instanceof LibroDigital) {
                    LibroDigital digital = (LibroDigital) recursoEditando;
                    digital.setUrlDescarga(txturldescarga.getText().trim().isEmpty() ?
                        "https://infolibros.org/libros-pdf-gratis/#google_vignette" : txturldescarga.getText().trim());
                    digital.setFormato(txtformato.getText().trim().isEmpty() ? "PDF" : txtformato.getText().trim());

                    double tamaño = 1.0;
                    if (!txttamaño.getText().trim().isEmpty()) {
                        try { tamaño = Double.parseDouble(txttamaño.getText().trim()); } catch (NumberFormatException ex) {}
                    }
                    digital.setTamañoMB(tamaño);
                }

                JOptionPane.showMessageDialog(this, "Cambios guardados correctamente");
                cargarRecursos();
                limpiarFormulario();
                txtId.setEditable(true);
                recursoEditando = null;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar cambios: " + ex.getMessage());
            }
        }

        private void eliminarRecurso(ActionEvent e) {
            int fila = tablaRecursos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un recurso de la tabla para eliminar");
                return;
            }

            String id = (String) modelotab.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas eliminar el recurso con ID " + id + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gestor.eliminarRecurso(id)) {
                    JOptionPane.showMessageDialog(this, "Recurso eliminado exitosamente");
                    cargarRecursos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: no se pudo eliminar el recurso");
                }
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
            txtId.setEditable(true);
            recursoEditando = null;
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
