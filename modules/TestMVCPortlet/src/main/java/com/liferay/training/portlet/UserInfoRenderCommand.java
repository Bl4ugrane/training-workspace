package com.liferay.training.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.constants.SimpleMVCPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + SimpleMVCPortletKeys.SIMPLEMVC,
                "mvc.command.name=/user/info"
        },
        service = MVCRenderCommand.class
)
public class UserInfoRenderCommand implements MVCRenderCommand {

    private static final String USER_INFO = "/user-info.jsp";
    private static final Log log = LogFactoryUtil.getLog(UserInfoRenderCommand.class);

    @Reference
    private volatile UserLocalService userLocalService;

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) {
        try {
            long userId = ParamUtil.getLong(renderRequest, "userId");
            User user = userLocalService.getUser(userId);

            CustomUser customUser = new CustomUser();
            customUser.setUserId(user.getUserId());
            customUser.setFullName(user.getFullName());
            customUser.setEmail(user.getEmailAddress());
            customUser.setBirthday(user.getBirthday());
            customUser.setSex(user.isMale());

            renderRequest.setAttribute("user", customUser);
        } catch (PortalException exception) {
            log.error("Could not map user: " + exception);
        }
        return USER_INFO;
    }
}
