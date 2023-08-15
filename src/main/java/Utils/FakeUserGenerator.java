package Utils;

/**
 *
 * @author Al
 */

import Models.Profiles;
import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class FakeUserGenerator {
    private final Faker faker;
    private static Set<String> generatedUsernames = new HashSet<>();
    private Supplier<String> usernameGenerator;

    public FakeUserGenerator() {
        faker = new Faker();
        setUsernameGenerator("dragonBall");
    }
    
    public void setUsernameGenerator(String input) {
        switch (input) {
            case "dragonBall" -> usernameGenerator = () -> faker.dragonBall().character();
            case "harryPotter" -> usernameGenerator = () -> faker.harryPotter().character();
            case "rickandMorty" -> usernameGenerator = () -> faker.rickAndMorty().character();

            default -> usernameGenerator = () -> faker.hobbit().character();
        }
    }

    private String generateUniqueUsername() {
        String username;
        do {
            username = usernameGenerator.get();
        } while (generatedUsernames.contains(username));

        generatedUsernames.add(username);
        return username;
    }
    
    public Profiles generateFakeUserProfile(String groupName) {
        Profiles profile = new Profiles();
        profile.setUsername(generateUniqueUsername());
        profile.setPassword(faker.internet().password());
        profile.setGroupName(groupName);
        return profile;
    }
}