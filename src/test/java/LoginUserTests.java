import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginUserTests {

    private UserClient userClient;
    private final User userData = User.getRandom();

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void deleteUser() {
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @Description("Залогиниться с корректными данными")
    public void loginUser() {
        userClient.create(userData);
        Response response = userClient.login(userData.getEmail(), userData.getPassword());
        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
    }

    @Test
    @Description("Нельзя залогиниться с некорректным логином или паролем")
    public void loginUserWithIncorrectPasswordTest() {
        User userData = User.getRandom();
        userClient.create(userData);
        Response response = userClient.login(userData.getEmail(), "test");
        assertEquals(401, response.statusCode());
        assertFalse(response.path("success"));
        assertEquals("email or password are incorrect", response.path("message"));
    }
}