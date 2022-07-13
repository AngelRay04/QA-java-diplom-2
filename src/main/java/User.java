import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    static Faker faker = new Faker();

    private String email;
    private String password;
    private String userName;

    public static User getRandom(){
        final String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String userName = faker.name().username();
        return new User(email, password, userName);
    }

    public User setEmailAndPassword(String email, String password) {
        this.email = email;
        this.password = password;
        return this;
    }

    public User setEmailAndName(String email, String userName) {
        this.email = email;
        this.userName = userName;
        return this;
    }

    public User setNameAndPassword(String userName, String password) {
        this.userName = userName;
        this.password = password;
        return this;
    }

    public static User getWithEmailAndPassword() {
        return new User().setEmailAndPassword(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", RandomStringUtils.randomAlphabetic(10));
    }

    public static User getWithEmailAndName() {
        return new User().setEmailAndName(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru", faker.name().firstName());
    }

    public static User getWithNameAndPassword() {
        return new User().setNameAndPassword(faker.name().username(), RandomStringUtils.randomAlphabetic(10));
    }
}