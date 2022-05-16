<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*,
java.time.LocalDateTime"%>


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
    
       <% Movie movie  = (Movie)JsonUtil.stringToObject(session.getAttribute("movie").toString(), Movie.class);
       LocalDateTime localDateTime = new Booking().showDateTime(session.getAttribute("date").toString(),session.getAttribute("time").toString());
       Theater theater = (Theater)JsonUtil.stringToObject(session.getAttribute("theater").toString(), Theater.class);
       Screen screen =(Screen)JsonUtil.stringToObject(session.getAttribute("screen").toString(), Screen.class);%>
       
       
        <h2 style="text-align:center;">Choice your seats to book ticket</h2>
        <h2 style="margin-left: 400px;"><%out.print(movie.getMovieName());%>(<%out.print(movie.getMovieType());%>)
       <%out.print(localDateTime);%></h2>
       <form action="booking" method="POST">
       <%if(session.getAttribute("user") != null){
           if(request.getParameter("seat") == null){%>
           <p style="margin-left: 400px;"><%out.print(theater.getTheaterName());%>  SCREEN-(<%out.print(screen.getScreenId());%>) <input style="font-size: .8rem" type ="text" name="noOfSeatsBooked" placeholder="Eg:A1-A10 or A1,A2,A3"/> <input type="submit" value="Book"></p></form>
       <%}%>
       
       <%//;
       if(request.getParameter("seat") != null && request.getParameter("reset") == null){
           
           
           String getSeat = request.getParameter("seat").toString();
           if(session.getAttribute("seat") != null && session.getAttribute("seat").toString().contains(getSeat)){%>
               <p style="text-align:center;text-color: red;">Please Select Only unselected seats!</p>
           <%}else if(session.getAttribute("seat") != null && getSeat.length() == 2){
               session.setAttribute("seat", session.getAttribute("seat")+","+getSeat);
           }else{
               session.setAttribute("seat", getSeat);
           }
           String conSeat = session.getAttribute("seat").toString();
           %>
           
            <p style="margin-left: 400px;"><%out.print(theater.getTheaterName());%>  SCREEN-(<%out.print(screen.getScreenId());%>) 
            <input style="font-size: .8rem" type ="text" name="noOfSeatsBooked" placeholder="Eg:A1-A10 or A1,A2,A3" value="<%= conSeat%>"/> <input type="submit" value="Book"></p></form>
            <form  action="seat.jsp"><input style="margin-left: 140vmin;" type="Submit" name="reset" value="Reset"></form>           
       <%}else{
           session.removeAttribute("seat");
       }%>
       <%}%>
       
       <%  String[][] seats = new Booking().timeBook(localDateTime, theater.getTheaterId(), screen);
           int bookedDetail[] = new Booking().totalSeatsBooked(seats); %>
       
        
       <form action="seat.jsp">
        <pre><p style="margin-left:500px;"><%= bookedDetail[0]%>: Sold             <%= bookedDetail[1]%>: Available<td style="background-color: green;"></p></pre>
        <table class="seats-table" BORDER="1">
            <%for(int i = 0 ; i < seats.length;i++){%>
                <tr>
                <%for(int j = 0 ; j < 10 ;j++){
                    if(seats[i][j].equals("-")){%>
                    <td >
                        <p style="text-align:centre;" ><%= seats[i][j]%> </p>
                    </input></td>
                    <%}else{%>
                        <td style="background-color: green;">
                        <input style="width:50px; font-size: .8rem" type="submit" name="seat" value="<%= seats[i][j]%>" required>
                            </input></td>
                    <%}
                    }%>
                </tr>
            <%}%>
        </table>
        
        <form action="booking" method="POST">
        
	</body>
</html>

            