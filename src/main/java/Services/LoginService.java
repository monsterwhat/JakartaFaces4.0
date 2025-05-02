package Services;


import Models.Profiles;
import jakarta.annotation.PostConstruct; 
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash; 
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Al
 */

@Data
@Named
@Stateless
public class LoginService extends GService<Profiles>{
        
    @Inject Pbkdf2PasswordHash passwordHasher; 
 
    @Override
    protected Class<Profiles> getEntityClass(){
        return Profiles.class;
    }
     
    @PostConstruct
    @Transactional
    public void init() {
        InsertAdmin();
    }
    
    public boolean verifyPassword(char[] password, String hashedPassword){
        return passwordHasher.verify(password, hashedPassword);
    }
        
    public Profiles getSession(String username) {
        try {
            TypedQuery<Profiles> query = em.createQuery("SELECT u FROM Profiles u WHERE u.username = :username", Profiles.class);
            query.setParameter("username", username);

            List<Profiles> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error: ");
            System.out.println(e);
            return null;
        }
    }
     
    @Transactional
    public void InsertAdmin() {  
        try {
             
            String username = "Admin";
            TypedQuery<Profiles> query = em.createQuery("SELECT u FROM Profiles u WHERE u.username = :username", Profiles.class);
            query.setParameter("username", username);

            try {
                
                query.getSingleResult();
                
            } catch (NoResultException e) {
                Profiles user = new Profiles();
                user.setUsername(username);
                user.setPassword(passwordHasher.generate("password123".toCharArray()));
                user.setGroupName("admin");

                em.persist(user);
            }
        } catch (Exception e) {
            System.out.println("Error in InsertAdmin! Error: " + e.toString());
        }
    }
 
}
