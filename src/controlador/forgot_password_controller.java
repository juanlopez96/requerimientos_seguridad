/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import modelo.database;
import modelo.encrypt;
import modelo.persona;
import modelo.send_validation;
import vista.forgot_password;
import vista.login;

/**
 *
 * @author Juan
 */
public class forgot_password_controller implements ActionListener {

    forgot_password vista;
    Logger LOGGER;
    database db;
    persona person;
    String text;
    String subject;
    send_validation send_mail;
    String mustNumber;
    String mustUpper;
    String mustLower;
    String mustSpecial;
    String mustLength;
    encrypt enc;

    public forgot_password_controller(forgot_password vista, Logger LOGGER, database db) {
        this.vista = vista;
        this.vista.setSize(new Dimension(430, 300));
        this.LOGGER = LOGGER;
        this.db = db;
        enc = new encrypt();
        mustLength = "La constraseña debe tener al menos 12 caracteres\n";
        mustNumber = "La contraseña debe tener al menos un numero\n";
        mustUpper = "La contraseña debe tener una mayuscula\n";
        mustLower = "La contraseña debe tener una minuscula\n";
        mustSpecial = "La contraseña debe tener un caracter especial";
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton1.setEnabled(false);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jTextField1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vista.jButton1.setEnabled(encrypt.validateEmail(vista.jTextField1.getText()));
            }
        });
        this.vista.jPanel1.setBounds(140, 20, 200, 150);
        this.vista.jPanel2.setVisible(false);
        this.vista.jToggleButton1.addActionListener((ActionListener) this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            String email = this.vista.jTextField1.getText();
            String code = encrypt.generateCode();
            this.person = db.getAllPerson().stream().filter(x -> email.equals(x.getCorreo_persona())).findFirst().orElse(null);
            send_mail = new send_validation();
            text = "Hola " + person.getNombre_persona() + ", hemos detectadao un intento de cambio de contraseña, por favor ingresa el código "
                    + code + " para poder reestablecerla";
            subject = "Código de verificación - Reestablecer contraseña";
            if (person != null) {
                if (send_mail.sendCode(db, person, code, text, subject)) {
                    LOGGER.log(Level.INFO, "Email con codigo de verificacion enviado a {0}", person.getId_persona());
                    String inputCode = JOptionPane.showInputDialog(vista, "Ingrese el código de verificación enviado al correo");
                    if (inputCode.equals(db.validateCode(person.getId_persona()))) {
                        LOGGER.log(Level.INFO, "Codigo ingresado validado correctamente");
                        this.vista.jPanel1.setVisible(false);
                        this.vista.jPanel2.setVisible(true);
                        this.vista.jPanel2.setBounds(40, 20, 350, 300);
                        vista.changepass.setEnabled(false);
                        this.vista.newpass1.addKeyListener(new KeyListener() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                            }

                            @Override
                            public void keyPressed(KeyEvent e) {
                            }

                            @Override
                            public void keyReleased(KeyEvent e) {
                                validatePasswords();
                                String guideText = "";
                                if (!enc.validatePasswordLength(vista.newpass1.getText())) {
                                    guideText += mustLength;
                                }
                                if (!enc.validatePasswordNumber(vista.newpass1.getText())) {
                                    guideText += mustNumber;
                                }
                                if (!enc.validatePasswordUpper(vista.newpass1.getText())) {
                                    guideText += mustUpper;
                                }
                                if (!enc.validatePasswordLower(vista.newpass1.getText())) {
                                    guideText += mustLower;
                                }
                                if (!enc.validatePasswordSpecial(vista.newpass1.getText())) {
                                    guideText += mustSpecial;
                                }
                                vista.jTextArea1.setText(guideText);
                                vista.jLabel4.setText(enc.passwordStrength(vista.newpass1.getText(), e.getKeyChar()));
                            }
                        });
                        this.vista.newpass2.addKeyListener((new KeyListener() {
                            @Override
                            public void keyTyped(KeyEvent e) {
                            }

                            @Override
                            public void keyPressed(KeyEvent e) {
                            }

                            @Override
                            public void keyReleased(KeyEvent e) {
                                validatePasswords();
                            }
                        }));
                        this.vista.changepass.addActionListener((ActionListener) this);
                    } else {
                        LOGGER.log(Level.WARNING, "Error al validar el código");
                    }
                }
            }
        } else if (e.getSource() == this.vista.jButton2) {
            LOGGER.log(Level.INFO, "Volviendo al login");
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            this.vista.dispose();
        } else if (e.getSource() == this.vista.changepass) {
            String aux_pass = this.vista.newpass2.getText();
            if (enc.validatePasswordLength(aux_pass) && enc.validatePasswordLower(aux_pass)
                    && enc.validatePasswordNumber(aux_pass) && enc.validatePasswordSpecial(aux_pass)
                    && enc.validatePasswordUpper(aux_pass)) {
                if (db.updatePassword(this.person.getId_persona(), enc.toEncrypt(this.vista.newpass2.getText()))) {
                    LOGGER.log(Level.INFO, "Cambio de contraseña efectuado al usuario de id {0}",person.getId_persona());
                    text = "Señor(a) " + person.getNombre_persona() + " su contraseña ha sido cambiada correctamente";
                    subject = "Confirmación de cambio de contraseña";
                    send_mail.sendMessage(db, person, text, subject);
                    JOptionPane.showMessageDialog(vista, "Contraseña cambiada satisfactoriamente");
                    LOGGER.log(Level.INFO, "Volviendo al login");
                    login login = new login();
                    login.setVisible(true);
                    login_controller controller = new login_controller(login, db, LOGGER);
                    this.vista.dispose();
                }
            }else{
                JOptionPane.showMessageDialog(vista, mustLength+mustLower+mustUpper+mustNumber+mustSpecial, "Error",JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == this.vista.jToggleButton1) {
            if (this.vista.jToggleButton1.getModel().isSelected()) {
                this.vista.newpass1.setEchoChar((char) 0);
            } else {
                this.vista.newpass1.setEchoChar((char) '*');
            }
        }
    }

    private boolean validatePasswords() {
        if (vista.newpass2.getText().equals(vista.newpass1.getText()) &&  enc.validatePasswordLength(vista.newpass1.getText())) {
            vista.changepass.setEnabled(true);
            return true;
        } else {
            vista.changepass.setEnabled(false);
            return false;
        }
    }

}
