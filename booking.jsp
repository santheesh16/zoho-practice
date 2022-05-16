

<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.service.*,com.zoho.booktickets.jsonutil.*"%>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Login Page</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
    <%@ include file="/navbar.jsp" %>
    <%session = request.getSession();
    if(session.getAttribute("user") == null){
        response.sendRedirect("/BookMyTicket/index.jsp");
    }%>
		<form >
        
        <h2 style="text-align:center;">Your ticket Succussfully Booked</h2>
        <h2 style="text-align:center;">Check your Mail & SMS</h2>
        <%Booking booking = (Booking)JsonUtil.stringToObject(session.getAttribute("booking").toString(), Booking.class);
          Theater theater = (Theater)JsonUtil.stringToObject(session.getAttribute("theater").toString(), Theater.class);
          Movie movie = (Movie)JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);
          session.removeAttribute("booking");%>
          
          <table class="booking-table" BORDER="1">
            <tr> <td>Booking Id:</td> <td><%= booking.getBookingId()%></td><tr>
            <tr> <td>Theater Name:</td> <td><%= theater.getTheaterName() %></td><tr>
            <tr> <td>Screen No:</td> <td><%= booking.getScreenId()%></td><tr>
            <tr> <td>Address: </td> <td><%= theater.getAddress()%></td><tr>
            <tr> <td>Movie Name:</td> <td><%= movie.getMovieName()%></td><tr>
            <tr> <td>Booked Seats:</td> <td><%= Arrays.toString(booking.getNoOfBookedSeats()).replace("[","").replace("]","")%></td><tr>
            <tr> <td>Movie Date & Time:</td> <td><%= booking.getShowDateTime()%></td><tr>
            <tr> <td>Booked Date & Time:</td> <td><%=booking.getBookedDateTime()%></td><tr>
            
          </table>

            
		</form>
        
        
	</body>
</html>