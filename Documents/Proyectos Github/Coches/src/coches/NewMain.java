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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gerard
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       List<Coche> listaCoches;
       GestionCoches gestion = new GestionCoches();
        String [] titulos = {"Matricula", "Marca", "Modelo", "Color", "Ano", "Precio"};
        String [] fila = new String [6];
        List<Coche> listaCoches2;
        listaCoches2 = gestion.mostrarCoches();
        DefaultTableModel model = new DefaultTableModel (null, titulos);
        for (Coche c : listaCoches2){
            fila[0] = c.getMatricula();
            fila[1] = c.getMarca();
            fila[2] = c.getModelo();
            fila[3] = c.getColor();
            fila[4] = c.getAno().toString();
            fila[5] = c.getPrecio().toString();
            model.addRow(fila);
        }
        String tira = model.toString();
        System.out.println(tira);
    }
    
}
