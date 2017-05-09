/*
 * DB Connection class
 * 
 */
package com.formsubmission.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dbConnection {

    public Connection getConnectionInstance() {
        Connection conn = null;
        Properties prop;
        try {
            InputStream inputStream = new FileInputStream("/home/deepanshu/glassfish-4.1.1/glassfish/domains/domain1/config/DatabaseDetails.properties");

            prop = new Properties();
            prop.load(inputStream);

            Class.forName(prop.getProperty("driver"));
            conn = DriverManager.getConnection(
                    prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));

        } catch (ClassNotFoundException ex) {
            System.out.println("class not found : " + ex.getMessage());
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("database exception : " + ex.getMessage());
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found exception : " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("exception : " + ex.getMessage());
            ex.printStackTrace();
        }
        return conn;
    }
}
