# RestAssured Java Example

## О проекте

**RestAssured Java Example** — учебный проект, предназначенный для демонстрации навыков автоматизации тестирования REST API с помощью Java и библиотеки RestAssured. Структура проекта соответствует стандартам индустрии (каталоги `src/main` и `src/test`).

## Назначение

- Пример написания автоматизированных тестов для REST API на Java.
- Демонстрация основных возможностей и best practices работы с RestAssured.
- Быстрый старт для изучения подходов к тестированию API.

## Технологии и зависимости

- Java
- RestAssured
- JUnit 
- Maven

## Структура проекта

```
.
├── src/
│   ├── main/         # Основной код приложения (если есть)
│   └── test/
│       └── java/     # Тестовые классы с использованием RestAssured
└── build.gradle или pom.xml
```

## Как запустить тесты

1. Клонируйте репозиторий:
    ```sh
    git clone https://github.com/CyberJhin/RestAssured_java_example.git
    cd RestAssured_java_example
    ```
2. Запустите тесты (пример для Gradle):
    ```sh
    ./gradlew test
    ```
   Или для Maven:
    ```sh
    mvn test
    ```

## Примеры тестов

В каталоге `src/test/java` находятся примеры тестов RestAssured для различных сценариев работы с REST API.

## Контакты

Автор: [CyberJhin](https://github.com/CyberJhin)
