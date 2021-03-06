/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.database;
import modelo.encrypt;
import modelo.persona;
import vista.administrator;
import vista.basicUser;
import vista.forgot_password;
import vista.generalUser;
import vista.login;
import vista.register;

/**
 *
 * @author Yeiny
 */
public class login_controller implements ActionListener {

    login vista;
    database db;
    persona user;
    Logger LOGGER;

    public login_controller(login vista, database db, Logger LOGGER) {
        this.vista = vista;
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jButton3.addActionListener((ActionListener) this);
        this.db = db;
        this.LOGGER = LOGGER;
        user = new persona();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            String pass = "";
            encrypt enc = new encrypt();

            pass = enc.toEncrypt(this.vista.jPasswordField1.getText());
            user = db.validateUser(this.vista.jTextField1.getText(), pass);
            if (user != null) {
                LOGGER.log(Level.INFO, "Usuario Loggeado: {0}", user.getCorreo_persona());
                LOGGER.log(Level.INFO, "Tipo de usuario: {0}", user.getNombre_rol());
                if (user.getNombre_rol().equals("admin")) {
                    administrator admin = new administrator();
                    administrator_controller admin_controller = new administrator_controller(admin, LOGGER, db, user);
                    admin.setVisible(true);
                    this.vista.dispose();
                }else if(user.getNombre_rol().equals("usuario general")){
                    generalUser gu = new generalUser();
                    generalUser_controller guc = new generalUser_controller(gu, db, LOGGER, user);
                    gu.setVisible(true);
                    this.vista.dispose();
                    
                }else if(user.getNombre_rol().equals("usuario basico")){
                    basicUser bu = new basicUser();
                    basicUser_controller buc = new basicUser_controller(bu,LOGGER,db,user);
                    bu.setVisible(true);
                    this.vista.dispose();
                }
            } else {
                LOGGER.log(Level.WARNING, "Usuario y/o contrase??a incorrecto(s): \n"
                        + "Intento de acceso: {0}", this.vista.jTextField1.getText());
            }
        } else if(e.getSource() == this.vista.jButton2){
            register rg = new register();
            register_controller rgc = new register_controller(rg, db, LOGGER);
            rg.setVisible(true);
            LOGGER.log(Level.INFO, "Ingreso a registrar usuario");
            this.vista.dispose();
        }else if (e.getSource() == this.vista.jButton3) {
            forgot_password fp = new forgot_password();
            forgot_password_controller fpcontroller = new forgot_password_controller(fp, LOGGER, db);
            fp.setVisible(true);
            LOGGER.log(Level.INFO, "Ingreso a reestablecimiento de contrase??a");
            this.vista.dispose();
        }
    }
}
