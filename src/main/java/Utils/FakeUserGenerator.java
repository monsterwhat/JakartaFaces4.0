package Utils;

/**
 *
 * @author Al
 */

import Models.Profiles;
import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.Set;

public class FakeUserGenerator {
    private final Faker faker;
    private final Set<String> generatedUsernames;

    public FakeUserGenerator() {
        faker = new Faker();
        generatedUsernames = new HashSet<>();
    }

    private String generateUniqueUsername() {
        String username;
        do {
            username = faker.dragonBall().character();
        } while (generatedUsernames.contains(username));
        
        generatedUsernames.add(username);
        return username;
    }

    public Profiles generateFakeUserProfile() {
        Profiles profile = new Profiles();
        profile.setUsername(generateUniqueUsername());
        profile.setPassword(faker.internet().password());
        profile.setGroupName("user");
        return profile;
    }
    
    public Profiles generateFakeAdminProfile() {
        Profiles profile = new Profiles();
        profile.setUsername(generateUniqueUsername());
        profile.setPassword(faker.internet().password());
        profile.setGroupName("admin");
        return profile;
    }
}

