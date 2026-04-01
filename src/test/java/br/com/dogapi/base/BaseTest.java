package br.com.dogapi.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

/**
 * Configuração base compartilhada por todos os testes.
 * Define baseURI, content-type esperado e filtros de log.
 */
public abstract class BaseTest {

    protected static RequestSpecification spec;

    @BeforeAll
    static void setup() {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://dog.ceo/api")
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
