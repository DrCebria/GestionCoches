package coches;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Esta clase reúne todos los métodos necesarios para la gestión de los coches
 *
 * @author GerardCebriá
 */
public class GestionCoches {

    private Connection conexion;
    String titulos[] = {"Matricula", "Marca", "Modelo", "Color", "Ano", "Precio"};
    String filas[] = new String[6];
    String createTable = "CREATE TABLE IF NOT EXISTS coches ("
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
        String matricula = "null", marca = "null", modelo = "null", color = "null";
        int contador = 0, ano = 0, precio = 0;
        PreparedStatement ps;
        ResultSet rs;
        String linea;
        String[] trozos;

        try (FileReader fr = new FileReader(fichero)) {
            BufferedReader br = new BufferedReader(fr);
            while ((linea = br.readLine()) != null) {
                trozos = linea.split(":");
                matricula = trozos[0];
                marca = trozos[1];
                modelo = trozos[2];
                color = trozos[3];
                ano = Integer.parseInt(trozos[4]);
                precio = Integer.parseInt(trozos[5]);

                String sql = "INSERT INTO "
                        + "coches (matricula,marca,modelo,color,ano,precio)"
                        + "VALUES (?,?,?,?,?,?)";
                ps = conexion.prepareStatement(sql);
//                try {
                ps.setString(1, matricula);
                ps.setString(2, marca);
                ps.setString(3, modelo);
                ps.setString(4, color);
                ps.setInt(5, ano);
                ps.setInt(6, precio);
                if (ps.executeUpdate() == 1) {
                    contador++;
                }
//                } catch (SQLException ex) {
//                    Logger.getLogger(GestionCoches.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }

        } catch (IOException ex) {
            Logger.getLogger(GestionCoches.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contador;
    }

    public List<Coche> mostrarCoches() {
        List<Coche> listaCoches = new ArrayList<>();
        String sql = "SELECT * FROM coches;";
        String matricula, marca, modelo, color;
        int ano = 0, precio = 0;
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                matricula = rs.getString("Matricula");
                marca = rs.getString("Marca");
                modelo = rs.getString("Modelo");
                color = rs.getString("Color");
                ano = rs.getInt("Ano");
                precio = rs.getInt("Precio");

                Coche c = new Coche(matricula, marca, modelo, color, ano, precio);
                listaCoches.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionCoches.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCoches;
    }

    public File exportarCoches(TableModel datosCoche, Path archivo) throws IOException {
        File datosExportados;
        datosExportados = archivo.toFile();
        String text = "";
        int col = datosCoche.getColumnCount();
        int fil = datosCoche.getRowCount();
        for (int i = 0; i < fil; i++) {
            text = text.concat("Coche n?: " + i + "\n");
            for (int j = 0; j < col; j++) {
                if (j == 0) {
                    text = text.concat("Matricula " + "\t.........." + datosCoche.getValueAt(i, j));
                }
                if (j == 1) {
                    text = text.concat("Marca " + "\t.........." + datosCoche.getValueAt(i, j));
                }
                if (j == 2) {
                    text = text.concat("Modelo " + "\t.........." + datosCoche.getValueAt(i, j));
                }
                if (j == 3) {
                    text = text.concat("Color " + "\t.........." + datosCoche.getValueAt(i, j));
                }
                if (j == 4) {
                    text = text.concat("A?o" + "\t.........." + datosCoche.getValueAt(i, j));
                }
                if (j == 5) {
                    text = text.concat("Precio" + "\t.........." + datosCoche.getValueAt(i, j));
                }
            }
            FileWriter fw = new FileWriter(datosExportados);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
        }

        return datosExportados;
    }
}
