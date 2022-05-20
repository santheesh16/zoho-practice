package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.Booking;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.service.MovieService;
import com.zoho.booktickets.service.TheaterService;

public class MovieServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            Theater theater = new TheaterService().readName(session.getAttribute("theaterName").toString());
            session.setAttribute("theater", JsonUtil.objectToString(theater));
            Movie movie = (Movie) JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);
            List<Screen> screens = theater.getScreen();
            Screen screen = null;
            for (int i = 0; i < screens.size(); i++) {
                if (movie.getMovieId() == screens.get(i).getMovie().getMovieId()) {
                    screens.get(i).setScreenId(i + 1);
                    screen = screens.get(i);
                    System.out.println("Screen" + screens.get(i));
                    break;
                }
            }
            session.setAttribute("screen", JsonUtil.objectToString(screen));
            res.sendRedirect("seat.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        long movieId = Long.parseLong(request.getParameter("movieId"));
        try {
            HttpSession session = request.getSession();
            Movie movie = new MovieService().read(movieId);
            List<Theater> theaters = new ArrayList<Theater>();
            for (Theater theater : new TheaterService().readAll()) {
                List<Screen> screens = new ArrayList<Screen>();
                for (Screen screen : theater.getScreen()) {
                    if (screen.getMovie().getMovieId() == movieId) {
                        screens.add(screen);
                    }
                    ;
                }
                theater.setScreen(screens);
                if (theater.getScreen().size() != 0) {
                    theaters.add(theater);
                }

            }

            session.setAttribute("movie", JsonUtil.objectToString(movie));
            session.setAttribute("theaters", theaters);
            response.sendRedirect("theater.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            HttpSession session = req.getSession();

            Theater theater = (Theater) JsonUtil.stringToObject(session.getAttribute("theaterName").toString(),
                    Theater.class);
            Movie movie = (Movie) JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);
            Screen screen = (Screen) JsonUtil.stringToObject(session.getAttribute("movie").toString(), Screen.class);
            String[] noOfSeatsBooked = (String[]) JsonUtil
                    .stringToObject(req.getParameter("noOfSeatsBooked").toString(), String[].class);

            Booking booking = new Booking(32, theater.getTheaterId(), movie.getMovieId(), screen.getScreenId(),
                    noOfSeatsBooked, new Booking().showDateTime(session.getAttribute("date").toString(),
                            session.getAttribute("time").toString()));
            res.getWriter().append(booking.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

        try {
            new MovieService().delete(Long.parseLong(req.getParameter("id")));
            res.getWriter().append("Movie Delete Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
