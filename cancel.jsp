<%@ page import="java.util.*,java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.service.*,com.zoho.booktickets.jsonutil.*"%>
<%@ page language="java" 
    contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>


<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>BookedDeatils</title>
        <style><%@include file="/css/style.css"%></style>
	</head>
   
    
	<body>
     <%@ include file="/navbar.jsp" %>
     
     <form action="delete.jsp">
        <input style="margin-left:450px;margin-top: 20px;" type="text" name="bookedId" placeholder="Enter booking id"></input><input type="submit" value="Cancel"></input>
     </form>
     <%if(session.getAttribute("booking") != null){
         Booking booking = (Booking)JsonUtil.stringToObject(session.getAttribute("booking").toString(), Booking.class);
         %>
        <form >
        
        <h2 style="text-align:center;">Booking Id :<%= booking.getBookingId()%> Successfully Cancelled</h2>
        <h2 style="text-align:center;">Check your Mail & SMS</h2>
        <%
          Theater theater = new TheaterService().read(booking.getTheaterId());
          Movie movie = new MovieService().read(booking.getMovieId());
          session.removeAttribute("booking");
          %>
          
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
    <%}else if(session.getAttribute("user") != null){%>
        <form >
        
        <h2 style="text-align:center;">Your All Booked Details</h2>
        <%User user = (User)JsonUtil.stringToObject(session.getAttribute("user").toString(), User.class);;
        List<Booking> bookedList = new BookingService().searchUserBooked(user.getUserId());
          for(Booking booking: bookedList){
          Theater theater = new TheaterService().read(booking.getTheaterId());
          Movie movie = new MovieService().read(booking.getMovieId());
          %>
          
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

          <%}%>
		</form>
    <%}else{
        response.sendRedirect("index.jsp");
    }%>     
	</body>
</html>

            