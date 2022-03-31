package com.zoho.booktickets.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zoho.booktickets.model.User;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Address;
import com.zoho.booktickets.model.Booking;
import com.zoho.booktickets.constant.QueryStatement;
import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.model.MovieType;
import com.zoho.booktickets.model.MovieStatus;


public class ControllerDB{
    
    Connection conn = null;
    Statement state = null;
        
    DatabaseConnection db = new DatabaseConnection();
    
    
    
   
    
    
    
}
