<%@ page import="java.util.*,com.zoho.booktickets.model.*"%>

<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.service.*,com.zoho.booktickets.jsonutil.*"%>
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
     
     <form action="booked.jsp">
     <input style="margin-left:450px;margin-top: 20px;" type="text" name="bookedId" placeholder="Enter booking id"></input> <input type="submit" value="Search"></input>
     </form>
     <%if(request.getParameter("bookedId") != null){%>
        <form >
        
        <h2 style="text-align:center;">Your Booked Details</h2>
        <%Booking booking = new BookingService().read(Long.parseLong(request.getParameter("bookedId")));
          
          Theater theater = new TheaterService().read(booking.getTheaterId());
          Movie movie = new MovieService().read(booking.getMovieId());
          %>
          
          <table class="booking-table" BORDER="1">
            <tr> <td>Booking Id:</td> <td><%= booking.getBookingId()%></td><tr>
            <tr> <td>User Id:</td> <td><%= booking.getUserId() %></td><tr>
            <tr> <td>Theater Name:</td> <td><%= theater.getTheaterName() %></td><tr>
            <tr> <td>Screen No:</td> <td><%= booking.getScreenId()%></td><tr>
            <tr> <td>Address: </td> <td><%= theater.getAddress()%></td><tr>
            <tr> <td>Movie Name:</td> <td><%= movie.getMovieName()%></td><tr>
            <tr> <td>Booked Seats:</td> <td><%= Arrays.toString(booking.getNoOfBookedSeats()).replace("[","").replace("]","")%></td><tr>
            <tr> <td>Movie Date & Time:</td> <td><%= booking.getShowDateTime()%></td><tr>
            <tr> <td>Booked Date & Time:</td> <td><%=booking.getBookedDateTime()%></td><tr>
            
          </table>

            
		</form>
    <%}else{%>
        <form >
        
        <h2 style="text-align:center;">All Booked Details</h2>
        <%
        List<Booking> bookedList = new BookingService().readAll();
          for(Booking booking: bookedList){
          Theater theater = new TheaterService().read(booking.getTheaterId());
          Movie movie = new MovieService().read(booking.getMovieId());
          %>
          
          <table class="booking-table" BORDER="1">
            <tr> <td>Booking Id:</td> <td><%= booking.getBookingId()%></td><tr>
            <tr> <td>User Id:</td> <td><%= booking.getUserId() %></td><tr>
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
    <%}%>
	</body>
</html>

            