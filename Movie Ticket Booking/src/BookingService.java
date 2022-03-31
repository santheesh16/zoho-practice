package com.zoho.booktickets.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.sql.Array;

import com.zoho.booktickets.model.*;
import com.zoho.booktickets.constant.QueryStatement;
import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.constant.Constant;

public class BookingService{
    
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.CREATE_BOOKING_TABLE)) {              
            pstmt.executeUpdate();
            System.out.println("Booking Successfully Created!!");  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public long add(Booking booking){
         long id = 0;
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_BOOKING,
                Statement.RETURN_GENERATED_KEYS)) {
            setPs(ps, booking);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
				return resultSet.getLong(Constant.BOOKING_ID);
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }
    
     public Booking read(long bookedId){
         Booking booking = null;
         try (Connection conn = DatabaseConnection.getConnect();PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_BOOKING)) {
            pstmt.setLong(1, bookedId);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
            booking = getRs(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return booking;
    }
    
    public List<Booking> searchBooked(long theaterId, int screenNo, LocalDateTime localDateTime){
         List<Booking> bookingList = new ArrayList<Booking>();
         try (Connection conn = DatabaseConnection.getConnect();PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SEARCH_BOOKED)) {
            pstmt.setLong(1, theaterId);
            pstmt.setLong(2, screenNo);
            pstmt.setObject(3, localDateTime);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
                bookingList.add(getRs(rs));    
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return bookingList;
    }
    
    public List<Booking> readAll(){
         List<Booking> bookingList = new ArrayList<Booking>();
         try (Connection conn = DatabaseConnection.getConnect();PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_BOOKING_ALL)) {
            
			ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                bookingList.add(getRs(rs));    
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return bookingList;
    }
    
    public void delete(long bookedId) {
        long id = 0;
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.DELETE_BOOKING)) {
			ps.setLong(1, bookedId);
            if (ps.executeUpdate() == 1) {
				System.out.println("Your booked tickets cancelled successfully!!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public void update(Booking booking, long bookedId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.UPADE_BOOKING_SEATS)) {
            setPs(ps,booking);
            ps.setArray(1, DatabaseConnection.getConnect().createArrayOf("text", booking.getNoOfBookedSeats()));
            ps.setLong(2, bookedId);
            
			if (ps.executeUpdate() == 1) {
				System.out.println("Booking Seats Details Successfully Updated");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	} 
    
     public String[] arrayToString(Array array)  throws SQLException{
        return (String[])array.getArray();
    }
    
    public void setPs(PreparedStatement ps,Booking booking) throws SQLException{
        
            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getTheaterId());
            ps.setLong(3, booking.getMovieId());
            ps.setLong(4, booking.getScreenId());
            ps.setArray(5, DatabaseConnection.getConnect().createArrayOf("text", booking.getNoOfBookedSeats()));
            ps.setObject(6, booking.getShowDateTime());
            ps.setObject(7, booking.getBookedDateTime());
    }
    
    public Booking getRs(ResultSet resultSet) throws SQLException {
		return new Booking(resultSet.getLong(Constant.BOOKING_ID), resultSet.getLong(Constant.BOOK_USER_ID),resultSet.getLong(Constant.BOOK_THEATER_ID),
				resultSet.getLong(Constant.BOOK_MOVIE_ID),resultSet.getInt(Constant.SCREEN_NO),arrayToString(resultSet.getArray(Constant.BOOKED_SEATS)),
                resultSet.getObject(Constant.SHOW_DATE_TIME, LocalDateTime.class),resultSet.getObject(Constant.BOOKED_DATE_TIME, LocalDateTime.class));
    }
}