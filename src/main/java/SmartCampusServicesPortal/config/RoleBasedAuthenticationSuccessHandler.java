package SmartCampusServicesPortal.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(RoleBasedAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        logger.info("Authentication successful for user: " + authentication.getName());
        logger.info("User authorities: " + authentication.getAuthorities());

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        logger.info("Redirecting to: " + targetUrl);
        response.sendRedirect(targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case "STUDENT":
                    return "/student/dashboard";
                case "LECTURER":
                    return "/lecturer/dashboard";
                case "ADMIN":
                    return "/admin/dashboard";
            }
        }

        throw new IllegalStateException("User has no role assigned");
    }
}
