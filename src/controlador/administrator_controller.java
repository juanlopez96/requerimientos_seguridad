/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo.database;
import modelo.persona;
import modelo.rol;
import vista.administrator;
import vista.change_password;
import vista.login;

/**
 *
 * @author Yessica
 */
public class administrator_controller implements ActionListener {

    administrator vista;
    Logger LOGGER;
    database db;
    persona person;
    ArrayList<rol> allRol;
    persona user;

    public administrator_controller(administrator vista, Logger LOGGER, database db, persona person) {
        this.vista = vista;
        this.LOGGER = LOGGER;
        this.db = db;
        this.person = person;
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jButton3.addActionListener((ActionListener) this);
        this.vista.jButton4.addActionListener((ActionListener) this);
        this.vista.jTextField2.setEnabled(false);
        this.vista.jTextField3.setEnabled(false);
        ArrayList<persona> allUsers = db.getAllPerson();
        allRol = db.getRol();
        this.vista.jComboBox1.setEnabled(false);
        this.user = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            //Ingresar cambio de contraseña
        } else if (e.getSource() == this.vista.jButton2) {
            LOGGER.log(Level.INFO, "Cerrando sesion");
            person = null;
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            this.vista.dispose();
        } else if (e.getSource() == this.vista.jButton3) {
            int id = 0;
            try {
                id = Integer.parseInt(this.vista.jTextField1.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "El campo ID debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            }
            String []selection = this.vista.jComboBox1.getSelectedItem().toString().split("-");
            int id_rol = Integer.parseInt(selection[0]);
            if(db.updateRol(id, id_rol)){
                LOGGER.log(Level.INFO, "Cambio de rol efectuado al usuario de id {0}",id);
                JOptionPane.showMessageDialog(vista, ("El rol asociado al id " + id +" ha sido cambiado satisfactoriamente"));
                changeState();
            }else{
                LOGGER.log(Level.SEVERE, "Error en el cambio de rol al usuario de id {0}",id);
                JOptionPane.showMessageDialog(vista, "Error al actualizar rol", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == this.vista.jButton4) {
            int id = 0;
            try {
                id = Integer.parseInt(this.vista.jTextField1.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "El campo ID debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (person.getId_persona() != id) {
                user = db.searchUser(id);
            } else {
                changeState();
            }
            if (user != null) {
                LOGGER.log(Level.INFO, "Busqueda efectuada del usuario de id {0}",id);
                this.vista.jComboBox1.setEnabled(true);
                this.vista.jTextField3.setText(user.getNombre_persona());
                allRol.stream().filter(x -> (x.getId() == Integer.parseInt(user.getNombre_rol()))).forEachOrdered(x -> {
                    this.vista.jTextField2.setText(x.getNombre());
                });
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                allRol.stream().filter(x -> (x.getId() != Integer.parseInt(user.getNombre_rol()))).forEachOrdered(x -> {
                    model.addElement(x.getId() + "-" + x.getNombre());
                });
                this.vista.jComboBox1.setModel(model);
            }
        }
    }

    private void changeState() {
        this.user = null;
        this.vista.jTextField2.setText("");
        this.vista.jTextField3.setText("");
        this.vista.jComboBox1.setModel(new DefaultComboBoxModel());
        this.vista.jComboBox1.setEnabled(false);
    }
}
