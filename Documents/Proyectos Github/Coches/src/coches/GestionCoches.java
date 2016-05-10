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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase reúne todos los métodos necesarios para la gestión de los coches
 *
 * @author GerardCebriá
 */
public class GestionCoches {

    
    private Connection conexion;
    String titulos[] = {"Matricula", "Marca", "Modelo", "Color", "Ano", "Precio"};
    String filas[] = new String[6];
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

    /**
     * El método consiste en establecer la conexión con la base de datos SQL
     * "coches"
     *
     * @return Conection
     */
    public Connection crearConexion() {
        
        try {
            this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/coches", "root", "root");

        } catch (SQLException e) {
            //LabelConexion.setText(e.getMessage() + " -- " + e.getErrorCode());
            throw new RuntimeException(e);
        }

        return conexion;
    }

    /**
     * En este método se pretende crear la tabla en el archivo SQL
     *
     * @return g<strong> true </strong> si se ha completado,
     * <strong>false</strong>
     * si no se ha creado.
     */
    public boolean crearTablaCoches() {
        PreparedStatement ps;
        
        boolean comp = false;
        try {

            ps = conexion.prepareStatement(createTable);
            ps.execute();
            comp = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return comp;
    }

    /**
     * El siguiente método carga en la tabla el contenido del fichero pasado
     * como parámetro.
     *
     * @param fichero Un <code> file</code> del que sacaremos el texto.
     * @return número de coches que hay
     */
    public int cargarTablaCoches(File fichero) throws SQLException {
        String matricula, marca, modelo, color, ano, precio;
        PreparedStatement ps;
        ResultSet rs;
        String linea;
        String[] trozos;
        int contador = 0;
        try (FileReader fr = new FileReader(fichero)) {
            BufferedReader br = new BufferedReader(fr);
            while ((linea = br.readLine()) != null) {
                contador++;

                trozos = linea.split(":");
                matricula = trozos[0];
                marca = trozos[1];
                modelo = trozos[2];
                color = trozos[3];
                ano = trozos[4];
                precio = trozos[5];
                ps = conexion.prepareStatement("INSERT INTO coches values ('?', '?', '?', '?', '?')");
                ps.setString(1, matricula);
                ps.setString(2, marca);
                ps.setString(3, modelo);
                ps.setString(4, color );
                ps.setString(5, ano);
                ps.setString(6, precio);
                
                rs = ps.executeQuery();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionCoches.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionCoches.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contador;
    }
}



//public String[][] mostrarCoches(String sql) {
//
//
//    }

