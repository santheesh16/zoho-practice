package com.zoho.booktickets.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.constant.QueryStatement;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.port.UserPort;

public class UserService{
    
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();
              PreparedStatement ps = conn.prepareStatement(QueryStatement.CREATE_USER_TABLE);) {          
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
                Statement.RETURN_GENERATED_KEYS);) {
            setPs(ps, user);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
                long userId = resultSet.getLong(Constant.USER_ID);
                UserPort.add(userId);
				return userId;
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    
    public User read(long userId){
        User user = null;
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_USER)) {
            
            pstmt.setLong(1, userId);
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
    
    public void update(User user, long userId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.UPADE_USER)) {

            setPs(ps,user);
            ps.setFloat(6, userId);
            System.out.println(ps.executeUpdate());
			if (ps.executeUpdate() == 1) {
                UserPort.update(userId);
				System.out.println("User Details Successfully Updated");
			}

		} catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
    
    
    public void delete(long userId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = DatabaseConnection.getConnect().prepareStatement(QueryStatement.DELETE_USER)) {
			ps.setLong(1, userId);

			if (ps.executeUpdate() != 1) {
				System.out.println("User not deleted");
			}
            UserPort.delete(userId);
		} catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
    
    public User getRs(ResultSet resultSet) throws SQLException {
		return new User(resultSet.getLong(Constant.USER_ID),resultSet.getString(Constant.USER_ROLE),resultSet.getString(Constant.USER_NAME), resultSet.getString(Constant.MOBILE_NO),resultSet.getString(Constant.EMAIL_ID), resultSet.getString(Constant.SEX));
	}

	public void setPs(PreparedStatement ps, User user) throws SQLException {
		ps.setString(1, user.getUserRole() == null ? "user" : "admin");
        ps.setString(2, user.getName());
        ps.setString(3, user.getMobileNo());
        ps.setString(4, user.getEmailId());
        ps.setString(5, user.getSex());
	}
    
    public static void main(String[] args){
        //new UserService().add(new User("Santheesh16","9597516993","santh16@gamail.com","Male"));
        //new UserService().update(new User(2, "admin", "Santheesh16","9597516993","santh16@gamail.com","Male"), 1);
        //new UserService().delete(4);
        
    }
}