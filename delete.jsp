
<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.service.*,com.zoho.booktickets.service.*,
com.zoho.booktickets.jsonutil.*"%>
<% 
        //Delete Theater Screen
        if(session.getAttribute("admin") != null && request.getParameter("screenNo") != null){
            int screenNo = Integer.parseInt(request.getParameter("screenNo")) - 1;
            Theater theater = new TheaterService().readName(request.getParameter("theaterName"));
            Screen deleteScreen = theater.getScreen().get(screenNo);
            new ScreenService().delete(deleteScreen.getScreenId());
            List<Screen> screens = theater.getScreen();
            List<Movie> movies = theater.getMovies();
            screens.remove(screenNo);
            movies.remove(screenNo);
            theater.setMovies(movies);
            theater.setScreen(screens);
            new TheaterService().update(theater, theater.getTheaterId());

            response.sendRedirect("remove.jsp");
        }else if(request.getParameter("delete") != null){
            //Delete Theater
            new TheaterService().delete(Long.parseLong(request.getParameter("theaterId")));
            response.sendRedirect("board.jsp");
        }else{
            //Delete Booked Tickets
            User user = (User)JsonUtil.stringToObject(session.getAttribute("user").toString(), User.class);
            long bookedId = Long.parseLong(request.getParameter("bookedId"));   
            session = request.getSession();
            new EmailService().generatedCancelPDF(bookedId);
            new EmailService().sendPDFMail(user.getEmailId());
            new SmsService().cancelTickets(bookedId, user.getMobileNo());
            Booking booking = new BookingService().read(bookedId);
            session.setAttribute("booking", JsonUtil.objectToString(booking));
            new BookingService().delete(bookedId);
            response.sendRedirect("cancel.jsp");
        }
        %>