package com.zoho.booktickets.service;

import java.sql.Array;
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
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.port.TheaterPort;

public class TheaterService{
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.CREATE_THEATER_TABLE)) {              
            pstmt.executeUpdate();
            System.out.println("Address Successfully Created!!");
        }catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
    
    public long add(Theater theater){
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_THEATER,
                Statement.RETURN_GENERATED_KEYS)) {
            
            setPs(ps, theater);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
                long theaterId = resultSet.getLong(Constant.THEATER_ID);
                TheaterPort.add(theaterId); // Calling Another Port 
				return theaterId;
			}
        } catch (Exception e) {
			System.out.println(e.getMessage());
		}
        return 0;
    }
    
    public Theater readName(String theaterName){
         Theater theater = null;
            try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_THEATER_NAME)) {
            ps.setString(1, theaterName);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}
            theater = getRs(rs);
        } catch (Exception e) {
			System.out.println(e.getMessage());
		}
        return theater;
    }
    
    public Theater read(long theaterId){
         Theater theater = null;
            try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_THEATER)) {
            ps.setLong(1, theaterId);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}
            theater = getRs(rs);
        } catch (Exception e) {
			System.out.println(e.getMessage());
		}
        return theater;
    }
    
    public List<Theater> readAll(){
         List<Theater> theaters = new ArrayList<Theater>();
         try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_THEATER_ALL)) {
			ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                theaters.add(getRs(resultSet));
            }
        }catch (Exception e) {
			System.out.println(e.getMessage());
		}
        return theaters;
    }
    
    public void update(Theater theater, long theaterId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.UPADE_THEATER)) {
           
            ps.setString(1, theater.getTheaterName());
            ps.setLong(2, theater.getAddress().getAddressId());
            ps.setArray(3,listToArray(listScreenToInt(theater.getScreen())));
            ps.setArray(4, listToArray(listMovieToInt(theater.getMovies())));
            ps.setFloat(5, theater.getRating());
            ps.setFloat(6, theaterId);
            System.out.println(ps.executeUpdate());
			if (ps.executeUpdate() == 1) {
                TheaterPort.update(theaterId); // Calling Another Port 
				System.out.println("Theater Details Successfully Updated");

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    
    public void updateSeats(Theater theater){
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = DatabaseConnection.getConnect().prepareStatement(QueryStatement.UPADE_THEATER_SCREEN)) {            
            ps.setArray(1, listToArray(listScreenToInt(theater.getScreen())));
			ps.setLong(2, theater.getTheaterId());
            
			if (ps.executeUpdate() > 0) {
				System.out.println("Theater Seats Updated");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public void delete(long theaterId) {
        
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = DatabaseConnection.getConnect().prepareStatement(QueryStatement.DELETE_THEATER)) {
            Theater theater = new TheaterService().read(theaterId);
            
            List<Screen> screens = theater.getScreen();
            for(Screen screen: screens){
                new ScreenService().delete(screen.getScreenId());
            }
            new MovieService().deleteUnused();
			ps.setLong(1, theaterId);
			if (ps.executeUpdate() != 1) {
				System.out.println("Theater not deleted");
			}
            new AddressService().delete(theater.getAddress().getAddressId());
            TheaterPort.delete(theaterId); // Calling Another Port
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public Integer[] arrayToIntArr(Array array) throws SQLException {
        return (Integer[])array.getArray();
    }
        
    public List<Movie> intArrToListMovies(Integer[] intArrMovies) throws SQLException{
        List<Movie> list = new ArrayList<Movie>();
        for(int i = 0; i < intArrMovies.length; i++){
                list.add(new MovieService().read(intArrMovies[i]));
        }
        return list;
    }
    
    public List<Screen> intArrToListScreens(Integer[] intArrScreen) throws SQLException{
        List<Screen> list = new ArrayList<Screen>();
        for(int i = 0; i < intArrScreen.length; i++){
                list.add(new ScreenService().read(intArrScreen[i]));
        }
        return list;
    }
    
    public List<Integer> listMovieToInt(List<Movie> movies){
        List<Integer> moviesId = new ArrayList<Integer>();
        for(Movie movie : movies){
            moviesId.add((int)(new MovieService().checkMovieOrCreate(movie)));
        }
        return moviesId;
    }
    
    public List<Integer> listScreenToInt(List<Screen> screens){
        List<Integer> screensId = new ArrayList<Integer>();
        for(Screen screen : screens){
            screensId.add((int)(screen.getScreenId()));
        }
        return screensId;
    }
    public List<Integer> listAddScreenToInt(List<Screen> screens){
        List<Integer> screensId = new ArrayList<Integer>();
        for(Screen screen : screens){
            screensId.add((int)(new ScreenService().add(screen)));
        }
        return screensId;
    }
    public Array listToArray(List<Integer> listInt) throws SQLException {
        Integer[] intArr = listInt.toArray(new Integer[listInt.size()]);
        Array sqlArray = DatabaseConnection.getConnect().createArrayOf("integer", intArr);
        return sqlArray;
    }
    
    public void setPs(PreparedStatement ps,Theater theater) throws SQLException{
            ps.setString(1, theater.getTheaterName());
            ps.setLong(2, theater.getAddress().getAddressId() > 0 ? theater.getAddress().getAddressId() : new AddressService().add(theater.getAddress()));
            ps.setArray(3, theater.getScreen().get(0).getScreenId() > 0 ? listToArray(listScreenToInt(theater.getScreen())) : listToArray(listAddScreenToInt(theater.getScreen())));
            ps.setArray(4, listToArray(listMovieToInt(theater.getMovies())));
            ps.setFloat(5, theater.getRating());
    }
    
    public Theater getRs(ResultSet resultSet) throws SQLException {

		return new Theater(resultSet.getLong(Constant.THEATER_ID),resultSet.getString(Constant.THEATER_NAME),
                new AddressService().read(resultSet.getLong(Constant.THAT_ADDRESS_ID)),
                intArrToListScreens(arrayToIntArr(resultSet.getArray(Constant.THEATER_SCREENS))),intArrToListMovies(arrayToIntArr(resultSet.getArray(Constant.THEATER_MOVIES))),
                resultSet.getFloat(Constant.THEATER_RATING));
    }
    
    public static void main(String[] args){
        
        /*
        List<Integer> screens = new ArrayList<Integer>(){
            {
            add(50);
            add(70);
            }
        };
        List<Movie> movies = new ArrayList<Movie>(){
            {
            add(new Movie("KGF", MovieType.valueOf("Tamil"),MovieStatus.valueOf("Available")));
            add(new Movie("Don", MovieType.valueOf("Tamil"),MovieStatus.valueOf("Available")));
            }
        };
        Address address = new Address("Colony", "Tirupur", "Tamil Nadu", "641602", "Near Colony");
        new TheaterService().update(new Theater("Sri Sakthi", address, screens, movies, 9.8f),1);
        */
        System.out.println(new TheaterService().readAll());
        
        
    }
}