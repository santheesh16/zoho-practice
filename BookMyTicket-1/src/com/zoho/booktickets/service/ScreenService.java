package  com.zoho.booktickets.service;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zoho.booktickets.connection.DatabaseConnection;
import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.constant.QueryStatement;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.port.ScreenPort;

public class ScreenService{
    
    public void create(){
        try(Connection conn = DatabaseConnection.getConnect();
                PreparedStatement pstmt = conn.prepareStatement(QueryStatement.CREATE_SCREEN_TABLE)) {              
            pstmt.executeUpdate();
            System.out.println("Screen Successfully Created!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public long add(Screen screen){
         try (Connection conn = DatabaseConnection.getConnect();
                PreparedStatement ps = conn.prepareStatement(QueryStatement.INSERT_SCREEN,
                Statement.RETURN_GENERATED_KEYS)) {
            setPs(ps, screen);
			ResultSet resultSet;
			if (ps.executeUpdate() == 1 && (resultSet = ps.getGeneratedKeys()).next()) {
				return resultSet.getLong(Constant.SCREEN_ID);
			}
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
    
    public Screen read(long screenId){
         Screen screen = null;
            try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.SELECT_SCREEN)) {
            ps.setLong(1, screenId);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}
            screen = getRs(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return screen;
    }
    
    public void update(Screen screen, long screenId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = conn.prepareStatement(QueryStatement.UPADE_SCREEN)) {

            
            setPs(ps,screen);
            ps.setFloat(4, screenId);
            
			if (ps.executeUpdate() == 1) {
                ScreenPort.update(screenId);
				System.out.println("Screen Details Successfully Updated");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}   
    
    public void delete(long screenId) {
		try (Connection conn = DatabaseConnection.getConnect();PreparedStatement ps = DatabaseConnection.getConnect().prepareStatement(QueryStatement.DELETE_SCREEN)) {
			ps.setLong(1, screenId);
			if (ps.executeUpdate() != 1) {

				System.out.println("Screen not deleted");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    
    public Array listToArray(int[] listInt) throws SQLException {
        Integer[] intArr = new Integer[listInt.length];
        for(int i = 0; i < listInt.length; i++){
            intArr[i] = listInt[i];
        }
        Array sqlArray = DatabaseConnection.getConnect().createArrayOf("integer", intArr);
        return sqlArray;
    }
    
    public void setPs(PreparedStatement ps,Screen screen) throws SQLException{
            ps.setInt(1,screen.getSeats());
            ps.setLong(2, new MovieService().checkMovieOrCreate(screen.getMovie()));
            ps.setArray(3, DatabaseConnection.getConnect().createArrayOf("text", screen.getShowTimes()));
            
    }
    //2D String -> Array 
    public Array stringToArray(String seats[][])  throws SQLException{
        Array sqlArray = DatabaseConnection.getConnect().createArrayOf("text", seats);        
        return sqlArray;
    }
    
    public String[] arrayToString(Array array)  throws SQLException{
        return (String[])array.getArray();
    }
    
    public int[] arrayToInt(Array array)  throws SQLException{
         Integer[] intArr = (Integer[])array.getArray();
         int[] listInt = new int[intArr.length];
         for(int i = 0; i < listInt.length; i++){
            listInt[i] = intArr[i];
        }
        return listInt;
    }
    
    public Screen getRs(ResultSet resultSet) throws SQLException {
		return new Screen(resultSet.getLong(Constant.SCREEN_ID), resultSet.getInt(Constant.SCREEN_SEATS),
                new MovieService().read(resultSet.getLong(Constant.SCREEN_MOVIE_ID)),arrayToString(resultSet.getArray(Constant.SHOW_TIMES)));
    }
    
        
}