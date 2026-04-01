package br.com.dogapi.tests;

import br.com.dogapi.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT01–CT06: Testes para o endpoint GET /breeds/list/all
 * Retorna JSON com todas as raças e sub-raças disponíveis.
 */
@Feature("Breeds List All")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GET /breeds/list/all")
public class BreedsListAllTest extends BaseTest {

    private static Response response;

    @BeforeAll
    void fetchBreeds() {
        response = given()
                .spec(spec)
                .when()
                .get("/breeds/list/all")
                .then()
                .extract()
                .response();
    }

    @Test
    @DisplayName("CT01 – Status HTTP deve ser 200")
    @Description("Verifica que o endpoint retorna HTTP 200 OK")
    @Severity(SeverityLevel.BLOCKER)
    void ct01_statusCode200() {
        assertEquals(200, response.statusCode(),
                "Status code esperado: 200");
    }

    @Test
    @DisplayName("CT02 – Campo 'status' deve ser 'success'")
    @Description("Verifica que o campo status da resposta é 'success'")
    @Severity(SeverityLevel.CRITICAL)
    void ct02_statusFieldSuccess() {
        assertEquals("success", response.jsonPath().getString("status"),
                "O campo status deveria ser 'success'");
    }

    @Test
    @DisplayName("CT03 – Content-Type deve ser application/json")
    @Description("Verifica que a resposta retorna Content-Type JSON")
    @Severity(SeverityLevel.NORMAL)
    void ct03_contentTypeJson() {
        assertTrue(response.contentType().contains("application/json"),
                "Content-Type deveria conter 'application/json', mas foi: " + response.contentType());
    }

    @Test
    @DisplayName("CT04 – Campo 'message' não deve ser vazio")
    @Description("Verifica que o mapa de raças não está vazio")
    @Severity(SeverityLevel.CRITICAL)
    void ct04_messageNotEmpty() {
        Map<String, ?> message = response.jsonPath().getMap("message");
        assertNotNull(message, "O campo 'message' não deveria ser null");
        assertFalse(message.isEmpty(), "O mapa de raças não deveria estar vazio");
    }

    @Test
    @DisplayName("CT05 – Raças conhecidas devem estar presentes")
    @Description("Verifica que raças como 'labrador', 'husky' e 'poodle' existem na lista")
    @Severity(SeverityLevel.NORMAL)
    void ct05_knownBreedsPresent() {
        given()
                .spec(spec)
                .when()
                .get("/breeds/list/all")
                .then()
                .statusCode(200)
                .body("message", hasKey("labrador"))
                .body("message", hasKey("husky"))
                .body("message", hasKey("poodle"));
    }

    @Test
    @DisplayName("CT06 – Lista deve conter mais de 50 raças")
    @Description("Verifica que a API retorna ao menos 50 raças distintas")
    @Severity(SeverityLevel.NORMAL)
    void ct06_moreThan50Breeds() {
        Map<String, ?> message = response.jsonPath().getMap("message");
        assertTrue(message.size() > 50,
                "Esperado mais de 50 raças, mas foram encontradas: " + message.size());
    }
}
