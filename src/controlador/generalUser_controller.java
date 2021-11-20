/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.login_controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import modelo.database;
import modelo.persona;
import vista.generalUser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import modelo.producto;
import vista.change_password;
import vista.login;

/**
 *
 * @author Alejandra
 */
public class generalUser_controller implements ActionListener {

    generalUser vista;
    database db;
    Logger LOGGER;
    persona person;
    Object[] header = {"ID", "NOMBRE", "CANTIDAD"};

    public generalUser_controller(generalUser vista, database db, Logger LOGGER, persona person) {
        this.vista = vista;
        this.LOGGER = LOGGER;
        this.db = db;
        this.person = person;
        this.vista.jTextField1.setEditable(false);
        this.vista.jButton1.addActionListener((ActionListener) this);
        this.vista.jButton2.addActionListener((ActionListener) this);
        this.vista.jButton4.addActionListener((ActionListener) this);
        this.vista.jButton5.addActionListener((ActionListener) this);
        this.vista.jButton6.addActionListener((ActionListener) this);
        this.vista.jButton7.addActionListener((ActionListener) this);
        this.vista.jButton8.addActionListener((ActionListener )this);
        this.vista.jButton7.setEnabled(false);
        this.vista.jTextField2.setEditable(false);
        this.vista.jTextField3.setEditable(false);
        updateFields();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.jButton1) {
            person = null;
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            this.vista.dispose();
        } else if (e.getSource() == this.vista.jButton2) {
            showOptions("buscar");
        } else if (e.getSource() == this.vista.jButton4) {
            showOptions("insertar");
        } else if (e.getSource() == this.vista.jButton5) {
            showOptions("modificar");
        } else if (e.getSource() == this.vista.jButton6) {
            showOptions("eliminar");
        } else if (e.getSource() == this.vista.jButton7) {
            doAction(this.vista.jButton7.getText());
        } else if(e.getSource() == this.vista.jButton8){
            LOGGER.log(Level.INFO, "Ingreso a cambio de contraseña");
            change_password change = new change_password();
            changepassword_controller changepass_controller = new changepassword_controller(change, LOGGER, db, person);
            change.setVisible(true);
            this.vista.dispose();
        }
    }

    private void updateFields() {
        this.vista.jTextField1.setText("");
        this.vista.jTextField2.setText("");
        this.vista.jTextField3.setText("");
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
            js.setPreferredSize(new Dimension(430, 100));
            this.vista.jPanel1.removeAll();
            this.vista.jPanel1.setLayout(new BorderLayout());
            this.vista.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Productos", TitledBorder.CENTER, TitledBorder.TOP));
            this.vista.jPanel1.add(js);
            this.vista.jPanel1.updateUI();
        } else {
            JOptionPane.showMessageDialog(vista, "No se encontraron productos registrados");
        }
    }

    private void showOptions(String action) {
        switch (action) {
            case "buscar" ->{
                this.vista.jPanel1.removeAll();
                this.vista.jPanel1.updateUI();
                this.vista.jTextField1.setText("");
                this.vista.jTextField1.setEditable(true);
                this.vista.jTextField2.setEditable(false);
                this.vista.jTextField3.setEditable(false);
                this.vista.jButton7.setText("Buscar producto");
                this.vista.jButton7.setEnabled(true);
            }

            case "insertar" ->{
                updateFields();
                this.vista.jTextField1.setText("");
                this.vista.jTextField1.setEditable(true);
                this.vista.jTextField2.setEditable(true);
                this.vista.jTextField3.setEditable(true);
                this.vista.jButton7.setText("Insertar producto");
                this.vista.jButton7.setEnabled(true);
            }
            case "modificar" -> {
                updateFields();
                this.vista.jTextField1.setText("");
                this.vista.jTextField1.setEditable(true);
                this.vista.jTextField2.setEditable(true);
                this.vista.jTextField3.setEditable(true);
                this.vista.jButton7.setText("Modificar producto");
                this.vista.jButton7.setEnabled(true);
            }
            case "eliminar" -> {
                updateFields();
                this.vista.jTextField1.setText("");
                this.vista.jTextField1.setEditable(true);
                this.vista.jTextField2.setEditable(false);
                this.vista.jTextField3.setEditable(false);
                this.vista.jButton7.setText("Eliminar producto");
                this.vista.jButton7.setEnabled(true);
            }
        }
    }

    private void doAction(String action) {
        switch (action) {
            case "Buscar producto" -> {
                int id_prod = 0;
                try {
                    id_prod = Integer.parseInt(this.vista.jTextField1.getText());
                    LOGGER.log(Level.INFO, "Buscando producto de id {0}", id_prod);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this.vista, "El campo ID debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
                }
                producto prod = db.getProd(id_prod);
                if (prod != null) {
                    updateFields();
                    Object[][] row = new Object[1][3];
                    row[0][0] = prod.getId_producto();
                    row[0][1] = prod.getNombre_producto();
                    row[0][2] = prod.getCantidad_producto();
                    JTable table = new JTable(row, header);
                    JScrollPane js = new JScrollPane(table);

                    js.setPreferredSize(new Dimension(430, 100));
                    this.vista.jPanel1.removeAll();
                    this.vista.jPanel1.setLayout(new BorderLayout());
                    this.vista.jPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Productos", TitledBorder.CENTER, TitledBorder.TOP));
                    this.vista.jPanel1.add(js);

                    this.vista.jPanel1.updateUI();

                } else {
                    JOptionPane.showMessageDialog(vista, "Producto no encontrado");
                }
            }
            case "Insertar producto" -> {
                int id_prod = 0;
                int cantidad_proc = 0;
                try {
                    id_prod = Integer.parseInt(this.vista.jTextField1.getText());
                    cantidad_proc = Integer.parseInt(this.vista.jTextField3.getText());
                    producto aux = new producto();
                    aux.setId_producto(id_prod);
                    aux.setNombre_producto(this.vista.jTextField2.getText());
                    aux.setCantidad_producto(cantidad_proc);
                    if (db.insertProduct(aux)) {
                        LOGGER.log(Level.INFO, "Producto insertado IDPRODUCTO {0}", id_prod);
                        JOptionPane.showMessageDialog(this.vista, "Producto insertado");
                        this.vista.jPanel1.removeAll();
                        this.vista.jPanel1.updateUI();
                        updateFields();
                    } else {
                        LOGGER.log(Level.SEVERE, "Error al insertar producto IDPRODUCTO {0}", id_prod);
                        JOptionPane.showMessageDialog(this.vista, "Error al insertar producto", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this.vista, "El campo ID y cantidad deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "Modificar producto" -> {
                int id_prod = 0;
                int cantidad_proc = 0;
                try {
                    id_prod = Integer.parseInt(this.vista.jTextField1.getText());
                    cantidad_proc = Integer.parseInt(this.vista.jTextField3.getText());
                    producto aux = new producto();
                    aux.setId_producto(id_prod);
                    aux.setNombre_producto(this.vista.jTextField2.getText());
                    aux.setCantidad_producto(cantidad_proc);
                    int answ = JOptionPane.showConfirmDialog(vista, "¿Está seguro de modificar este producto?");
                    if (answ == JOptionPane.OK_OPTION) {
                        if (db.updateProduct(aux)) {
                            LOGGER.log(Level.INFO, "Producto modificado IDPRODUCTO {0}", id_prod);
                            JOptionPane.showMessageDialog(vista, "Producto modificado");
                            this.vista.jPanel1.removeAll();
                            this.vista.jPanel1.updateUI();
                            updateFields();
                        } else {
                            LOGGER.log(Level.SEVERE, "Error al modificar producto IDPRODUCTO {0}", id_prod);
                            JOptionPane.showMessageDialog(this.vista, "Error al modificar producto", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        LOGGER.log(Level.WARNING, "Modificacion de producto cancelada IDPRODUCTO {0}", id_prod);
                        JOptionPane.showMessageDialog(vista, "Producto NO modificado");
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this.vista, "El campo ID y cantidad deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
            case "Eliminar producto" -> {
                int id_prod = 0;
                try {
                    id_prod = Integer.parseInt(this.vista.jTextField1.getText());
                    int answ = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este producto?");
                    if (answ == JOptionPane.OK_OPTION) {
                        if (db.deleteProduct(id_prod)) {
                            JOptionPane.showMessageDialog(vista, "Producto Eliminado");
                            LOGGER.log(Level.INFO, "Producto eliminado IDPRODUCTO {0}", id_prod);
                            this.vista.jPanel1.removeAll();
                            this.vista.jPanel1.updateUI();
                            updateFields();
                        } else {
                            LOGGER.log(Level.SEVERE, "Error al eliminar el producto IDPRODUCTO {0}", id_prod);
                            JOptionPane.showMessageDialog(this.vista, "Error al eliminar producto", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        LOGGER.log(Level.WARNING, "Eliminación de producto cancelada IDPRODUCTO {0}", id_prod);
                        JOptionPane.showMessageDialog(vista, "Producto NO eliminado");
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this.vista, "El campo ID y cantidad deben ser numéricos", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }
}
