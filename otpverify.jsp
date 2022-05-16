<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>OTP Verify</title>
        <style><%@include file="/css/style.css"%></style>
	</head>

	<body>
    <%
    session = request.getSession();
    if(session.getAttribute("user") == null){
        response.sendRedirect("/BookMyTicket/index.jsp");
    } %>
		<pre  >
        
        <form class="verify-form" action="login" method="POST"> 
        <h2 style="text-align:center;">BookMyTicket Verification</h3>
        <h3>   OTP is sent to your Mobile Number & Mail</h3>
    Enter OTP:<input type="text" name="otp"/> 		
		
                                <input type="submit" value="Verify">	
       
		
		</form>
        </pre >
       
	</body>
    
</html>