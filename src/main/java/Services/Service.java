package Services;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

/**
 *
 * @author Al
 */


public class Service {
    
    @PersistenceContext
    EntityManager em;
    
    public Service() {
        
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    public boolean login(String username, String password){
        try {
            System.out.println("Created EM");

            String sqlQuery = "SELECT COUNT(*) FROM users WHERE username = ?1 AND password = ?2";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, username);
            query.setParameter(2, password);

            var result = (Number) query.getSingleResult();  // Cast the result to Number type
            int count = result.intValue();
            
            System.out.println(count);
            return count > 0;
            
        } catch (Exception e) {
            System.out.println("Error: ");
            System.out.println(e);
        return false;
        }
    }
}
