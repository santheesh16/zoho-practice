package com.zoho.booktickets.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.constant.QueryStatement;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.MovieStatus;
import com.zoho.booktickets.model.MovieType;

public class MovieService{
    
    public void createEnumType(){
        try(Connection conn = DatabaseConnection.getConnect()) {
            PreparedStatement  pstmt = conn.prepareStatement(QueryStatement.ENUM_MOVIE_STATUS);
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(QueryStatement.ENUM_MOVIE_TYPE);
            pstmt.executeUpdate();
            System.out.println("MovieType and MovieStatus Successfully Created!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public long checkMovieOrCreate(Movie movie) {
		try (Connection conn = DatabaseConnection.getConnect(); PreparedStatement ps =
        conn.prepareStatement(QueryStatement.GET_MOVIE_ID)) {

			setPs(ps, movie);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				
				return resultSet.getLong(Constant.MOVIE_ID);
			}	
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        return add(movie);
	}
    
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect(); PreparedStatement pstmt =
        conn.prepareStatement(QueryStatement.CREATE_MOVIE_TABLE);) {
              pstmt.executeUpdate();
              System.out.println("Movie Successfully Created!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public long add(Movie movie){
         
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_MOVIE,
                Statement.RETURN_GENERATED_KEYS)) {
            setPs(ps, movie);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
				return resultSet.getLong(Constant.MOVIE_ID);
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    
    public Movie read(long movieId){
         Movie movie = null;
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.SELECT_MOVIE)) {
            
            pstmt.setLong(1, movieId);
			ResultSet rs = pstmt.executeQuery();
            
			if (!rs.next()) {
				return null;
			} 
            movie = getRs(rs);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return movie;
    }
    
    public List<Movie> readAll(){
         List<Movie> movies = new ArrayList<Movie>();
         try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_MOVIE_ALL)) {
			ResultSet rs = ps.executeQuery();
            while(rs.next()){
                movies.add(getRs(rs));
            }
            if(movies.isEmpty()){
                System.out.println("Movies Table is Empty");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return movies;
    }
    
    
    public void update(Movie movie, long movieId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.UPADE_MOVIE)) {

            setPs(ps, movie);
            ps.setLong(4, movieId);
			if (ps.executeUpdate() == 1) {
				System.out.println("Movie Details Successfully Updated");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public void delete(long movieId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.DELETE_MOVIE)) {
			ps.setLong(1, movieId);
            if (ps.executeUpdate() == 1) {
				System.out.println("Movie successfully deleted!!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public void deleteUnused() {
		try(Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.DELETE_UNUSED_MOVIE)){
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public void setPs(PreparedStatement ps,Movie movie){
        try{
            ps.setString(1, movie.getMovieName());
            ps.setObject(2, movie.getMovieType(), Types.OTHER);
            ps.setObject(3, movie.getMovieStatus(),  Types.OTHER);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public Movie getRs(ResultSet resultSet) throws SQLException {
		return new Movie(resultSet.getLong(Constant.MOVIE_ID), resultSet.getString(Constant.MOVIE_NAME),
				MovieType.valueOf(resultSet.getString(Constant.MOVIE_TYPE)),MovieStatus.valueOf(resultSet.getString(Constant.MOVIE_STATUS)));
	}
    
    public static void main(String[] args){
        new MovieService().deleteUnused();
        //Integer[] inte = new Integer[]{1};
        //System.out.println(new MovieService().read(inte[0]));
    }
}