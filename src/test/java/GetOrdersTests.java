import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class GetOrdersTests {
    OrderClient orderClient;
    UserClient userClient;
    IngredientsClient ingredientsClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        ingredientsClient = new IngredientsClient();
    }

    @Test
    @Description("Получение заказа с авторизацией")
    public void getOrderWithAuthTest() {
        User userData = User.getRandom();
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        List<String> ingredients  = ingredientsClient.getIngredients().path("data._id");
        orderClient.createOrder(ingredients, accessToken);
        Response response = orderClient.getOrders(accessToken);
        assertEquals(200, response.statusCode());
        assertTrue(response.path("success"));
        assertThat("Orders is null", response.path("orders"), notNullValue());
    }

    @Test
    @Description("Нельзя получить заказ без авторизации")
    public void getOrderWithoutAuthTest() {
        User userData = User.getRandom();
        String accessToken = userClient.create(userData).getBody().path("accessToken");
        List<String> ingredients  = ingredientsClient.getIngredients().path("data._id");
        orderClient.createOrder(ingredients, accessToken);
        Response response = orderClient.getOrders("");
        assertEquals(401, response.statusCode());
        assertFalse(response.path("success"));
        assertEquals("You should be authorised", response.path("message"));
    }
}
