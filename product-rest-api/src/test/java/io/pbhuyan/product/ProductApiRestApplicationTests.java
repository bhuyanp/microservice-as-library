package io.pbhuyan.product;

import io.pbhuyan.product.entity.Product;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:insert_products.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:delete_products.sql")
class ProductApiRestApplicationTests {

    @Container
    //@ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("productdb");


    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.product.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.product.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.product.password", mySQLContainer::getPassword);
    }


    @LocalServerPort
    int serverPort;


    @BeforeAll
    static void setUp() {
        assertTrue(mySQLContainer.isCreated());
        assertTrue(mySQLContainer.isRunning());
    }


    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = serverPort;

    }

    @AfterEach
    void afterEachEach() {
    }

    @Test
    void shouldCreateProduct() {
        String request = """
                {
                    "title":"Product A",
                    "description":"Product A Description",
                    "price":100.02
                }
                """;

        post(request)
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("title", Matchers.equalTo("Product A"))
                .body("description", Matchers.equalTo("Product A Description"));
    }

    private static Response post(String request) {
        return RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/rest/products");
    }

    @Test
    void whenPriceMissing_shouldReturn400() {
        String request = """
                {
                    "title":"Product A",
                    "description":"Product A Description"
                }
                """;
        post(request)
                .then()
                .statusCode(400);
    }


    @Test
    void whenPriceNegative_shouldReturn400() {
        String request = """
                {
                    "title":"Product A",
                    "description":"Product A Description",
                    "price":-100.02
                }
                """;
        post(request)
                .then()
                .statusCode(400);
    }

    @Test
    void whenTitleMissing_shouldReturn400() {
        String request = """
                {
                    "description":"Product A Description",
                    "price":100.02
                }
                """;
        post(request)
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnAllProducts() {
        Product[] products = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/rest/products")
                .then()
                .statusCode(200)
                .extract()
                .as(Product[].class);
        Arrays.stream(products).forEach(System.out::println);
        assertThat(products).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldReturnProductById() {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/rest/products/" + PRODUCT.getId())
                .then()
                .statusCode(200)
                .body("id", Matchers.is(PRODUCT.getId()))
                .body("title", Matchers.equalTo(PRODUCT.getTitle()))
                .body("description", Matchers.equalTo(PRODUCT.getDescription()));
    }

    @Test
    void shouldNotReturnMissingProduct() {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/rest/products/" + "PRODUCT.getId()")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteProductById() {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/rest/products/" + PRODUCT.getId())
                .then()
                .statusCode(200);
    }

    static final Product PRODUCT = new Product(
            "96b7e8b4-3405-49b7-85a2-56b01ea1a610",
            "Product Test 1",
            "Integration test subject 1",
            BigDecimal.valueOf(102.02));

    @AfterAll
    static void tearDown() {
    }

}
