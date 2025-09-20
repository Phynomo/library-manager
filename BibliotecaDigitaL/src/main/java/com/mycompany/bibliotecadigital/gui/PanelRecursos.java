package com.mycompany.bibliotecadigital.gui;

import com.mycompany.bibliotecadigital.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

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
            add(crearPanelFormulario(), BorderLayout.EAST);
        }

        private void initComponents() {
            setLayout(new BorderLayout());

            // PANEL SUPERIOR (busqueda)
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

            // TABLA
            String[] columnas = {"ID", "Titulo", "Autor", "Tipo", "ISBN", "Editorial", "Año", "Categoria", "Disponible"};
            modelotab = new DefaultTableModel(columnas, 0);
            tablaRecursos = new JTable(modelotab);
            JScrollPane scrollPane = new JScrollPane(tablaRecursos);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Catalogo de Recursos"));
            add(scrollPane, BorderLayout.CENTER);

            // PANEL FORMULARIO (lado derecho, alineado con GridBagLayout)
            add(crearPanelFormulario(), BorderLayout.EAST);
        }

        private JPanel crearPanelFormulario() {
            JPanel panelFormulario = new JPanel(new GridBagLayout());
            panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar / Editar Recurso"));
           

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 2, 1, 5); // margenes
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Método auxiliar para añadir fila
            final int[] fila = {0};

            BiConsumer<String, JComponent> addCampo = (label, comp) -> {
                gbc.gridx = 0;
                gbc.gridy = fila[0];
                gbc.weightx = 0;
                panelFormulario.add(new JLabel(label), gbc);

                gbc.gridx = 1;
                gbc.weightx = 1;
                panelFormulario.add(comp, gbc);

                fila[0]++;
            };

            // Campos
            addCampo.accept("Titulo:", txttitulo = new JTextField());
            addCampo.accept("Autor:", txtautor = new JTextField());
            addCampo.accept("ID:", txtId = new JTextField());
            addCampo.accept("ISBN:", txtisbn = new JTextField());
            addCampo.accept("Editorial:", txtedi = new JTextField());
            addCampo.accept("Año:", txtaño = new JTextField());
            addCampo.accept("Categoría:", txtcate = new JTextField());
            addCampo.accept("Tipo:", cmbrecurso = new JComboBox<>(new String[] { "Libro Fisico", "Libro Digital" }));
            addCampo.accept("URL:", txturldescarga = new JTextField());
            addCampo.accept("Formato:", txtformato = new JTextField());
            addCampo.accept("Tamaño (MB):", txttamaño = new JTextField());

            // Panel de botones (ocupa toda la fila)
            gbc.gridx = 0;
            gbc.gridy = fila[0];
            gbc.gridwidth = 2;
            gbc.weightx = 1;
            JPanel panelBotones = new JPanel(new GridLayout(0, 1, 1, 1)); // 1 columna, varias filas
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

            panelFormulario.add(panelBotones, gbc);

            return panelFormulario;
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
