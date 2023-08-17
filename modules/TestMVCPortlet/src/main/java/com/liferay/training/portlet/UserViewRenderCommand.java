package com.liferay.training.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationModel;
import com.liferay.portal.kernel.model.PhoneModel;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.constants.SimpleMVCPortletKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + SimpleMVCPortletKeys.SIMPLEMVC,
                "mvc.command.name=/",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
        },
        service = MVCRenderCommand.class
)
public class UserViewRenderCommand implements MVCRenderCommand {

    private static final String USER_VIEW = "/view.jsp";
    private static final Log log = LogFactoryUtil.getLog(UserViewRenderCommand.class);

    @Reference
    private volatile RoleLocalService roleLocalService;
    @Reference
    private volatile UserLocalService userLocalService;

    @Override
    public String render(RenderRequest request, RenderResponse response) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        User currentUser = themeDisplay.getUser();

        List<User> users = new ArrayList<>();
        List<CustomUser> usersForDisplay = new ArrayList<>();

        try {
            long[] organizationIds = currentUser.getOrganizationIds();

            for (long organizationId : organizationIds) {
                users.addAll(userLocalService.getOrganizationUsers(organizationId, -1, -1));
            }
        } catch (Exception exception) {
            log.error("Could not get users: " + exception);
        }

        for (User user : users) {
            try {
                CustomUser customUser = new CustomUser();
                customUser.setUserId(user.getUserId());
                customUser.setFullName(user.getFullName());
                customUser.setEmail(user.getEmailAddress());
                customUser.setBirthday(user.getBirthday());

                List<String> phones = new ArrayList<>();
                List<String> organizations = new ArrayList<>();
                List<String> roles = new ArrayList<>();
                List<UserGroupRole> userGroupRoles = new ArrayList<>();

                if (!user.getPhones().isEmpty()) {
                    phones.addAll(user.getPhones().stream().map(PhoneModel::getNumber).collect(Collectors.toList()));
                }

                if (!user.getOrganizations().isEmpty()) {
                    organizations.addAll(user.getOrganizations().stream().map(OrganizationModel::getName).collect(Collectors.toList()));

                    List<Long> groupIds = user.getOrganizations().stream().map(Organization::getGroupId).collect(Collectors.toList());

                    if (!groupIds.isEmpty()) {
                        groupIds.forEach(groupId -> userGroupRoles.addAll(UserGroupRoleLocalServiceUtil.getUserGroupRolesByGroup(groupId)));

                        roles.addAll(userGroupRoles.stream().map(userGroupRole -> {
                            try {
                                Role role = userGroupRole.getRole();
                                if (role != null) return role.getName();
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
        return USER_VIEW;
    }
}
