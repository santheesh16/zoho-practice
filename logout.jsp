
<%
session = request.getSession();
if(session != null){
    session.invalidate();
}
response.sendRedirect("index.jsp");    

%>