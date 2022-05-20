<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*,
java.time.LocalDate,java.time.format.DateTimeFormatter,javax.servlet.http.HttpSession"%>


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
    
    <% Movie movie  = (Movie)JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);%>
    
    <%if(request.getParameter("date") != null ){
        session.setAttribute("date",request.getParameter("date"));
        
    }%>
    <%
    if(request.getParameter("theaterName") != null && request.getParameter("time") != null ){%>
            
            <%
            session.setAttribute("time",request.getParameter("time"));
            session.setAttribute("theaterName",request.getParameter("theaterName"));
            %>
            <div class="theaterDateTime">
            <form action="movies" method="POST">
                
            <h2>Your selected details</h2>
            <input style="width:150px;" type="button" disabled name="theaterName" value="<%= session.getAttribute("theaterName")%>"></input>
            <input type="button" disabled name="date" value="<%= session.getAttribute("date")%>"></input>
            <input type="button" disabled name="time" value="<%= session.getAttribute("time")%>"></input>
            <input type="submit" value="Submit">
                </form>
            </div>
    <%}%>
    <div class="view-theater">
    <h2 style="text-align:center;">Select your Date & Time of Movie</h2>
    <h2><%out.print(movie.getMovieName());%>(<%out.print(movie.getMovieType());%>)</h2>
     
    <form  action="theater.jsp">
    <%
    for(String date: new Booking().generateDates()){
        %>
        <input  style="margin-left: 10px;margin-top: 10px" type="submit" name="date" value=<%= date%>></input>
       <%}
    %>
    </form>
    <table BORDER="1">
            
             <%List<Theater> theaters= (List)session.getAttribute("theaters");
    
            for(int i = 0; i < theaters.size(); i++){
                Theater theater = theaters.get(i);
                String theaterName = theater.getTheaterName();%>
                 <form  action="theater.jsp">
                <h2><input style="width:50px;" name="theaterName" value="<%= theaterName%>" type="radio"><%= theaterName%></input></h2>
                    <%for(Screen screen : theater.getScreen()){
                        for(String string: screen.getShowTimes()){
                            if(!string.isEmpty()){%>
                            <input style="margin-left:5px;width:110px;" type="submit" name="time" value="<%= string.trim()%>"></input>
                            
                            <%}
                        }
                }%>
                
            <%}%>

                </form>
                
                            
    </table>
   
   
   
    
    </div>
    
	</body>
</html>

            