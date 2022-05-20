<%@ page import="java.util.*,com.zoho.booktickets.model.*,com.zoho.booktickets.jsonutil.*,
java.time.LocalDateTime,com.zoho.booktickets.service.*, com.duosecurity.duoweb.*"%>

<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
		<title>Reset Password</title>
        <style><%@include file="/css/style.css"%></style>
        <script><%@include file="/js/Duo-Web-v1.bundled.js"%></script>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
	</head>

	<body>
     <%if(session.getAttribute("message") != null && session.getAttribute("userName") != null){%>
     
        <p style="margin-left:600px;margin-top: 200px;"><%= session.getAttribute("message")%></p>   
        <%session.invalidate();%>
		</pre>    
		</form>
    <%}else if( session.getAttribute("userName") != null &&   request.getParameter("sig_response") == null){
        String sigReq = (session.getAttribute("sigRequest")).toString();
        %>
        
        <script src="/js/Duo-Web-v1.bundled.js" type="text/javascript"></script>        
        <script>
           
            Duo.init({
                'host': 'api-56ffff6d.duosecurity.com',
                'sig_request': '<%= sigReq%>',
                'post_action': 'reset.jsp'
            });
        </script>
        <iframe id="duo_iframe" width="100%" height="500" frameborder="0"></iframe>
       
        <style>
        #duo_iframe {
            width: 100%;
            margin-left: 450px;
            margin-top: 150px;
            min-width: 304px;
            max-width: 620px;
            height: 330px;
            border: none;
        }
        </style>
        
    <%}else if(session.getAttribute("userName") != null && request.getParameter("sig_response") != null){
        %>

    <form name="mainform" action="reset" method="POST" >
        
        <pre class="login-form">
        <h2 style="text-align:center;">BookMyTicket Reset Password</h2>
    
   New Password     <input type="text" name="newPassword" required/><br>		
		
   Confirm Password <input type="text" name="confirmPassword" required/><br>
                                        
    <input type="submit" value="Submit"> <input type="reset" value="Clear">
            
		</pre>    
		</form>
    <%}else{%>

    <div class="expired">
     <div class="message">
        <h1>Oops, this link is expired</h1>
        <p>This URL is not valid anymore.</p>
     </div>
    </div>
        
    <%}%>
		
	</body>
</html>