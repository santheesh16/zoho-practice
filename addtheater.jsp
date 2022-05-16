

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
    <%if(request.getParameter("theaterId") != null){
        Theater theater = new TheaterService().read(Long.parseLong(request.getParameter("theaterId")));%>
        <div class="add-theater">
		<form name="mainform" action="update.jsp" method="POST">
        
        
        <h2 style="text-align:center;">Edit Theater Details </h2>
        <input style="width: 220px;" type="text" name="theaterName" placeholder="Theater Name:" value="<%= theater.getTheaterName()%>"/> <input type="text" name="rating" placeholder="Rating:Eg:9.8" value="<%= theater.getRating()%>"/>
        <h3>Address:</h3>
        <input style="width: 220px;" type="text" name="street" placeholder="Street:" value="<%= theater.getAddress().getStreet()%>"/>        <input type="text" name="city" placeholder="City:" value="<%= theater.getAddress().getCity()%>"/>
        <select style="margin: 10px 0px;width: 230px;" name="state" id="state">
            <option >Choose State..</option>
            <option value="<%= theater.getAddress().getCity()%>" selected="selected"><%= theater.getAddress().getState()%></option>
            
            <option value="Andhra Pradesh">Andhra Pradesh</option>
            <option value="Arunachal Pradesh">Arunachal Pradesh</option>
            <option value="Assam">Assam</option>
            <option value="Bihar">Bihar</option>
            <option value="Chandigarh">Chandigarh</option>
            <option value="Chhattisgarh">Chhattisgarh</option>
            
            <option value="Daman and Diu">Daman and Diu</option>
            <option value="Delhi">Delhi</option>
            <option value="Lakshadweep">Lakshadweep</option>
            <option value="Puducherry">Puducherry</option>
            <option value="Goa">Goa</option>
            <option value="Gujarat">Gujarat</option>
            <option value="Haryana">Haryana</option>
            <option value="Himachal Pradesh">Himachal Pradesh</option>
            <option value="Jammu and Kashmir">Kashmir</option>
            <option value="Jharkhand">Jharkhand</option>
            <option value="Karnataka">Karnataka</option>
            <option value="Kerala">Kerala</option>
            <option value="Madhya Pradesh">Madhya Pradesh</option>
            <option value="Maharashtra">Maharashtra</option>
            <option value="Manipur">Manipur</option>
            <option value="Meghalaya">Meghalaya</option>
            <option value="Mizoram">Mizoram</option>
            <option value="Nagaland">Nagaland</option>
            <option value="Odisha">Odisha</option>
            <option value="Punjab">Punjab</option>
            <option value="Rajasthan">Rajasthan</option>
            <option value="Sikkim">Sikkim</option>
            
            
            <option value="Tamil Nadu" selected="selected">Tamil Nadu</option>
            <option value="Telangana">Telangana</option>
            <option value="Tripura">Tripura</option>
            <option value="Uttar Pradesh">Uttar Pradesh</option>
            <option value="Uttarakhand">Uttarakhand</option>
            <option value="West Bengal">West Bengal</option>
        </select>
        <input style="width: 270px;" type="text" name="pincode" value="<%= theater.getAddress().getPincode()%>" placeholder="Pincode:"/>        <input type="text" name="landmark" value="<%= theater.getAddress().getLandmark()%>" placeholder="Landmark:"/>
        
        <input type="submit" style="text-align:center;margin-left:50vmin;" value="Update"></input>
		</form>
        </div>
    <%}else{%>
        <div class="add-theater">
		<form name="mainform" action="theater" method="POST">
        
        
        <h2 style="text-align:center;">Enter Theater Details </h2>
        <input style="width: 220px;" type="text" name="theaterName" placeholder="Theater Name:"/> <input type="text" name="rating" placeholder="Rating:Eg:9.8"/>
        <h3>Address:</h3>
        <input style="width: 220px;" type="text" name="street" placeholder="Street:"/>        <input type="text" name="city" placeholder="City:"/>
        <select style="margin: 10px 0px;width: 230px;" name="state" id="state">
            <option >Choose State..</option>
            
            
            <option value="Andhra Pradesh">Andhra Pradesh</option>
            <option value="Arunachal Pradesh">Arunachal Pradesh</option>
            <option value="Assam">Assam</option>
            <option value="Bihar">Bihar</option>
            <option value="Chandigarh">Chandigarh</option>
            <option value="Chhattisgarh">Chhattisgarh</option>
            
            <option value="Daman and Diu">Daman and Diu</option>
            <option value="Delhi">Delhi</option>
            <option value="Lakshadweep">Lakshadweep</option>
            <option value="Puducherry">Puducherry</option>
            <option value="Goa">Goa</option>
            <option value="Gujarat">Gujarat</option>
            <option value="Haryana">Haryana</option>
            <option value="Himachal Pradesh">Himachal Pradesh</option>
            <option value="Jammu and Kashmir">Kashmir</option>
            <option value="Jharkhand">Jharkhand</option>
            <option value="Karnataka">Karnataka</option>
            <option value="Kerala">Kerala</option>
            <option value="Madhya Pradesh">Madhya Pradesh</option>
            <option value="Maharashtra">Maharashtra</option>
            <option value="Manipur">Manipur</option>
            <option value="Meghalaya">Meghalaya</option>
            <option value="Mizoram">Mizoram</option>
            <option value="Nagaland">Nagaland</option>
            <option value="Odisha">Odisha</option>
            <option value="Punjab">Punjab</option>
            <option value="Rajasthan">Rajasthan</option>
            <option value="Sikkim">Sikkim</option>
            <option value="Tamil Nadu" selected="selected">Tamil Nadu</option>
            <option value="Telangana">Telangana</option>
            <option value="Tripura">Tripura</option>
            <option value="Uttar Pradesh">Uttar Pradesh</option>
            <option value="Uttarakhand">Uttarakhand</option>
            <option value="West Bengal">West Bengal</option>
        </select>
        <input style="width: 270px;" type="text" name="pincode" placeholder="Pincode:"/>        <input type="text" name="landmark" placeholder="Landmark:"/>
        
        <h3>Screen:</h3>
        <input style="width: 220px;margin:0px;" type="text" name="noOfSeats" placeholder="No. of Seats:"/> <input type="text" name="showTimes" placeholder="Show Times:"/>
        <h3>Movie:</h3>
        <input style="width: 220px;" type="text" name="movieName" placeholder="Name:"/> 
        <select style="margin:0px 0px 10px 0px;width: 130px;" name="movieType" id="movieType">
            <option >Tamil</option>
            <option >English</option>
        </select>
        <select style="margin:0px 0px 10px 0px;width: 150px;" name="movieStatus" id="movieStatus">
            <option >Available</option>
            <option >NotAvailable</option>
        </select>
        <input type="submit" style="text-align:center;margin-left:50vmin;" value="Add Theater"></input>
		</form>
        </div>
    <%}%>
	</body>
</html>