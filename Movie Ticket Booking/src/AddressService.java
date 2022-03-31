package com.zoho.booktickets.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.zoho.booktickets.constant.*;
import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.model.*;


public class AddressService{
	
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();PreparedStatement pstmt = conn.prepareStatement(QueryStatement.CREATE_ADDRESS_TABLE)) {              
            pstmt.executeUpdate();
            System.out.println("Address Successfully Created!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
    public long add(Address address){
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_ADDRESS,
                Statement.RETURN_GENERATED_KEYS)) {
            setPs(ps, address);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
				return resultSet.getLong(Constant.ADDRESS_ID);
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    
    public Address read(long addressId){
         Address address = null;
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_ADDRESS)) {
            ps.setLong(1, addressId);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}
            address = getRs(rs);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return address;
    }
    
    public void setPs(PreparedStatement ps,Address address) throws SQLException{
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getState());
            ps.setString(4, address.getPincode());
            ps.setString(5, address.getLandmark());
    }
    
    public Address getRs(ResultSet resultSet) throws SQLException {
		return new Address(resultSet.getLong(Constant.ADDRESS_ID),resultSet.getString(Constant.STREET),
				resultSet.getString(Constant.CITY),resultSet.getString(Constant.STATE),
                resultSet.getString(Constant.PINCODE),resultSet.getString(Constant.LANDMARK));
    }
}