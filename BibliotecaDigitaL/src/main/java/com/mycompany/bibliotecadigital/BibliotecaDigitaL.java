package com.mycompany.bibliotecadigital;

import com.mycompany.bibliotecadigital.gui.VentanaPrincipal;
import com.mycompany.bibliotecadigital.gui.LoginDialog;
import com.mycompany.bibliotecadigital.persistencia.ConfiguracionManager;
import javax.swing.*;

public class BibliotecaDigitaL {

    public static void main(String[] args) {
       
        ConfiguracionManager.cargarConfiguracion();
        
       
        try {
            
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            
            System.out.println("Usando look and feel por defecto");
        }
        
      
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               
                LoginDialog loginDialog = new LoginDialog(null);
                loginDialog.setVisible(true);
                
               
                if (loginDialog.isLoginExitoso()) {
                    try {
                        VentanaPrincipal ventana = new VentanaPrincipal();
                        ventana.setVisible(true);
                        System.out.println("Sistema de Biblioteca Digital iniciado correctamente");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error al iniciar la aplicacion: " + e.getMessage());
                    }
                } else {
                    System.out.println("Login cancelado. Cerrando aplicacion...");
                    System.exit(0);
                }
            }
        });
    }
}