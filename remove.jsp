<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*,
java.time.LocalDateTime,com.zoho.booktickets.service.*"%>

<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Board</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
    <%@ include file="/navbar.jsp" %>   
		<form name="mainform" action="login" >
        <div class="add-theater">
        
        <h2 style="text-align:center;">BookMyTicket </h2>
      
    <%
    List<Theater> theaters = new TheaterService().readAll();
    for(int j = 0;  j < theaters.size() ;j++ ){%>
        <h2><%= j+1%>.<%= theaters.get(j).getTheaterName()%> - <a href="addtheater.jsp?theaterId=<%= theaters.get(j).getTheaterId()%>"><input type="button" style="text-align:center;margin-left:10px;" value="Edit"></input></a> <a href="delete.jsp?theaterId=<%=theaters.get(j).getTheaterId()%>&delete=yes"><input type="button" style="text-align:center;margin-left:10px;" value="Delete"></input></a></h2>
        
        <%List<Screen> screens = theaters.get(j).getScreen();
        for(int i = 0;  i < screens.size() ;i++ ){%>
            <h3 style="margin-left:100px;">SCREEN(<%= i+1%>) - <%= screens.get(i).getMovie().getMovieName()%><a href="addscreen.jsp?theaterId=<%= theaters.get(j).getTheaterId()%>&screenNo=<%= i+1 %>"><input type="button" style="text-align:center;margin-left:10px;" value="Edit"></input></a> <a href="delete.jsp?theaterName=<%=theaters.get(j).getTheaterName()%>&screenNo=<%= i + 1%>"><input type="button" style="text-align:center;margin-left:10px;" value="Delete"></input></a></h3> 
        
        <%}%>
        
    <%}%>
      
    </div>
    
		</form>
	</body>
</html>