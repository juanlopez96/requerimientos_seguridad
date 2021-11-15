/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package requerimientos_seguridad;

import controlador.login_controller;
import java.net.InetAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import modelo.database;
import vista.login;

/**
 *
 * @author juan
 */
public class Requerimientos_seguridad {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            InetAddress ip = InetAddress.getLocalHost();
            Logger LOGGER = Logger.getLogger("security.security");
            Handler consoleHandler = new ConsoleHandler();
            Handler fileHandler = new FileHandler("./security.log", false);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            LOGGER.log(Level.INFO, "----------Iniciando sistema----------");
            LOGGER.log(Level.INFO, "IP de conexion: {0}", ip);
            database db = new database(LOGGER);
            login login = new login();
            login.setVisible(true);
            login_controller controller = new login_controller(login, db, LOGGER);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    LOGGER.log(Level.INFO, "----------Cerrando sistema----------");
                    db.insertLog();
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
