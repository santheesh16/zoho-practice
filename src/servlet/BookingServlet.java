package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.Booking;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Screen;
import com.zoho.booktickets.model.Theater;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.service.BookingService;
import com.zoho.booktickets.service.EmailService;
import com.zoho.booktickets.service.SmsService;
import com.zoho.booktickets.service.UserService;

public class BookingServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		try {

			HttpSession session = req.getSession();
			User user = (User) JsonUtil.stringToObject(session.getAttribute("user").toString(), User.class);
			Theater theater = (Theater) JsonUtil.stringToObject(session.getAttribute("theater").toString(),
					Theater.class);
			Movie movie = (Movie) JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);
			Screen screen = (Screen) JsonUtil.stringToObject(session.getAttribute("screen").toString(), Screen.class);
			String bookedSeats = req.getParameter("noOfSeatsBooked").toString();
			System.out.println(bookedSeats);
			String[] noOfSeatsBooked = null;
			if (bookedSeats.contains(",")) {
				noOfSeatsBooked = bookedSeats.split(",");
			} else if (bookedSeats.contains("-")) {
				noOfSeatsBooked = new Booking().noOfSeatsBooking(new Booking().generateSeats(screen.getSeats()),
						bookedSeats);
			} else {
				noOfSeatsBooked = new String[] { bookedSeats };
			}
			Booking booking = new Booking(user.getUserId(), theater.getTheaterId(), movie.getMovieId(),
					screen.getScreenId(), noOfSeatsBooked, new Booking().showDateTime(
							session.getAttribute("date").toString(), session.getAttribute("time").toString()));
			long bookingId = new BookingService().add(booking);
			booking.setBookingId(bookingId);
			session.setAttribute("booking", JsonUtil.objectToString(booking));
			//new EmailService().generatedPDF(bookingId);
			//new EmailService().sendPDFMail(new UserService().read(user.getUserId()).getEmailId());
			//new SmsService().bookTickets(bookingId, new UserService().read(user.getUserId()).getMobileNo());
			res.sendRedirect("booking.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		long bookingId = Long.parseLong(req.getParameter("id"));
		try {
			if (bookingId == 0) {
				res.getWriter().append(JsonUtil.objectToString(new BookingService().readAll()));
			} else {
				res.getWriter().append(JsonUtil.objectToString(new BookingService().read(bookingId)));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {

			new BookingService().update((Booking) JsonUtil.stringToObject(new StringBuffer()
					.append(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))).toString()
					.toString(), Booking.class), Long.parseLong(req.getParameter("id")));
			res.getWriter().append("Booking Update Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {

		try {
			new BookingService().delete(Long.parseLong(req.getParameter("bookedId")));
			res.sendRedirect("cancel.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
