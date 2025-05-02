package Services;

import Models.Profiles;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.Data;

/**
 *
 * @author Al
 */

@Data
@Named
@Stateless
public class ProfilesService extends GService<Profiles> {
    
    @Inject Pbkdf2PasswordHash passwordHasher; 
    
    @Override
    protected Class<Profiles> getEntityClass(){
        return Profiles.class;
    }
    
    @PostConstruct
    public void init(){
        
    }
    
    public boolean verifyHashedPassword(String password, String hashedPassword){
        try {
            return passwordHasher.verify(password.toCharArray(), hashedPassword);
        } catch (Exception e) {
            System.out.println("Error:" + e.getLocalizedMessage());
            return false;
        } 
    }
    
    public String getHashedPassword(String password){
        return passwordHasher.generate(password.toCharArray());
    }
    
    @Override
    public void create(Profiles entity) {
        try {
            var unHashedPassword = entity.getPassword();
            var HashedPassword = passwordHasher.generate(unHashedPassword.toCharArray());
            entity.setPassword(HashedPassword);
            em.persist(entity);
        } catch (Exception e) {
        }
    }
    
    @Override
    public void delete(Profiles entity) {
        try {
            if (!em.contains(entity)) {
                entity = em.find(getEntityClass(), entity.getId());
            }

            if (entity != null) {
                em.remove(entity);
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error deleting "+ getEntityClass().getSimpleName() +" : " + e.toString());
        }
    }
}
