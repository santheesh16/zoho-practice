package com.zoho.booktickets.connection;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.zoho.booktickets.constant.Constant;

public class DatabaseConnection {

    static Connection conn = null;

    public static Connection getConnect() {

        try (FileReader fReader = new FileReader(Constant.DB_FILE);) {
            Properties db = new Properties();

            db.load(fReader);
            Class.forName(db.getProperty("DB_DRIVER_CLASS")).newInstance();
            Properties props = new Properties();
            props.setProperty("user", db.getProperty("USERNAME"));
            props.setProperty("password", db.getProperty("PASSWORD"));
            conn = DriverManager.getConnection(db.getProperty("URL"), props);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
