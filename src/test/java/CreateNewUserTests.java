import io.qameta.allure.Description;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class CreateNewUserTests {
    private UserClient userClient;
    private final User user;
    private final int statusCode;
    private final String message;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][]{
                {User.getRandom(), 200, null},
                {User.getWithEmailAndPassword(), 403, "Email, password and name are required fields"},
                {User.getWithEmailAndName(), 403, "Email, password and name are required fields"},
                {User.getWithNameAndPassword(), 403, "Email, password and name are required fields"}
        };
    }

    @Test
    @Description("Создание нового пользователя")
    public void createNewUserTest() {
        Response response = userClient.create(user);
        assertEquals(statusCode, response.statusCode());
        Assert.assertEquals(message, response.path("message"));
    }
}