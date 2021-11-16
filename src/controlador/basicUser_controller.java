/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import modelo.database;
import modelo.persona;
import modelo.producto;
import vista.basicUser;
import vista.change_password;
import vista.login;

/**
 *
 * @author Valentina
 */
public class basicUser_controller implements ActionListener {

    basicUser vista;
    Logger LOGGER;
    database db;
    persona person;
    Object[] header = {"ID", "NOMBRE", "CANTIDAD"};

    public basicUser_controller(basicUser vista, Logger LOGGER, database db, persona person) {
        this.vista = vista;
        this.LOGGER = LOGGER;
        this.db = db;
        this.person = person;
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jButton3.addActionListener((ActionListener) this);
        this.vista.jButton4.addActionListener((ActionListener) this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            
            int id_prod = 0;
            try {
                id_prod = Integer.parseInt(this.vista.jTextField1.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El ID del producto debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.log(Level.INFO, "Busqueda de producto con id {0}",id_prod);
            producto prod = db.getProd(id_prod);
            if (prod != null) {
                Object[][] row = new Object[1][3];

                row[0][0] = prod.getId_producto();
                row[0][1] = prod.getNombre_producto();
                row[0][2] = prod.getCantidad_producto();
                JTable table = new JTable(row, header);
                JScrollPane js = new JScrollPane(table);

                js.setPreferredSize(new Dimension(100, 100));
                this.vista.jPanel1.removeAll();
                this.vista.jPanel1.setLayout(new BorderLayout());
                this.vista.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Productos", TitledBorder.CENTER, TitledBorder.TOP));
                this.vista.jPanel1.add(js);
                this.vista.jPanel1.updateUI();

            } else {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado");
            }

        } else if (e.getSource() == this.vista.jButton2) {
            LOGGER.log(Level.INFO, "Cerrando sesion");
            person = null;
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            this.vista.dispose();
        } else if (e.getSource() == this.vista.jButton3) {
            LOGGER.log(Level.INFO, "Listado de todos los productos");
            ArrayList<producto> allprods = db.getAllProds();
            if (allprods.size() > 0) {
                Object[][] row = new Object[allprods.size()][3];
                for (int i = 0; i < allprods.size(); i++) {
                    row[i][0] = allprods.get(i).getId_producto();
                    row[i][1] = allprods.get(i).getNombre_producto();
                    row[i][2] = allprods.get(i).getCantidad_producto();
                }
                JTable table = new JTable(row, header);
                JScrollPane js = new JScrollPane(table);
                js.setPreferredSize(new Dimension(100, 100));
                this.vista.jPanel1.removeAll();
                this.vista.jPanel1.setLayout(new BorderLayout());
                this.vista.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Productos", TitledBorder.CENTER, TitledBorder.TOP));
                this.vista.jPanel1.add(js);
                this.vista.jPanel1.updateUI();
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontraron productos registrados");
            }
        } else if(e.getSource() == this.vista.jButton4){
            /*Ingreso a cambio de contraseña*/
        }
    }
}
