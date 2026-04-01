package br.com.dogapi.tests;

import br.com.dogapi.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT07–CT13: Testes para o endpoint GET /breed/{breed}/images
 * Retorna todas as imagens de uma raça específica.
 */
@Feature("Breed Images")
@DisplayName("GET /breed/{breed}/images")
public class BreedImagesTest extends BaseTest {

    @Test
    @DisplayName("CT07 – Raça válida: status HTTP 200")
    @Description("Verifica que busca de imagens de raça válida retorna 200")
    @Severity(SeverityLevel.BLOCKER)
    void ct07_validBreedStatus200() {
        given()
                .spec(spec)
                .when()
                .get("/breed/labrador/images")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("CT08 – Raça válida: campo 'status' é 'success'")
    @Description("Verifica que o campo status é 'success' para raça válida")
    @Severity(SeverityLevel.CRITICAL)
    void ct08_validBreedStatusSuccess() {
        given()
                .spec(spec)
                .when()
                .get("/breed/labrador/images")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

    @Test
    @DisplayName("CT09 – Raça válida: campo 'message' é uma lista não vazia")
    @Description("Verifica que a lista de imagens não está vazia para raça válida")
    @Severity(SeverityLevel.CRITICAL)
    void ct09_validBreedMessageIsList() {
        List<String> images = given()
                .spec(spec)
                .when()
                .get("/breed/labrador/images")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("message");

        assertNotNull(images, "A lista de imagens não deveria ser null");
        assertFalse(images.isEmpty(), "A lista de imagens não deveria estar vazia");
    }

    @Test
    @DisplayName("CT10 – Raça válida: URLs das imagens terminam com extensão de imagem")
    @Description("Verifica que cada URL da lista tem extensão .jpg, .jpeg ou .png")
    @Severity(SeverityLevel.NORMAL)
    void ct10_imageUrlsHaveValidExtension() {
        List<String> images = given()
                .spec(spec)
                .when()
                .get("/breed/labrador/images")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("message");

        assertFalse(images.isEmpty(), "A lista de imagens não deveria estar vazia");
        // Verifica as primeiras 5 URLs
        images.stream()
                .limit(5)
                .forEach(url -> assertTrue(
                        url.matches(".*\\.(jpg|jpeg|png)$"),
                        "URL inválida (sem extensão de imagem): " + url));
    }

    @ParameterizedTest(name = "CT11 – Raça ''{0}'' retorna imagens com sucesso")
    @ValueSource(strings = {"husky", "poodle", "beagle", "boxer", "dalmatian"})
    @Description("Parametrizado: verifica status 200 e success para 5 raças diferentes")
    @Severity(SeverityLevel.NORMAL)
    void ct11_parametrizedValidBreeds(String breed) {
        given()
                .spec(spec)
                .when()
                .get("/breed/{breed}/images", breed)
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", not(empty()));
    }

    @Test
    @DisplayName("CT12 – Raça inválida: status HTTP 404")
    @Description("Verifica que uma raça inexistente retorna HTTP 404")
    @Severity(SeverityLevel.CRITICAL)
    void ct12_invalidBreedStatus404() {
        given()
                .spec(spec)
                .when()
                .get("/breed/racainexistente/images")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("CT13 – Raça inválida: campo 'status' é 'error'")
    @Description("Verifica que uma raça inexistente retorna status 'error'")
    @Severity(SeverityLevel.CRITICAL)
    void ct13_invalidBreedStatusError() {
        given()
                .spec(spec)
                .when()
                .get("/breed/racainexistente/images")
                .then()
                .statusCode(404)
                .body("status", equalTo("error"));
    }
}
