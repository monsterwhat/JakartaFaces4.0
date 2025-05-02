package Controllers;

import Models.Profiles;
import Services.LoginService;
import Services.ProfilesService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import static jakarta.security.enterprise.AuthenticationStatus.NOT_DONE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Al
 */

@Data
@Named(value = "SessionController")
@SessionScoped
public class SessionController implements Serializable {

    public SessionController() {
    }

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private Profiles currentUser;

    @Inject private ProfilesService profileService;
    @Inject private LoginService loginService; 
    @Inject FacesContext facesContext;
    @Inject SecurityContext securityContext;

    @PostConstruct
    public void init() {
        loginService.init();
    }

    public void executeLogin() {
        try {
            switch (processAuthentication()) {
                case SEND_CONTINUE ->
                    facesContext.responseComplete();
                case SEND_FAILURE -> { 
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", ""));
                }
                case SUCCESS -> {
                    if (securityContext.getCallerPrincipal().getName() != null) {
                        currentUser = loginService.getSession(securityContext.getCallerPrincipal().getName());

                        redirectToSecuredArea();
                    }
                }
                case NOT_DONE -> {
                }

                default ->
                    throw new AssertionError();
            }

        } catch (IOException e) {
            System.out.println("Error logging in " + e.getLocalizedMessage());
        }
    }

    private void redirectToSecuredArea() throws IOException {
        ExternalContext ec = facesContext.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/secured/index");
    }

    public void logOut() {
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/index");
            ec.invalidateSession(); // Invalidate the session
            this.currentUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
        } catch (IOException e) {
            // Handle the IOException
            e.printStackTrace();

        }
    }

    public boolean isValid() {
        return currentUser != null;
    }

    private AuthenticationStatus processAuthentication() {
        ExternalContext ec = getExternalContext();
        return securityContext.authenticate(
                (HttpServletRequest) ec.getRequest(),
                (HttpServletResponse) ec.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(username, password)));
    }

    private ExternalContext getExternalContext() {
        return facesContext.getExternalContext();
    }

}
