<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
   // boolean logged = (boolean) session.getAttribute("logged");
%> 

<%if (request.getSession().getAttribute("logged") != null ) {%>
    <%@include file="WEB-INF/jsp/administrador.jsp" %>
<%} else {%>
    <%@include file="WEB-INF/jsp/login.jsp" %>
<%}%>
