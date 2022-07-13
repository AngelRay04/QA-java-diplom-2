import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateExistsUserTest {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @Description("Нельзя создать уже существующего пользователя")
    public void createExistsUserTest() {
        User userData = User.getRandom();
        userClient.create(userData);
        Response response = userClient.create(userData);
        assertEquals(403, response.statusCode());
        assertEquals("User already exists", response.path("message"));
    }
}
