

<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*,
java.time.LocalDateTime,com.zoho.booktickets.service.*"%>

<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Login Page</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
    <%@ include file="/navbar.jsp" %>   
    <%if(request.getParameter("Yes") != null){
        long screenNo = Long.parseLong(request.getParameter("screenNo"));
       String theaterName = request.getParameter("theaterName").toString();
        Theater theater = new TheaterService().readName(theaterName);%>
        <table class="movie-table" BORDER="1">
            <tr >
                <th>Screen No</th>
                <th>Movie Id</th>
                <th>Movie Name</th>
                <th>Movie Type</th>
                
            </tr>
            <% List<Movie> movies = (List)theater.getMovies();
                for(int i = 0; i < movies.size(); i++){ %>
            <tr>
                    <td><%= i+1 %></td>
                    <td><%= movies.get(i).getMovieId() %></td>
                    <td><%= movies.get(i).getMovieName() %></td>
                    <td><%= movies.get(i).getMovieType() %></td>
                    
                    
            </tr>
            <%}%>
        </table>
    <%}else if(request.getParameter("theaterId") != null && request.getParameter("screenNo") != null){
        Theater theater = new TheaterService().read(Long.parseLong(request.getParameter("theaterId")));
        Screen screen = theater.getScreen().get(Integer.parseInt(request.getParameter("screenNo")) - 1);
        session.setAttribute("theaterId", request.getParameter("theaterId"));
        session.setAttribute("screenNo", Integer.parseInt(request.getParameter("screenNo")) - 1);%>
        <div class="add-theater">
		<form action="update.jsp" method="POST">
        
        
        <h2 style="text-align:center;">Edit Screen Details </h2>
        <label>Theater Name:<label>    <input style="width: 220px;"  type="text" name="theaterName" placeholder="Theater Name:" value="<%= theater.getTheaterName()%>" disabled/>    
        
        <h3>Screen(<%= request.getParameter("screenNo")%>):</h3>
        <input style="width: 220px;margin:0px;" name="noOfSeats" type="text" placeholder="No. of Seats:" value="<%= screen.getSeats()%>"/> <input type="text" name="showTimes" placeholder="Show Times:" value="<%= Arrays.toString(screen.getShowTimes()).replace("[", "").replace("]", "")%>"/>
        
        
        <h3>Movie:</h3>
        <input style="width: 220px;" name="movieName" type="text" placeholder="Name:" value="<%= screen.getMovie().getMovieName()%>" /> 
        <select style="margin:0px 0px 10px 0px;width: 130px;" name="movieType" id="movieType">
            <option value="<%= screen.getMovie().getMovieType()%>" selected="selected"><%= screen.getMovie().getMovieType()%></option>
            <option value="Tamil">Tamil</option>
            <option value="English">English</option>
        </select>
        <select style="margin:0px 0px 10px 0px;width: 150px;" name="movieStatus" id="movieStatus">
            <option value="<%= screen.getMovie().getMovieStatus()%>" selected="selected"><%= screen.getMovie().getMovieStatus()%></option>
            <option value="Available">Available</option>
            <option value="NotAvailable">NotAvailable</option>
        </select>
        
        <a href="addscreen.jsp?theaterName=<%= theater.getTheaterName()%>&screenNo=<%= request.getParameter("screenNo")%>&Yes=yes"><input type="button" value="Check Movies Id(!)"></input></a><input type="submit" style="text-align:center;margin-left:55vmin;" value="Update"></input>
		</form>
        </div>
        
    <%}else{
        session.setAttribute("theaterName", request.getParameter("theaterName"));%>
    <div class="add-theater">
		<form action="screen" method="POST">
        
        
        <h2 style="text-align:center;">Enter Screen Details </h2>
        <label>Theater Name:<label>    <input style="width: 220px;"  type="text" name="theaterName" placeholder="Theater Name:" value="<%= session.getAttribute("theaterName")%>" disabled/>    
        
        <h3>Screen(<%= Integer.parseInt(request.getParameter("screenNo")) + 1%>):</h3>
        <input style="width: 220px;margin:0px;" name="noOfSeats" type="text" placeholder="No. of Seats:"/> <input type="text" name="showTimes" placeholder="Show Times:"/>
        
        <h3>Movie:</h3>
        <input style="width: 220px;" name="movieName" type="text" placeholder="Name:"/> 
        <select style="margin:0px 0px 10px 0px;width: 130px;" name="movieType" id="movieType">
            <option >Tamil</option>
            <option >English</option>
        </select>
        <select style="margin:0px 0px 10px 0px;width: 150px;" name="movieStatus" id="movieStatus">
            <option >Available</option>
            <option >NotAvailable</option>
        </select>
        <a href="addscreen.jsp?theaterName=<%= session.getAttribute("theaterName")%>&screenNo=<%= request.getParameter("screenNo")%>&Yes=yes"><input type="button" value="Check Movies Id(!)"></input></a>
        <input type="submit" style="text-align:center;margin-left:55vmin;" value="Add Screen"></input>
		</form>
        </div>
    <%}%>
	</body>
</html>