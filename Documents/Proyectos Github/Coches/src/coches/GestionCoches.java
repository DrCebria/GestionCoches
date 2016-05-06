/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coches;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author daw1
 */
public class GestionCoches {

    private Connection conexion;
    String direccion = "jdbc:mysql://localhost:3306/parques";
    String titulos[] = {"Matricula", "Marca", "Modelo", "Color", "Ano", "Precio"};
    String filas[] = new String [6];
    String createTable = "CREATE TABLE coches ("
            + "  Matricula varchar(45) NOT NULL,"
            + "  Marca varchar(45) NOT NULL,"
            + "  Modelo varchar(45) NOT NULL,"
            + "  Color varchar(45) NOT NULL,"
            + "  Ano int(11) NOT NULL,"
            + "  Precio int(11) NOT NULL,"
            + "  PRIMARY KEY (Matricula)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    DefaultTableModel modelo = new DefaultTableModel(null, titulos);
    static PreparedStatement ps;
    static ResultSet rs;
    static String linea;
    static String[] trozos;

    public Connection crearConexion() {
        try {
            conexion = DriverManager.getConnection(direccion, "root", "root");

        } catch (SQLException e) {
            //LabelConexion.setText(e.getMessage() + " -- " + e.getErrorCode());
            throw new RuntimeException(e);
        }

        return conexion;
    }

    public boolean crearTablaCoches() {
        boolean comp = false;
        try {

            ps = conexion.prepareStatement(createTable);
            rs = ps.executeQuery();
            comp = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return comp;
    }

    public int cargarTablaCoches(File fichero) {
        int contador = 0;
        try (FileReader fr = new FileReader(fichero)) {
            BufferedReader br = new BufferedReader(fr);
            while (br.readLine() != null) {
                contador++;
                linea = br.readLine();
                trozos = linea.split(":");
                
                
            }
            
        } catch (IOException ex) {
            throw new RuntimeException(ex);

        }
        return contador;
    }

    public String[][] mostrarCoches(String sql) {

    }
}
