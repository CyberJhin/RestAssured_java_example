package ru.practicum.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.example.pojo.BasicResponse;
import org.example.pojo.user.SuccessSignInSignUpResponse;
import org.example.pojo.user.UserRequest;
import org.example.steps.UsersSteps;
import org.example.utils.UsersUtils;

public class CreateUserTest {
    String accessToken;

    @Before
    public void init() throws InterruptedException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        //попытаться избежать 429
        Thread.sleep(4000);
    }

    @After
    public void shutdown() {
        if (accessToken != null)
            UsersSteps
                    .deleteUser(accessToken)
                    .then()
                    .statusCode(202);
    }

    @Test
    @DisplayName("Успешная регистрация уникального пользователя")
    public void registerUniqueUserSuccess() {
        UserRequest body = UsersUtils.getUniqueUser();

        SuccessSignInSignUpResponse response = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class);
        accessToken = response.getAccessToken();
        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertFalse("Неверное значения поля refreshToken", response.getRefreshToken().isBlank());
        Assert.assertFalse("Неверное значения поля accessToken", response.getAccessToken().isBlank());
        Assert.assertEquals("Неверное значения поля email" + body.getEmail().toLowerCase(), body.getEmail().toLowerCase(), response.getUser().getEmail().toLowerCase());
        Assert.assertEquals("Неверное значения поля name", body.getName(), response.getUser().getName());
    }

    @Test
    @DisplayName("При создании дубликата пользователя возвращается 403 ошибка")
    public void registerNotUniqueUserReturns403ErrorMessage() {
        UserRequest body = UsersUtils.getUniqueUser();

        SuccessSignInSignUpResponse responseFirst = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class);
        accessToken = responseFirst.getAccessToken();

        BasicResponse responseSecond = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(403)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", responseSecond.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "User already exists", responseSecond.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя без пароля возвращается 403 ошибка")
    public void registerUserWithoutPasswordReturns403ErrorMessage() {
        UserRequest body = UsersUtils.getUniqueUser();
        body.setPassword("");
        BasicResponse response = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(403)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "Email, password and name are required fields", response.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя без пароля возвращается 403 ошибка")
    public void registerUserWithoutEmailReturns403ErrorMessage() {
        UserRequest body = UsersUtils.getUniqueUser();
        body.setEmail("");
        BasicResponse response = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(403)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "Email, password and name are required fields", response.getMessage());
    }

    @Test
    @DisplayName("При создании пользователя без пароля возвращается 403 ошибка")
    public void registerUserWithoutEmailAndPassReturns403ErrorMessage() {
        UserRequest body = UsersUtils.getUniqueUser();
        body.setEmail("");
        body.setPassword("");
        BasicResponse response = UsersSteps.createUniqueUser(body)
                .then()
                .statusCode(403)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "Email, password and name are required fields", response.getMessage());
    }
}
