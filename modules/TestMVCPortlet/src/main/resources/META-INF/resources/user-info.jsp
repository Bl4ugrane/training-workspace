<%@ include file="/init.jsp" %>

<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.liferay.training.portlet.CustomUser"%>

<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String redirect = ParamUtil.getString(request, "backUrl");
SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

long userId = ParamUtil.getLong(request, "userId");
User usr = UserLocalServiceUtil.getUser(userId);
CustomUser customUser = new CustomUser();
customUser.setUserId(usr.getUserId());
customUser.setFullName(usr.getFullName());
customUser.setEmail(usr.getEmailAddress());
customUser.setBirthday(usr.getBirthday());
customUser.setSex(usr.isMale() ? "Мужчина" : "Женщина");
%>

<a href="<%=redirect%>">На главную</a>

<br/><br/>

<h3>Информация пользователя</h3><br/>

<p>Идентификатор пользователя: <%=customUser.getUserId()%></p>
<p>ФИО: <%=customUser.getFullName()%></p>
<p>Дата рождения: <%=sdf.format(customUser.getBirthday())%></p>
<p>Эл. почта: <%=customUser.getEmail()%></p>
<p>Пол: <%=customUser.getSex()%></p>
