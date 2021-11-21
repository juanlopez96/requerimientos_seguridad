/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import modelo.database;
import modelo.encrypt;
import modelo.persona;
import vista.login;
import vista.register;

/**
 *
 * @author Juan
 */
public class register_controller implements ActionListener {

    register vista;
    database db;
    Logger LOGGER;
    encrypt enc;
    String mustNumber;
    String mustUpper;
    String mustLower;
    String mustSpecial;
    String mustLength;
    boolean validateEmail, validatePassword;

    public register_controller(register vista, database db, Logger LOGGER) {
        this.vista = vista;
        this.db = db;
        this.LOGGER = LOGGER;
        enc = new encrypt();
        mustLength = "La constraseña debe tener al menos 12 caracteres\n";
        mustNumber = "La contraseña debe tener al menos un numero\n";
        mustUpper = "La contraseña debe tener una mayuscula\n";
        mustLower = "La contraseña debe tener una minuscula\n";
        mustSpecial = "La contraseña debe tener un caracter especial";
        validateEmail = false;
        validatePassword = false;
        this.vista.jButton2.setEnabled(false);
        this.vista.jToggleButton1.addActionListener((ActionListener) this);
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jPasswordField1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                validatePassword = validatePasswords();
                String guideText = "";
                if (!enc.validatePasswordLength(vista.jPasswordField1.getText())) {
                    guideText += mustLength;
                }
                if (!enc.validatePasswordNumber(vista.jPasswordField1.getText())) {
                    guideText += mustNumber;
                }
                if (!enc.validatePasswordUpper(vista.jPasswordField1.getText())) {
                    guideText += mustUpper;
                }
                if (!enc.validatePasswordLower(vista.jPasswordField1.getText())) {
                    guideText += mustLower;
                }
                if (!enc.validatePasswordSpecial(vista.jPasswordField1.getText())) {
                    guideText += mustSpecial;
                }
                vista.jTextArea1.setText(guideText);
                vista.jLabel6.setText(enc.passwordStrength(vista.jPasswordField1.getText(), e.getKeyChar()));
            }

        });
        this.vista.jPasswordField2.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                validatePassword = validatePasswords();
            }

        });
        this.vista.jTextField3.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                validateEmail = encrypt.validateEmail(vista.jTextField3.getText());
                vista.jButton2.setEnabled(validateEmail && validatePassword);

            }
        });

    }

    private boolean validatePasswords() {
        if (vista.jPasswordField1.getText().equals(vista.jPasswordField2.getText()) && enc.validatePasswordLength(vista.jPasswordField1.getText())) {
            validatePassword = true;
            vista.jButton2.setEnabled(validateEmail && validatePassword);
            return true;
        } else {
            validatePassword = false;
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jToggleButton1) {
            if (this.vista.jToggleButton1.getModel().isSelected()) {
                this.vista.jPasswordField1.setEchoChar((char) 0);
            } else {
                this.vista.jPasswordField1.setEchoChar((char) '*');
            }
        } else if (e.getSource() == this.vista.jButton1) {
            LOGGER.log(Level.INFO, "Volviendo al login");
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            this.vista.dispose();
        } else if (e.getSource() == this.vista.jButton2) {
            int id = 0;
            try {
                id = Integer.parseInt(this.vista.jTextField1.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El id debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!db.validateNewUser(id, this.vista.jTextField3.getText())) {
                persona p = new persona();
                p.setId_persona(id);
                p.setNombre_persona(this.vista.jTextField2.getText());
                p.setNombre_rol("" + 3);
                p.setCorreo_persona(this.vista.jTextField3.getText());
                if (db.insertUser(p, enc.toEncrypt(this.vista.jPasswordField1.getText()))) {
                    LOGGER.log(Level.INFO, "Nuevo usuario registrado IDPERSONA {0}", p.getId_persona());
                    JOptionPane.showMessageDialog(vista, "Usuario insertado satisfactoriamente");
                    LOGGER.log(Level.INFO, "Volviendo al login");
                    login login = new login();
                    login.setVisible(true);
                    login_controller controller = new login_controller(login, db, LOGGER);
                    this.vista.dispose();
                } else {
                    LOGGER.log(Level.SEVERE, "Error al insertar el usuario");
                    JOptionPane.showMessageDialog(vista, "Error al insertar el usuario");
                }
            } else {
                LOGGER.log(Level.SEVERE, "Error al validar el usuario");
                JOptionPane.showMessageDialog(vista, "Error al validar el usuario");
            }

        }
    }
}
