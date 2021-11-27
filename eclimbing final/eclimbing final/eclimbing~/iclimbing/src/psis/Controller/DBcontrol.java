/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package psis.Controller;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class DBcontrol {
    // JDBC driver name and database URL
    // static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

    //  Database credentials
    String USER = "root";
    String PASSWORD = "";
    String DB_URL = "jdbc:mysql://localhost:3306/iclimbing";
    String driver = "com.mysql.jdbc.Driver";
    Connection dbCon;
    Statement stmt;
    PreparedStatement pStmt;
    ResultSet rs;

    public DBcontrol() {
    }

    public void createDBcon() {

        //  STEP 1: Setup the Driver
        try {
            //Load the JDBC driver class dynamically.
            Driver d = (Driver) Class.forName(driver).newInstance();
            DriverManager.registerDriver(d);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Loading Database Driver\n\n" + e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //  STEP 2: Create connection to database using database URL
        try {
            dbCon = DriverManager.getConnection(this.DB_URL,this.USER,this.PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Creating Database Connection\n\n" + e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        //  STEP 3: Initializing SQL Statement Oject
        try {
            stmt = dbCon.createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error Creating DataBase Statement\n\n" + ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void terminateDBcon() {
        try {
            dbCon.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Terminating Database Connection\n\n" + e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

}
