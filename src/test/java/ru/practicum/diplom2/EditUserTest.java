package ru.practicum.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.user.SuccessSignInSignUpResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.example.pojo.BasicResponse;
import org.example.pojo.user.SuccessPatchUserResponse;
import org.example.pojo.user.UserRequest;
import org.example.steps.UsersSteps;
import org.example.utils.UsersUtils;

public class EditUserTest{
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
    @DisplayName("Успешное редактирование всех данных пользователя с авторизацией")
    public void editAllUserDataWithAuthSuccess() {
        UserRequest updatedUser = UsersUtils.getUniqueUser();
        SuccessPatchUserResponse response = UsersSteps.editUserDataWithAuth(accessToken, updatedUser)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessPatchUserResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверное значение поля email", updatedUser.getEmail().toLowerCase(),
                response.getUser().getEmail().toLowerCase());
        Assert.assertEquals("Неверное значение поля name", updatedUser.getName(), response.getUser().getName());
    }

    @Test
    @DisplayName("Успешное редактирование Email пользователя с авторизацией")
    public void editOnlyEmailWithAuthSuccess() {
        UserRequest updatedUser = new UserRequest(RandomStringUtils.randomAlphanumeric(10) + "@test.com",user.getPassword(),user.getName());
        SuccessPatchUserResponse response = UsersSteps.editUserDataWithAuth(accessToken, updatedUser)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessPatchUserResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверное значение поля email", updatedUser.getEmail().toLowerCase(),
                response.getUser().getEmail().toLowerCase());
        Assert.assertEquals("Неверное значение поля name", updatedUser.getName(), response.getUser().getName());
    }

    @Test
    @DisplayName("Успешное редактирование Password пользователя с авторизацией")
    public void editOnlyPassWithAuthSuccess() {
        UserRequest updatedUser = new UserRequest(user.getEmail(),RandomStringUtils.randomAlphanumeric(10),user.getName());
        SuccessPatchUserResponse response = UsersSteps.editUserDataWithAuth(accessToken, updatedUser)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessPatchUserResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверное значение поля email", updatedUser.getEmail().toLowerCase(),
                response.getUser().getEmail().toLowerCase());
        Assert.assertEquals("Неверное значение поля name", updatedUser.getName(), response.getUser().getName());
    }

    @Test
    @DisplayName("Успешное редактирование Name пользователя с авторизацией")
    public void editOnlyNameWithAuthSuccess() {
        UserRequest updatedUser = new UserRequest(user.getEmail(),user.getPassword(),RandomStringUtils.randomAlphanumeric(10));
        SuccessPatchUserResponse response = UsersSteps.editUserDataWithAuth(accessToken, updatedUser)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessPatchUserResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверное значение поля email", updatedUser.getEmail().toLowerCase(),
                response.getUser().getEmail().toLowerCase());
        Assert.assertEquals("Неверное значение поля name", updatedUser.getName(), response.getUser().getName());
    }

    @Test
    @DisplayName("Редактирование данных пользователя без авторизации возвращает ошибку")
    public void editUserDataWithoutAuthReturns401ErrorMessage() {
        UserRequest updatedUser = UsersUtils.getUniqueUser();
        BasicResponse response = UsersSteps.editUserDataWithoutAuth(updatedUser)
                .then()
                .statusCode(401)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "You should be authorised", response.getMessage());
    }

}
