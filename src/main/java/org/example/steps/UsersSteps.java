package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.pojo.user.UserRequest;
import org.example.pojo.user.SignInRequest;

import static org.example.constants.UserConstants.*;

public class UsersSteps {

    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_TEST_URL)
                    .setBasePath(BASE_AUTH_URL)
                    .setContentType(ContentType.JSON)
                    .build();

    @Step("Создание юзера")
    public static Response createUniqueUser(UserRequest body) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(BASE_REGISTER_URL);
    }


    @Step("Выполнение авторизации")
    public static Response signInWithSignInRequest(SignInRequest signInRequest) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(signInRequest)
                .when()
                .post(BASE_LOGIN_URL);
    }


    @Step("Редактирование информации о юзере")
    public static Response editUserDataWithAuth(String accessToken, UserRequest updatedUser) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch(BASE_USER_URL);
    }

    @Step("Редактирование информации о юзере без авторизации")
    public static Response editUserDataWithoutAuth(UserRequest updatedUser) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(updatedUser)
                .when()
                .patch(BASE_USER_URL);
    }

    @Step("Удаляем пользователя")
    public static Response deleteUser(String accessToken) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_USER_URL);
    }
}
