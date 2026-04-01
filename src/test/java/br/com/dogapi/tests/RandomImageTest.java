package br.com.dogapi.tests;

import br.com.dogapi.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CT14–CT18: Testes para o endpoint GET /breeds/image/random
 * Retorna uma URL de imagem aleatória de qualquer raça.
 */
@Feature("Random Image")
@DisplayName("GET /breeds/image/random")
public class RandomImageTest extends BaseTest {

    @Test
    @DisplayName("CT14 – Status HTTP deve ser 200")
    @Description("Verifica que o endpoint de imagem aleatória retorna HTTP 200")
    @Severity(SeverityLevel.BLOCKER)
    void ct14_statusCode200() {
        given()
                .spec(spec)
                .when()
                .get("/breeds/image/random")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("CT15 – Campo 'status' deve ser 'success'")
    @Description("Verifica que o campo status é 'success' na resposta de imagem aleatória")
    @Severity(SeverityLevel.CRITICAL)
    void ct15_statusFieldSuccess() {
        given()
                .spec(spec)
                .when()
                .get("/breeds/image/random")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

    @Test
    @DisplayName("CT16 – Campo 'message' deve ser uma URL válida")
    @Description("Verifica que a URL da imagem aleatória segue o padrão esperado")
    @Severity(SeverityLevel.CRITICAL)
    void ct16_messageIsValidUrl() {
        given()
                .spec(spec)
                .when()
                .get("/breeds/image/random")
                .then()
                .statusCode(200)
                .body("message", matchesPattern("https?://.+\\.(jpg|jpeg|png)"));
    }

    @Test
    @DisplayName("CT17 – URL da imagem deve ser do domínio dog.ceo")
    @Description("Verifica que a imagem retornada pertence ao domínio dog.ceo")
    @Severity(SeverityLevel.NORMAL)
    void ct17_urlFromDogCeoDomain() {
        Response response = given()
                .spec(spec)
                .when()
                .get("/breeds/image/random")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String imageUrl = response.jsonPath().getString("message");
        assertTrue(imageUrl.contains("dog.ceo") || imageUrl.contains("images.dog.ceo"),
                "A URL deveria pertencer ao domínio dog.ceo, mas foi: " + imageUrl);
    }

    @RepeatedTest(value = 3, name = "CT18 – Imagem aleatória retorna URL válida (repetição {currentRepetition}/{totalRepetitions})")
    @Description("Repete 3 vezes: verifica que cada chamada retorna uma URL distinta e válida")
    @Severity(SeverityLevel.NORMAL)
    void ct18_repeatedRandomImageAlwaysValid() {
        given()
                .spec(spec)
                .when()
                .get("/breeds/image/random")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", matchesPattern("https?://.+\\.(jpg|jpeg|png)"));
    }
}
