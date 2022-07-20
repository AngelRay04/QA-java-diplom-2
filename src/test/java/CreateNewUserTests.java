import io.qameta.allure.Description;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class CreateNewUserTests {
    private final User user;
    private final int statusCode;
    private final String message;
    private final User userData = User.getRandom();
    private UserClient userClient;

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {User.getRandom(), 200, null},
                {User.getWithEmailAndPassword(), 403, "Email, password and name are required fields"},
                {User.getWithEmailAndName(), 403, "Email, password and name are required fields"},
                {User.getWithNameAndPassword(), 403, "Email, password and name are required fields"}
        };
    }

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
    @Description("Создание нового пользователя")
    public void createNewUserTest() {
        Response response = userClient.create(user);
        assertEquals(statusCode, response.statusCode());
        Assert.assertEquals(message, response.path("message"));
    }
}