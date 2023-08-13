package Controllers;

import Models.Profiles;
import Services.Service;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_CONTINUE;
import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Al
 */

@Named(value = "SessionController")
@SessionScoped
public class SessionController implements Serializable{

    public SessionController() {
    }
    
    @NotEmpty private String username;
    @NotEmpty private String password;
    private Profiles currentUser;
    
    @Inject private Service UserService;
    @Inject FacesContext facesContext;
    @Inject SecurityContext securityContext;

    public void executeLogin(){
        try {
            switch (processAuthentication()) {
                case SEND_CONTINUE -> facesContext.responseComplete();
                case SEND_FAILURE -> {
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
                }
                case SUCCESS -> {
                    currentUser = UserService.getSession(securityContext.getCallerPrincipal().getName());
                    getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/secured/index.xhtml");
                }
                case NOT_DONE -> {
                }
                default -> throw new AssertionError();
            }
            
        } catch (IOException e) {
            System.out.println("Error logging in " + e.getLocalizedMessage());
        }
    }
    
    public String logOut(){
        try {
            this.currentUser = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
            ExternalContext ec = facesContext.getExternalContext();
            ((HttpServletRequest)ec.getRequest()).logout();
            return "/index?faces-redirect=true";
        } catch (ServletException e) {
            
        }
        return null;
    }
    
    public boolean isValid(){
        return currentUser != null;
    }
            
    private AuthenticationStatus processAuthentication(){
        ExternalContext ec = getExternalContext();
        return securityContext.authenticate(
                (HttpServletRequest)ec.getRequest(),
                (HttpServletResponse)ec.getResponse(),
                AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(username,password)));
    }
    
    private ExternalContext getExternalContext(){
     return facesContext.getExternalContext();
    }
        
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
