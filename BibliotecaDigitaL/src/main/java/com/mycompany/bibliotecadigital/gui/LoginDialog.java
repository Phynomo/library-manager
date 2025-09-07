package com.mycompany.bibliotecadigital.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {
    private JTextField txtusua;
    private JPasswordField txtcontra;
    private JButton btnlogin, btncancel;
    private boolean loginExitoso = false;
    
    
    private final String[] usua_VALIDOS = {"admin", "bibliotecario", "supervisor", "catedratico"};
    private final String[] contra_VALIDAS = {"admin123", "biblio001", "super456", "catedra123"};

    public LoginDialog(Frame parent) {
        super(parent, "Acceso al Sistema - Biblioteca Digital", true);
        crearInterfaz();
        configurarVentana();
    }

    private void crearInterfaz() {
      
        setLayout(new BorderLayout());
        
      
        JPanel pnlTitulo = new JPanel();
        pnlTitulo.setBackground(new Color(70, 130, 180));
        JLabel lblTitulo = new JLabel("SISTEMA DE BIBLIOTECA DIGITAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        pnlTitulo.add(lblTitulo);
        
       
        JPanel pnlFormulario = new JPanel(null); // Layout absoluto
        pnlFormulario.setPreferredSize(new Dimension(350, 150));
        
      
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 30, 80, 25);
        pnlFormulario.add(lblUsuario);
        
      
        txtusua = new JTextField();
        txtusua.setBounds(140, 30, 150, 25);
        pnlFormulario.add(txtusua);
        
        
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 70, 80, 25);
        pnlFormulario.add(lblPassword);
        
     
        txtcontra = new JPasswordField();
        txtcontra.setBounds(140, 70, 150, 25);
        pnlFormulario.add(txtcontra);
        
      
        JPanel pnlBotones = new JPanel();
        btnlogin = new JButton("Iniciar Sesion");
        btncancel = new JButton("Cancelar");
        
      
        btnlogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
        
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
      
        txtusua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtcontra.requestFocus();
            }
        });
        
        txtcontra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
        
        pnlBotones.add(btnlogin);
        pnlBotones.add(btncancel);
     
        add(pnlTitulo, BorderLayout.NORTH);
        add(pnlFormulario, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void configurarVentana() {
        setSize(400, 220);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txtusua.requestFocus();
            }
        });
    }

    private void validarLogin() {
        String usuario = txtusua.getText().trim();
        String password = new String(txtcontra.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese usuario y contraseña", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < usua_VALIDOS.length; i++) {
            if (usua_VALIDOS[i].equals(usuario) && contra_VALIDAS[i].equals(password)) {
                loginExitoso = true;
                JOptionPane.showMessageDialog(this, 
                    "Bienvenido al Sistema, " + usuario, 
                    "Acceso Autorizado", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Usuario o contraseña incorrectos", 
            "Error de Acceso", 
            JOptionPane.ERROR_MESSAGE);
        txtcontra.setText("");
        txtusua.requestFocus();
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }
}