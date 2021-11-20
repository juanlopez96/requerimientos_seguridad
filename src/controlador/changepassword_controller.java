/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import modelo.send_validation;
import vista.administrator;
import vista.basicUser;
import vista.change_password;
import vista.generalUser;
import vista.login;

/**
 *
 * @author Yessica
 */
public class changepassword_controller implements ActionListener {

    change_password vista;
    Logger LOGGER;
    database db;
    persona person;
    String mustNumber;
    String mustUpper;
    String mustLower;
    String mustSpecial;
    String mustLength;
    encrypt enc;

    public changepassword_controller(change_password vista, Logger LOGGER, database db, persona person) {
        this.vista = vista;
        this.LOGGER = LOGGER;
        this.db = db;
        this.person = person;
        this.enc = new encrypt();
        mustLength = "La constraseña debe tener al menos 12 caracteres\n";
        mustNumber = "La contraseña debe tener al menos un numero\n";
        mustUpper = "La contraseña debe tener una mayuscula\n";
        mustLower = "La contraseña debe tener una minuscula\n";
        mustSpecial = "La contraseña debe tener un caracter especial";
        this.vista.jToggleButton1.addActionListener((ActionListener) this);
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jButton1.setEnabled(false);
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
        this.vista.newpass2.addKeyListener(new KeyListener() {
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
        });
    }

    private boolean validatePasswords() {
        if (vista.newpass2.getText().equals(vista.newpass1.getText()) && enc.validatePasswordLength(vista.newpass1.getText())) {
            this.vista.jButton1.setEnabled(true);
            return true;
        } else {
            this.vista.jButton1.setEnabled(false);
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            String aux_pass = this.vista.newpass1.getText();
            String pass = enc.toEncrypt(this.vista.actual_pass.getText());
            persona aux = db.validateUser(person.getCorreo_persona(), pass);
            if (enc.validatePasswordLength(aux_pass) && enc.validatePasswordLower(aux_pass)
                    && enc.validatePasswordNumber(aux_pass) && enc.validatePasswordSpecial(aux_pass)
                    && enc.validatePasswordUpper(aux_pass)) {
                if (aux != null) {
                    pass = enc.toEncrypt(this.vista.newpass2.getText());
                    if (db.updatePassword(aux.getId_persona(), pass)) {
                        LOGGER.log(Level.INFO, "Cambio de contraseña efectuado al usuario de id {0}",aux.getId_persona());
                        String text = "Señor(a) " + person.getNombre_persona() + " su contraseña ha sido cambiada correctamente";
                        String subject = "Confirmación de cambio de contraseña";
                        send_validation send_mail = new send_validation();
                        send_mail.sendMessage(db, person, text, subject);
                        JOptionPane.showMessageDialog(vista, "Contraseña cambiada satisfactoriamente");
                        LOGGER.log(Level.INFO, "Volviendo al login");
                        login login = new login();
                        login.setVisible(true);
                        login_controller controller = new login_controller(login, db, LOGGER);
                        this.vista.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "La contraseña actual no coincide con la ingresada", "Error", JOptionPane.ERROR_MESSAGE);
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
        } else if(e.getSource() == this.vista.jButton2){    
            LOGGER.log(Level.INFO, "Volviendo al panel de {0}", person.getNombre_rol());
            switch(person.getNombre_rol()){
                case "admin" -> {
                    administrator admin = new administrator();
                    administrator_controller admin_controller = new administrator_controller(admin, LOGGER, db, person);
                    admin.setVisible(true);
                    this.vista.dispose();
                }
                case "usuario general" ->{
                    generalUser gu = new generalUser();
                    generalUser_controller guc = new generalUser_controller(gu, db, LOGGER, person);
                    gu.setVisible(true);
                    this.vista.dispose();
                }
                case "usuario basico" ->{
                    basicUser bu = new basicUser();
                    basicUser_controller buc = new basicUser_controller(bu,LOGGER,db,person);
                    bu.setVisible(true);
                    this.vista.dispose();
                }
            }
        }
    }
}
