package com.liferay.training.portlet;

import com.liferay.training.constants.SimpleMVCPortletKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=SimpleMVC",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + SimpleMVCPortletKeys.SIMPLEMVC,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class SimpleMVCPortlet extends MVCPortlet {
		
	private static final String ADMIN = "Administrator";
	
	private static Log log = LogFactoryUtil.getLog(SimpleMVCPortlet.class);
	
	@Reference
	private volatile UserLocalService userLocalService;
	
	@Override
	public void render(RenderRequest request, RenderResponse response) throws IOException, PortletException {

		 ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        
         List<Role> userRoles = themeDisplay.getUser().getRoles();
    		 
         boolean isAdmin = userRoles.stream().anyMatch(role -> ADMIN.equals(role.getName()));
         
         List<User> users = new ArrayList<>();
         List<CustomUser> usersForDisplay = new ArrayList<>();
        	 
         try {
 		 if(isAdmin) {
 			users = userLocalService.getUsers(0, userLocalService.getUsersCount());
 		 } else {
 			long[] organizationIds = themeDisplay.getUser().getOrganizationIds();
 			
 			for(int i=0; i<organizationIds.length; i++) {
 				users.addAll(userLocalService.getOrganizationUsers(organizationIds[i], 0, userLocalService.getOrganizationUsersCount(organizationIds[i])));
 	 		 }
 		   }
         } catch(Exception exception) {
        	 log.error("Could not get users: " + exception);
         }
         
         for(User user: users) {
            try {
        	CustomUser customUser = new CustomUser();
        	customUser.setUserId(user.getUserId());
        	customUser.setFullName(user.getFullName());
        	customUser.setEmail(user.getEmailAddress());
			customUser.setBirthday(user.getBirthday());
		  	customUser.setSex(user.isMale() ? "Мужчина" : "Женщина");
		  	
		  	List<String> phones = new ArrayList<>();
		  	List<String> organizations = new ArrayList<>();
		  	List<String> roles = new ArrayList<>();
			List<UserGroupRole> userGroupRoles = new ArrayList<>();
		  	
		  	if(!user.getPhones().isEmpty()) {
		  		phones.addAll(user.getPhones().stream().map(phone -> phone.getNumber()).collect(Collectors.toList()));
		  	}
		  	
			if(!user.getOrganizations().isEmpty()) {
				organizations.addAll(user.getOrganizations().stream().map(org -> org.getName()).collect(Collectors.toList()));
				
				List<Long> groupIds = user.getOrganizations().stream().map(org -> org.getGroupId()).collect(Collectors.toList());
				
				if(!groupIds.isEmpty()) {
					groupIds.forEach(groupId -> userGroupRoles.addAll(UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroup(groupId)));
					
					roles.addAll(userGroupRoles.stream().map(userGroupRole -> {
						try {
							if(userGroupRole.getRole() != null)
							return userGroupRole.getRole().getName();
						} catch (PortalException exception) {
							 log.error("Could not get userGroupRoles: " + exception);
						}
						return "";
					}).collect(Collectors.toList()));
	 	 		 }
			}
			
			customUser.setPhones(phones);
			customUser.setOrganizations(organizations);
			customUser.setPosition(roles);
		  	
		  	usersForDisplay.add(customUser);
			} catch (Exception exception) {
				 log.error("Could not map users: " + exception);
			}
         }
         
         log.info("users: " + usersForDisplay);
       		 
         request.setAttribute("users", usersForDisplay);
                
         super.render(request, response);
	}
}