<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Login Page</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
		<form name="mainform" action="login" >
        
        <pre class="login-form">
        <h2 style="text-align:center;">BookMyTicket Login</h2>
    <%session.invalidate();%>
    Username:  <input type="text" name="name" required/><br>		
		
    Mobile no: <input type="text" name="mobNo" required/><br>
            
    Email Id:  <input type="email" name="emailId" required/><br>
    
    Sex:       <input type="text" name="sex" required/><br>
                                        
                                <input type="submit" value="Submit">
            
		</pre>    
		</form>
        
        
	</body>
</html>