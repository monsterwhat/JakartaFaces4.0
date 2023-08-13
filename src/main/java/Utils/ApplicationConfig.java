package Utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 *
 * @author Al
 */

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/MySQL",
        callerQuery = "select password from Profiles where username = ?",
        groupsQuery = "select groupName from Profiles where username = ?"
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
        loginPage = "/index",
        errorPage = "",
        useForwardToLogin = false)
)
@FacesConfig
@ApplicationScoped
public class ApplicationConfig {
    
}
