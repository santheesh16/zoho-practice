
<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*"%>


<%@ page language="java" 
    contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>


<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Movies</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
    <%
    if(session.getAttribute("admin") == null){%>
        <div class="topnav">
            <a href="/BookMyTicket-1/movies.jsp">Book Tickets</a>
            <a href="/BookMyTicket-1/cancel.jsp">Cancellation</a>
            <a href="/BookMyTicket-1/avail.jsp">Availability</a>
            <a href="/BookMyTicket-1/userbooked.jsp">BookedDetails</a>
            <a href="/BookMyTicket-1/logout.jsp">Logout</a>
        </div>
    <%}else{%>
        <div class="topnav">
            <a href="/BookMyTicket-1/board.jsp">Add</a>
            <a href="/BookMyTicket-1/remove.jsp">Edit/Delete</a>
            <a href="/BookMyTicket-1/avail.jsp">Availability</a>
            <a href="/BookMyTicket-1/booked.jsp">BookedDetails</a>
            <a href="/BookMyTicket-1/logout.jsp">Logout</a>
        </div>
    <%}%>
	</body>
</html>

            