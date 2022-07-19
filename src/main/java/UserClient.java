import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private static final String AUTH_PATH = "api/auth/";

    @Step("Создать пользователя")
    public Response create(String email, String password, String userName) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .put("name", userName)
                .toString();
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(AUTH_PATH + "register/");
    }

    @Step("Создать пользователя")
    public Response create(User user) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", user.getEmail())
                .put("password", user.getPassword())
                .put("name", user.getUserName())
                .toString();
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(AUTH_PATH + "register/");
    }

    @Step("Залогиниться")
    public Response login(String email, String password) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .toString();
        return given()
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .post(AUTH_PATH + "login/");
    }

    @Step("Редактировать пользователя")
    public Response edit(String email, String password, String userName, String token) {
        JSONObject requestBodyJson = new JSONObject();
        String requestBody = requestBodyJson
                .put("email", email)
                .put("password", password)
                .put("name", userName)
                .toString();
        return given()
                .header("Authorization", token)
                .spec(getBaseSpec())
                .body(requestBody)
                .when()
                .patch(AUTH_PATH + "user/");
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accesstoken) {
        return given()
                .headers("Authorization", accesstoken)
                .spec(getBaseSpec())
                .delete("auth/user")
                .then();
    }
}