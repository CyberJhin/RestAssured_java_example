package ru.practicum.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.user.UserRequest;
import org.example.utils.UsersUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.example.pojo.BasicResponse;
import org.example.pojo.user.SignInRequest;
import org.example.pojo.user.SuccessSignInSignUpResponse;
import org.example.steps.UsersSteps;

public class LoginUserTest{
    UserRequest user;
    String accessToken;

    @Before
    public void init() throws InterruptedException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        user = UsersUtils.getUniqueUser();
        accessToken = UsersSteps
                .createUniqueUser(user)
                .as(SuccessSignInSignUpResponse.class)
                .getAccessToken();
        //попытаться избежать 429
        Thread.sleep(4000);
    }
    @After
    public void shutdown() {
        if(accessToken!=null)
            UsersSteps
                    .deleteUser(accessToken)
                    .then()
                    .statusCode(202);
    }

    @Test
    @DisplayName("Успешная авторизация с верными кредами")
    public void signInWithValidDataSuccess() {
        SignInRequest body = new SignInRequest(user.getEmail(), user.getPassword());
        SuccessSignInSignUpResponse response = UsersSteps.signInWithSignInRequest(body)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessSignInSignUpResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertFalse("Неверное значения поля accessToken", response.getAccessToken().isBlank());
        Assert.assertFalse("Неверное значения поля refreshToken", response.getRefreshToken().isBlank());
        Assert.assertEquals("Неверное значения поля name", user.getName(), response.getUser().getName());
        Assert.assertEquals("Неверное значения поля email", user.getEmail().toLowerCase(), response.getUser().getEmail().toLowerCase());
    }

    @Test
    @DisplayName("Авторизация с неверными кредами возвращает 401")
    public void signInWithInvalidDataReturns401ErrorMessage() {
        SignInRequest body = new SignInRequest(RandomStringUtils.randomAlphanumeric(10) + "@test.com", RandomStringUtils.randomAlphanumeric(10));
        BasicResponse response = UsersSteps.signInWithSignInRequest(body)
                .then()
                .statusCode(401)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "email or password are incorrect", response.getMessage());
    }

}
