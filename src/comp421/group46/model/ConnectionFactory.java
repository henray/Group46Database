/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp421.group46.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Katie Lin
 */
public class ConnectionFactory {
    private static ConnectionFactory cf = new ConnectionFactory();
    
    private ConnectionFactory(){
    }
    public static ConnectionFactory getConnectionFactory(){
        return cf;
    }
    
    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver ( new org.postgresql.Driver() ) ;
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        return DriverManager.getConnection (url,"cs421g46", "*Group_46*") ;
    }
}
