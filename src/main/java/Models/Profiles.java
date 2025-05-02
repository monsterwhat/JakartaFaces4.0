package Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Al
 */

@Data
@Entity
@Table(name = "Profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Profiles implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String username;
    
    @Column
    private String password;
    
    @Column
    private String groupName;

    public Profiles() {
    }

    public Profiles(Long id, String username, String password, String groupName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.groupName = groupName;
    }
  
}
