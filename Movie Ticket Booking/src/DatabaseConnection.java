package com.zoho.booktickets.connection;

import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.postgresql.Driver;
import java.io.FileReader;

import com.zoho.booktickets.constant.Constant;

public class DatabaseConnection{
    
    static Connection conn = null;
    public static Connection getConnect(){
        
         try (FileReader fReader = new FileReader("C:\\1zoho\\Movie Ticket Booking\\resources\\db.properties");){
                Properties db = new Properties();
                db.load(fReader);
                Class.forName(db.getProperty("DB_DRIVER_CLASS"));
                conn = DriverManager.getConnection(db.getProperty("URL"), db.getProperty("USERNAME"), db.getProperty("PASSWORD"));
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void main(String[] args){
        
        System.out.println(getConnect());   
    }
}
