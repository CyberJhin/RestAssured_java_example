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
import org.example.pojo.order.GetOrderResponse;
import org.example.pojo.BasicResponse;
import org.example.pojo.order.SuccessCreateOrderResponse;
import org.example.steps.OrderSteps;

public class CreateOrderTest {

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

    @DisplayName("Создание заказа с авторизацией и ингредиентами проходит успешно")
    @Test
    public void createOrderWithAuthSuccess() {
        OrderRequest orderRequest = new OrderRequest(OrderUtils.getCorrectIngredients());
        SuccessCreateOrderResponse getOrderResponse = OrderSteps.createOrderWithAuth(accessToken,orderRequest)
                .then()
                .statusCode(200)
                .extract()
                .as(SuccessCreateOrderResponse.class);

        GetOrderResponse order = getOrderResponse.getOrder();

        Assert.assertTrue("Запрос не выполняется", getOrderResponse.isSuccess());
        Assert.assertFalse("Имя не должно быть пустым", getOrderResponse.getName().isBlank());
        Assert.assertFalse("Заказ не может быть без ингредиентов", order.getIngredients().isEmpty());
        Assert.assertFalse("Заказ не может не содержать id", order.get_id().isBlank());
        Assert.assertFalse("Статус заказа не может быть пустым", order.getStatus().isBlank());
        Assert.assertNotEquals("Цена не может быть равна 0",0, order.getPrice());
        Assert.assertFalse("Имя в заказе не должно быть пустым", order.getName().isBlank());
        Assert.assertFalse("Дата создания не может быть пустой", order.getCreatedAt().isBlank());
        Assert.assertFalse("Дата обновления не может быть пустой", order.getUpdatedAt().isBlank());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов не выполняется")
    public void createOrderWithoutIngredients400ErrorMessage() {
        BasicResponse response = OrderSteps.createOrderWithoutIngredients(accessToken)
                .then()
                .statusCode(400)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "Ingredient ids must be provided",
                response.getMessage());
    }

    @Test
    @DisplayName("Создание заказа без авторизации не выполняется")
    public void createOrderWithoutAuth401ErrorMessage() {
        OrderRequest orderRequest = new OrderRequest(OrderUtils.getCorrectIngredients());
        BasicResponse response = OrderSteps.createOrderWithoutAuth(orderRequest)
                .then()
                .statusCode(401)
                .extract()
                .as(BasicResponse.class);

        Assert.assertFalse("Запрос не должен быть успешным", response.isSuccess());
        Assert.assertEquals("Неверный текст ошибки", "You should be authorised", response.getMessage());
    }

    @Test
    @DisplayName("Создание заказа с неверным хешом ингредиентов не выполняется")
    public void createOrderWithWrongIngredientsReturns500ErrorMessage() {
        OrderRequest orderRequest = new OrderRequest(OrderUtils.getIncorrectIngredients());
        OrderSteps.createOrderWithAuth(accessToken,orderRequest)
                .then()
                .statusCode(500);
    }
}
