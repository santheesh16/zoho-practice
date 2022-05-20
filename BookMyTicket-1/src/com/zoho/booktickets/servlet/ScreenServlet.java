package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.MovieStatus;
import com.zoho.booktickets.model.MovieType;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.port.ScreenPort;
import com.zoho.booktickets.service.ScreenService;
import com.zoho.booktickets.service.TheaterService;
import com.zoho.booktickets.service.UserService;


public class ScreenServlet extends HttpServlet{
    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
		try {
            Theater theater = new TheaterService().readName(session.getAttribute("theaterName").toString());
            
            
            String[] showTimes = req.getParameter("showTimes").split(",");
            Screen screen= null;
            Movie movie = null;
            movie = new Movie(req.getParameter("movieName"),MovieType.valueOf(req.getParameter("movieType")), MovieStatus.valueOf(req.getParameter("movieStatus")));
                
                screen = new Screen(Integer.parseInt(req.getParameter("noOfSeats")), movie,showTimes);
                long screenId = new ScreenService().add(screen);
                screen.setScreenId(screenId);
            
            List<Screen> screens =  theater.getScreen();
            List<Movie> movies =  theater.getMovies();
            screens.add(screen);
            movies.add(movie);
            theater.setScreen(screens);
            theater.setMovies(movies);
            new TheaterService().update(theater, theater.getTheaterId());
            ScreenPort.add(theater.getTheaterId());// Adding Screen & Update Theater
            res.sendRedirect("board.jsp"); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		long userId = Long.parseLong(req.getParameter("id"));
		try {
            if (userId == 0) {
				res.getWriter().append(JsonUtil.objectToString(new UserService().readAll()));
			} else {
				res.getWriter().append(JsonUtil.objectToString(new UserService().read(userId)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {

			new UserService().update((User) JsonUtil.stringToObject(new StringBuffer()
					.append(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))).toString()
					.toString(), User.class), Long.parseLong(req.getParameter("id")));
			res.getWriter().append("User Update Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {
			new UserService().delete(Long.parseLong(req.getParameter("id")));
			res.getWriter().append("User Delete Successfully");
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
