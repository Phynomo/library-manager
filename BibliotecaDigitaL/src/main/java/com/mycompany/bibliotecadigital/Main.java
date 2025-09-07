package com.mycompany.bibliotecadigital;

import com.mycompany.bibliotecadigital.gui.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    VentanaPrincipal ventana = new VentanaPrincipal();
                    ventana.setVisible(true);
                    System.out.println("Sistema de Biblioteca Digital iniciado correctamente");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error al iniciar la aplicacion: " + e.getMessage());
                }
            }
        });
    }
}