package com.zoho.booktickets.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zoho.booktickets.model.*;
import com.zoho.booktickets.constant.*;
import com.zoho.booktickets.connection.DatabaseConnection;
import java.util.List;
import java.util.ArrayList;

public class UserService{
    
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();
              PreparedStatement ps = conn.prepareStatement(QueryStatement.CREATE_USER_TABLE)) {              
              ResultSet resultSet;
              System.out.println("Address Successfully Created!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public long checkMovieOrAdd(User user) {
		try (Connection conn = DatabaseConnection.getConnect(); PreparedStatement ps =
        conn.prepareStatement(QueryStatement.GET_USER_ID)) {

            ps.setString(1, user.getEmailId());
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(Constant.USER_ID);
			}	
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        return add(user);
	}
    
    public long add(User user){
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_USER,
                Statement.RETURN_GENERATED_KEYS)) {
            setPs(ps, user);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
				return resultSet.getLong(Constant.USER_ID);
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    
    public User read(int userId){
        User user = null;
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_USER)) {
            
            pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return null;
			}
            user =  getRs(rs);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return user;
    }
	
	public List<User> readAll(){
        List<User> users = new ArrayList<User>();
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_USER_ALL)) {
            
			ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                users.add(getRs(rs));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return users;
    }
    
    public User getRs(ResultSet resultSet) throws SQLException {
		return new User(resultSet.getLong(Constant.USER_ID), resultSet.getString(Constant.USER_NAME),
				resultSet.getString(Constant.MOBILE_NO),resultSet.getString(Constant.EMAIL_ID), resultSet.getString(Constant.SEX));
	}

	public void setPs(PreparedStatement ps, User user) throws SQLException {
		ps.setString(1, user.getName());
        ps.setString(2, user.getMobileNo());
        ps.setString(3, user.getEmailId());
        ps.setString(4, user.getSex());
	}
    public static void main(String[] args){
        System.out.println(new UserService().checkMovieOrAdd(new User("Santheesh","7904188021","santheesh62@gmail.com","Male")));
    }
}