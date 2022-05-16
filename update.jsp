
<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.service.*,com.zoho.booktickets.service.*,
com.zoho.booktickets.jsonutil.*"%>
<% 
    	//Update Theater Address
        if(request.getParameter("theaterName") != null){
            Theater theater = new TheaterService().readName(request.getParameter("theaterName"));
            theater.setRating(Float.parseFloat(request.getParameter("rating")));
        Address address = new Address(request.getParameter("street"), request.getParameter("city"),
        request.getParameter("state"), request.getParameter("pincode"), request.getParameter("landmark"));
        address.setAddressId(theater.getAddress().getAddressId());
        new AddressService().update(address , theater.getAddress().getAddressId());
        new TheaterService().update(theater, theater.getTheaterId());
        response.sendRedirect("remove.jsp");
        }else{
            //Update Screen and Movie Details
            Theater theater = new TheaterService().read(Long.parseLong(session.getAttribute("theaterId").toString()));
            Screen screen = theater.getScreen().get(Integer.parseInt(session.getAttribute("screenNo").toString()));
            String[] showTimes = request.getParameter("showTimes").split(",");
            Movie movie = new Movie(request.getParameter("movieName"), MovieType.valueOf(request.getParameter("movieType")), MovieStatus.valueOf(request.getParameter("movieStatus")));
            long movieId = new MovieService().checkMovieOrCreate(movie);
            movie.setMovieId(movieId);
            List<Movie> movies = theater.getMovies();
            movies.set(Integer.parseInt(session.getAttribute("screenNo").toString()), movie);
            new TheaterService().update(theater, theater.getTheaterId());
            Screen updatedScreen = new Screen(Integer.parseInt(request.getParameter("noOfSeats")), movie, showTimes);
            updatedScreen.setScreenId(screen.getScreenId());
            new ScreenService().update(updatedScreen, screen.getScreenId());
            response.sendRedirect("remove.jsp");
        }
        %>