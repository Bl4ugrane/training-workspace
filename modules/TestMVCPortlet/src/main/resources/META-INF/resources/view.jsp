<%@ include file="/init.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.training.portlet.CustomUser"%>

<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<%
String currentUrl = PortalUtil.getCurrentURL(request);
List<CustomUser> users = (List<CustomUser>) renderRequest.getAttribute("users");
%>

<liferay-portlet:renderURL varImpl="iteratorUrl"/>
<liferay-ui:search-container total="<%=users.size()%>" var="searchContainer" delta="10" deltaConfigurable="true" emptyResultsMessage="Oops. There Are No Users To Display, Please add Users" iteratorURL="<%=iteratorUrl%>">
 <liferay-ui:search-container-results results="<%=ListUtil.subList(users, searchContainer.getStart(),searchContainer.getEnd())%>" />
 
  <liferay-ui:search-container-row className="com.liferay.training.portlet.CustomUser" modelVar="customUser" keyProperty="userId" >
  
   <liferay-ui:search-container-column-text name="Идентификатор пользователя" value="${customUser.userId}"/>
   
   <liferay-portlet:renderURL varImpl="userInfoUrl">
    <portlet:param name="backUrl" value="<%=currentUrl%>" />
    <portlet:param name="mvcPath" value="/user-info.jsp" />
	<portlet:param name="userId" value="<%=String.valueOf(customUser.getUserId())%>" />
   </liferay-portlet:renderURL>
   
   <liferay-ui:search-container-column-text name="ФИО" property="fullName" href="<%=userInfoUrl.toString()%>"/>
     
   <%SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");%>
   
   <liferay-ui:search-container-column-text name="Дата рождения" value="<%=sdf.format(customUser.getBirthday()) + " г."%>"/>
   <liferay-ui:search-container-column-text name="Эл. почта" value="${customUser.email}"/>
   <liferay-ui:search-container-column-text name="Должность" value="<%=customUser.getPosition().isEmpty() ? "" : customUser.getPosition().toString()%>"/>
   <liferay-ui:search-container-column-text name="Номера телефонов" value="<%=customUser.getPhones().isEmpty() ? "" : customUser.getPhones().toString()%>"/>
   <liferay-ui:search-container-column-text name="Организации" value="<%=customUser.getOrganizations().isEmpty() ? "" : customUser.getOrganizations().toString()%>"/>
   
  </liferay-ui:search-container-row>
 <liferay-ui:search-iterator searchContainer="<%=searchContainer%>" paginate="<%=true%>"/>
</liferay-ui:search-container>