<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.carpark.member.model.*"%>  
<%
String root = request.getContextPath();
MessageDto messageDto = (MessageDto) request.getAttribute("messageView");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link href="<%=root %>/css/bootstrap.min.css" rel="stylesheet">
	<link href="<%=root %>/css/stylish-portfolio.css" rel="stylesheet">
	<!-- jQuery -->
    <script src="<%=root %>/js/jquery.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<%=root %>/js/bootstrap.min.js"></script>
    <!-- message.js -->
    <script type="text/javascript" src="<%=root %>/message/message.js"></script>
</head>
<body>
    <form name="common" method="get" action="">
	<input type="hidden" name="act" id="act" value="">
	<input type="hidden" name="bcode" id="bcode" value="2">
	<input type="hidden" name="pg" id="pg" value="1">
	<input type="hidden" name="key" id="key" value="">
	<input type="hidden" name="word" id="word" value="">
	<input type="hidden" name="seq" id="seq" value="">
	<input type="hidden" name="receiver" id="receiver" value="">
</form>
<br>
<div id="page-wrapper">
<div class="container-fluid">
	<p>
	<input type="button" class="btn btn-default" value="삭제" onclick="javascript:messageDelete('<%=messageDto.getSeq()%>,<%=messageDto.getReceiverId() %>');">
	</p>
	<table class="table">
		<tr>
			<td width="80">받는사람</td>
			<td><%=messageDto.getReceiverId() %></td>
			<td width="50">시간</td>
			<td width="80"><%=messageDto.getLogtime() %></td>
			</tr>
			<tr>
			<td width="80">제목</td>
			<td colspan="3"><%=messageDto.getSubject() %></td>
			</tr>
			<tr>
			<td width="80">내용</td>
			<td colspan="3" height="350"><p><%=messageDto.getContent() %></p></td>
			</tr>
		<tr>
			<td colspan="4" align="center">
			<p>
			<input type="button" class="btn btn-default" value="닫기" onclick="javascript:messageViewClose();">
			</p>
			</td>
		</tr>
	</table>
</div>
</div>
</body>
</html>

	