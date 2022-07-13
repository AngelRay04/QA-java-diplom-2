import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditUserTests {
    private UserClient userClient;
    static Faker faker = new Faker();

    @Before
    public void setUp() {
        userClient = new UserClient();
        }

    @Test
    @Description("Изменение пароля с авторизацией")
    public void editUserPasswordWithAuthTest() {
        User userData = User.getRandom();
        Response user = userClient.create(userData);
        userClient.login(userData.getEmail(), userData.getPassword());
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(userData.getEmail(), newPassword, userData.getUserName(), user.getBody().path("accessToken"));
        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @Description("Изменение имени с авторизацией")
    public void editUserUserNameWithAuthTest() {
        User userData = User.getRandom();
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        userClient.login(userData.getEmail(), userData.getPassword());
        String newUserName = faker.name().username();
        Response response = userClient.edit(userData.getEmail(), userData.getPassword(), newUserName, accessToken);
        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @Description("Изменение email с авторизацией")
    public void editUserEmailWithAuthTest() {
        User userData = User.getRandom();
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        userClient.login(userData.getEmail(), userData.getPassword());
        String newEmail = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        Response response = userClient.edit(newEmail, userData.getPassword(), userData.getUserName(), accessToken);
        assertEquals( 200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @Description("Нельзя изменить email на уже существующий")
    public void editUserAlreadyExistsEmailWithAuthTest() {
        User userData = User.getRandom();
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        String newEmail = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        userClient.create(newEmail, userData.getPassword(), userData.getUserName());
        userClient.login(userData.getEmail(), userData.getPassword());
        Response response = userClient.edit(newEmail, userData.getPassword(), userData.getUserName(), accessToken);
        assertEquals(403, response.statusCode());
        assertFalse( response.path("success"));
        assertEquals("User with such email already exists", response.path("message"));
    }

    @Test
    @Description("Нельзя изменить пароль без авторизации")
    public void editUserPasswordWithoutAuthTest() {
        User userData = User.getRandom();
        userClient.create(userData);
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = userClient.edit(userData.getEmail(), newPassword, userData.getUserName(), "");
        assertEquals(401, response.statusCode());
        assertFalse( response.path("success"));
        assertEquals("You should be authorised", response.path("message"));
    }

    @Test
    @Description("Нельзя изменить имя без авторизации")
    public void editUserUsernameWithoutAuthTest() {
        User userData = User.getRandom();
        userClient.create(userData);
        String newUserName = faker.name().username();
        Response response = userClient.edit(userData.getEmail(), userData.getPassword(), newUserName, "");
        assertEquals(401, response.statusCode());
        assertFalse( response.path("success"));
        assertEquals("You should be authorised", response.path("message"));
    }

    @Test
    @Description("Нельзя изменить email без авторизации")
    public void editUserEmailWithoutAuthTest() {
        User userData = User.getRandom();
        userClient.create(userData);
        String newEmail = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        Response response = userClient.edit(newEmail, userData.getPassword(), userData.getUserName(), "");
        assertEquals(401, response.statusCode());
        assertFalse(response.path("success"));
        assertEquals("You should be authorised", response.path("message"));
    }
}