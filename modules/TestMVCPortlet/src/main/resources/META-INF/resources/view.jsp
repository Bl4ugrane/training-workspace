<%@ include file="/init.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.training.portlet.CustomUser"%>

<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>

<%
String currentUrl = PortalUtil.getCurrentURL(request);
List<CustomUser> users = (List<CustomUser>) renderRequest.getAttribute("users");
%>

<liferay-portlet:renderURL varImpl="iteratorUrl"/>
<liferay-ui:search-container total="<%=users.size()%>" var="searchContainer" delta="2" deltaConfigurable="true" emptyResultsMessage="Oops. There Are No Users To Display, Please add Users" iteratorURL="<%=iteratorUrl%>">
 <liferay-ui:search-container-results results="<%=ListUtil.subList(users, searchContainer.getStart(),searchContainer.getEnd())%>" />
 
  <liferay-ui:search-container-row className="com.liferay.training.portlet.CustomUser" modelVar="customUser" keyProperty="user" >
  
   <liferay-ui:search-container-column-text name="user.id" value="${customUser.userId}"/>
   
   <portlet:renderURL var="userInfoUrl">
	<portlet:param name="mvcRenderCommandName" value="/user/info"/>
	<portlet:param name="backUrl" value="<%=currentUrl%>"/>
	<portlet:param name="userId" value="${customUser.userId}"/>
   </portlet:renderURL>
   
   <liferay-ui:search-container-column-text name="user.fullName" property="fullName" href="${userInfoUrl}"/>
     
   <%SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");%>
   
   <liferay-ui:search-container-column-text name="user.birthday" value="<%=sdf.format(customUser.getBirthday())%>"/>
   <liferay-ui:search-container-column-text name="user.email" value="${customUser.email}"/>
   <liferay-ui:search-container-column-text name="user.position" value="<%=customUser.getPosition().isEmpty() ? "" : customUser.getPosition().toString()%>"/>
   <liferay-ui:search-container-column-text name="user.phones" value="<%=customUser.getPhones().isEmpty() ? "" : customUser.getPhones().toString()%>"/>
   <liferay-ui:search-container-column-text name="user.organizations" value="<%=customUser.getOrganizations().isEmpty() ? "" : customUser.getOrganizations().toString()%>"/>
   
  </liferay-ui:search-container-row>
 <liferay-ui:search-iterator searchContainer="<%=searchContainer%>" paginate="<%=true%>"/>
</liferay-ui:search-container>