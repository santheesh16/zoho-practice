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
     
     <h2 style="text-align:center;margin-top:80px">Check movie seat availability</h2>
      <table class="movie-table" BORDER="1">
            <tr >   
                <th>Movie No    </th>
                <th>Movie Name</th>
                <th>Movie Type</th>
                
                <th>Book</th>
            </tr>
            <% List<Movie> movies = new MovieService().readAll();
                for(int i = 0; i < movies.size(); i++){ %>
            <tr>
                
                    <td><%= i+1 %></td>
                    <td><%= movies.get(i).getMovieName() %></td>
                    <td><%= movies.get(i).getMovieType() %></td>
                    
                    <td><a href="movies?movieId=<%= movies.get(i).getMovieId()%>">Here</a></td>
            </tr>
            <%}%>
        </table>
        
	</body>
</html>

            