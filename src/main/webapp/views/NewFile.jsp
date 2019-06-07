<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
  Date date = new Date();
  SimpleDateFormat tokyoSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  tokyoSdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
  String strDate = tokyoSdf.format(date);
%>
現在日時：<%= strDate %>
</body>
</html>