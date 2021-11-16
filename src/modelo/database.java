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
import java.util.ArrayList;

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

    public ArrayList<persona> getAllPerson() {
        ArrayList<persona> all = new ArrayList();
        String sql = "SELECT PERSONA.ID_PERSONA, PERSONA.NOMBRE_PERSONA, PERSONA.CORREO_PERSONA, PERSONA.ID_ROL FROM PERSONA";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                persona aux = new persona();
                aux.setId_persona(rs.getInt(1));
                aux.setNombre_persona(rs.getString(2));
                aux.setCorreo_persona(rs.getString(3));
                aux.setNombre_rol(rs.getString(4));
                all.add(aux);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al validar usuarios {0}", e);
        }
        return all;
    }

    public ArrayList<rol> getRol() {
        ArrayList<rol> all = new ArrayList();
        try {
            String sql = "SELECT * FROM ROL";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rol aux = new rol();
                aux.setId(rs.getInt(1));
                aux.setNombre(rs.getString(2));
                all.add(aux);
            }
            return all;
        } catch (Exception e) {
            System.out.println("Error al consutlar roles " + e);
        }
        return all;
    }
    
    public persona searchUser(int id) {
        persona p = null;
        String sql = "SELECT PERSONA.NOMBRE_PERSONA, PERSONA.ID_ROL FROM PERSONA WHERE PERSONA.ID_PERSONA = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                p = new persona();
                p.setNombre_persona(rs.getString(1));
                p.setNombre_rol(rs.getString(2));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Error al consultar usuario" + e);
        }
        return p;
    }
    
    public boolean updateRol(int id_persona, int id_rol) {
        String sql = "UPDATE PERSONA SET PERSONA.ID_ROL = ? WHERE PERSONA.ID_PERSONA = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_rol);
            ps.setInt(2, id_persona);
            int result = ps.executeUpdate();
            return result == 1;
        } catch (Exception e) {
            System.out.println("Error al actulizar rol " + e);
            return false;
        }
    }
}
