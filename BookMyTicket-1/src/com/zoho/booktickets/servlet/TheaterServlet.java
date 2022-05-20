package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.Address;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.MovieStatus;
import com.zoho.booktickets.model.MovieType;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.service.TheaterService;

public class TheaterServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		List<Screen> screens = new ArrayList<Screen>();
		List<Movie> movies = new ArrayList<Movie>();
		try {

			Address address = new Address(req.getParameter("street"), req.getParameter("city"),
					req.getParameter("state"),
					req.getParameter("pincode"), req.getParameter("landmark"));
			Movie movie = new Movie(req.getParameter("movieName"), MovieType.valueOf("Tamil"),
					MovieStatus.valueOf(req.getParameter("movieStatus")));
			String[] showTimes = req.getParameter("showTimes").split(",");
			Screen screen = new Screen(Integer.parseInt(req.getParameter("noOfSeats")), movie, showTimes);

			screens.add(screen);

			movies.add(movie);
			Theater theater = new Theater(req.getParameter("theaterName"), address, screens, movies,
					Float.parseFloat(req.getParameter("rating")));
			System.out.println(theater.toString());
			new TheaterService().add(theater);

			res.sendRedirect("board.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		long theaterId = Long.parseLong(req.getParameter("id"));
		try {
			if (theaterId == 0) {
				res.getWriter().append(JsonUtil.objectToString(new TheaterService().readAll()));
			} else {
				res.getWriter().append(JsonUtil.objectToString(new TheaterService().read(theaterId)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {

			new TheaterService().update((Theater) JsonUtil.stringToObject(new StringBuffer()
					.append(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))).toString()
					.toString(), Theater.class), Long.parseLong(req.getParameter("id")));
			res.getWriter().append("Theater Update Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {
			new TheaterService().delete(Long.parseLong(req.getParameter("theaterId")));
			res.getWriter().append("Theater Delete Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
