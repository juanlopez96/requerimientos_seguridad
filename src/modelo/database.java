/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

/**
 *
 * @author Yeiny
 */
public class database {

    private Connection con;
    private final String USER = "inventory";
    private final String PASS = "inventory";
    private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private Logger LOGGER;

    public database(Logger LOGGER) {
        con = null;
        this.LOGGER = LOGGER;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            con = null;
            LOGGER.log(Level.SEVERE, "Error al establecer la conexion: {0}", e);
            System.out.println("Error al establecer la conexion");
        }
    }

    public persona validateUser(String email, String pass) {
        persona user = null;
        try {
            String sql = "SELECT PERSONA.ID_PERSONA, ROL.NOMBRE_ROL, PERSONA.NOMBRE_PERSONA"
                    + " FROM PERSONA INNER JOIN ROL ON ROL.ID_ROL = PERSONA.ID_ROL WHERE"
                    + " PERSONA.CORREO_PERSONA = ? AND PERSONA.PASSWORD_PERSONA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new persona();
                user.setId_persona(rs.getInt(1));
                user.setNombre_rol(rs.getString(2));
                user.setNombre_persona(rs.getString(3));
                user.setCorreo_persona(email);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al establecer la conexion: {0}", e);
            user = null;
        }
        return user;
    }

    public boolean insertLog() {
        String sql = "SELECT * FROM LOG";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            boolean exist = false;
            while (rs.next()) {
                exist = true;
                Clob clob = rs.getClob("ARCHIVO_LOG");
                Reader inputdb = clob.getCharacterStream();
                int x;
                StringBuffer str = new StringBuffer();
                while ((x = inputdb.read()) > 0) {
                    str.append("").append((char) x);
                }
                File bitacora = new File("./security.log");
                BufferedReader br = new BufferedReader(new FileReader(bitacora));
                String line = br.readLine();
                while (line != null) {
                    str.append(line);
                    str.append("\n");
                    line = br.readLine();
                }
                br.close();
                File aux = new File("./auxiliar.log");
                if (!aux.exists()) {
                    aux.createNewFile();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(aux));
                bw.append(str);
                bw.close();
                sql = "UPDATE LOG SET LOG.ARCHIVO_LOG = ?";
                ps = con.prepareStatement(sql);
                File file = new File("./auxiliar.log");
                FileReader frd = new FileReader(file);
                ps.setCharacterStream(1, frd);
                int result = ps.executeUpdate();
                if (result == 1) {
                    System.out.println("OK");
                }
                frd.close();
                aux.delete();
                bitacora.delete();
            }
            if (!exist) {
                sql = "INSERT INTO LOG (LOG.ARCHIVO_LOG) VALUES (?)";
                ps = con.prepareStatement(sql);
                File file = new File("./security.log");
                FileReader frd = new FileReader(file);
                ps.setCharacterStream(1, frd);
                int result = ps.executeUpdate();
                if (result == 1) {
                    System.out.println("OK test");
                }
            }
        } catch (Exception e) {
            System.out.println("Error insert log " + e);
        }
        return false;
    }

}
