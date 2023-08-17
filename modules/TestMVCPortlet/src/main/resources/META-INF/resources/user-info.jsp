<%@ include file="/init.jsp" %>

<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.liferay.training.portlet.CustomUser"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>

<%
String redirect = ParamUtil.getString(request, "backUrl");
SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
CustomUser customUser = (CustomUser) renderRequest.getAttribute("user");
%>

<a href="<%=redirect%>"><liferay-ui:message key="back"/></a>

<br/><br/>

<h3><liferay-ui:message key="user.info" /></h3><br/>

<p><liferay-ui:message key="user.id"/>: <%=customUser.getUserId()%></p>
<p><liferay-ui:message key="user.fullName"/>: <%=customUser.getFullName()%></p>
<p><liferay-ui:message key="user.birthday"/>: <%=sdf.format(customUser.getBirthday())%></p>
<p><liferay-ui:message key="user.email"/>: <%=customUser.getEmail()%></p>
<p><liferay-ui:message key="user.sex"/>:
<%if(customUser.isMale()){%>
<liferay-ui:message key="male"/></p>
<%}
else {%>
<liferay-ui:message key="female"/></p>
<%}
%>
