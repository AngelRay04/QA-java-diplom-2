import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateExistsUserTest {
    private User userData = User.getRandom();
    private UserClient userClient;

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
    @Description("Нельзя создать уже существующего пользователя")
    public void createExistsUserTest() {
        userClient.create(userData);
        Response response = userClient.create(userData);
        assertEquals(403, response.statusCode());
        assertEquals("User already exists", response.path("message"));
    }
}
