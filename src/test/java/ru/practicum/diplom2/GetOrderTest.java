package ru.practicum.diplom2;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.pojo.order.OrderRequest;
import org.example.pojo.user.SuccessSignInSignUpResponse;
import org.example.pojo.user.UserRequest;
import org.example.steps.UsersSteps;
import org.example.utils.OrderUtils;
import org.example.utils.UsersUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.example.pojo.BasicResponse;
import org.example.pojo.order.GetUserOrdersResponse;
import org.example.steps.OrderSteps;

public class GetOrderTest{
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

    @DisplayName("Получение заказов пользователя с авторизацией проходит успешно")
    @Test
    public void getUserOrderWithAuthSuccess() {
        OrderRequest orderRequest = new OrderRequest(OrderUtils.getCorrectIngredients());
        OrderSteps.createOrderWithAuth(accessToken,orderRequest);
        GetUserOrdersResponse response = OrderSteps.getUsersOrdersWithAuth(accessToken)
                .then()
                .statusCode(200)
                .extract()
                .as(GetUserOrdersResponse.class);

        Assert.assertTrue("Запрос должен быть успешным", response.isSuccess());
        Assert.assertFalse("Список заказов не должен быть пустым", response.getOrders().isEmpty());
        Assert.assertNotEquals("Общее число заказов не должно быть равно 0", 0, response.getTotal());
        Assert.assertNotEquals("Число заказов за сегодня не должно быть равно 0", 0,
                response.getTotalToday());
    }

    @DisplayName("Получение заказов пользователя без авторизации не выполняется")
    @Test
    public void getUserOrderWithoutAuth401ErrorMessage() {
        BasicResponse response = OrderSteps.getUsersOrdersWithoutAuth()
                .then()
                .statusCode(401)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "You should be authorised", response.getMessage());
    }
}
