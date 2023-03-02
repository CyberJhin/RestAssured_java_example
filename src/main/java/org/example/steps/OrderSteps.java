package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.example.pojo.order.OrderRequest;

import static org.example.constants.OrderConstants.*;

public class OrderSteps {
    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_TEST_URL)
                    .setBasePath(BASE_ORDERS_URL)
                    .setContentType(ContentType.JSON)
                    .build();


    @Step("Создание нового заказа c ингредиентами и авторизацией")
    public static Response createOrderWithAuth(String accessToken, OrderRequest orderRequest) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .body(orderRequest)
                .when()
                .post();
    }

    @Step("Создание нового заказа без ингредиентов с авторизацией")
    public static Response createOrderWithoutIngredients(String accessToken) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .body(new OrderRequest())
                .when()
                .post();
    }

    @Step("Создание нового заказа без авторизации")
    public static Response createOrderWithoutAuth(OrderRequest orderRequest) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .body(orderRequest)
                .when()
                .post();
    }

    @Step("Получение заказов конкретного пользователя с авторизацией")
    public static Response getUsersOrdersWithAuth(String accessToken) {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .get();
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public static Response getUsersOrdersWithoutAuth() {
        return RestAssured.given()
                .spec(REQUEST_SPECIFICATION)
                .when()
                .get();
    }
}
