package io.pbhuyan.product;

import io.pbhuyan.product.dto.ProductResponseDto;
import io.pbhuyan.product.entity.Product;
import io.pbhuyan.security.common.dto.AuthenticationResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;

import static io.pbhuyan.product.controller.ProductRestController.PRODUCT_SERVICE_BASE_URI;
import static io.pbhuyan.security.common.controller.AuthenticationController.AUTH_API_BASE_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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
    static void mysqlProperties(DynamicPropertyRegistry registry) {
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

    private Response post(String request) {
        return RestAssured
                .given()
                .headers("Authorization", "Bearer " + getAdminToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(PRODUCT_SERVICE_BASE_URI);
    }

    private Response get(String url) {
        log.info("Get URL: {}", url);
        return RestAssured
                .given()
                .headers("Authorization", "Bearer " + getAdminToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(url);
    }

    private String getAdminToken() {
        ResponseBody<?> body = RestAssured.given()
                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("""
                        {
                            "username":"admin",
                            "password":"admin1"
                        }
                        """)
                .post(AUTH_API_BASE_URI + "/login")
                .body();
        log.info("body: {}", body.prettyPrint());
        String token = body.as(AuthenticationResponse.class).token();
        log.info("Token: {}", token);
        return token;
    }

    @AfterEach
    void afterEachEach() {
    }

    @Test
    void shouldCreateProduct() {
        //given
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
        ProductResponseDto[] productResponseDtos = get(PRODUCT_SERVICE_BASE_URI)
                .then()
                .statusCode(200)
                .extract()
                .as(ProductResponseDto[].class);
        Arrays.stream(productResponseDtos).forEach(System.out::println);
        assertThat(productResponseDtos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldReturnProductById() {
        get(PRODUCT_SERVICE_BASE_URI + "/" + PRODUCT.getId())
                .then()
                .statusCode(200)
                .body("id", Matchers.is(PRODUCT.getId()))
                .body("title", Matchers.equalTo(PRODUCT.getTitle()))
                .body("description", Matchers.equalTo(PRODUCT.getDescription()));
    }

    @Test
    void shouldNotReturnMissingProduct() {
        get(PRODUCT_SERVICE_BASE_URI + "PRODUCT.getId()")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteProductById() {
        RestAssured
                .given()
                .headers("Authorization", "Bearer " + getAdminToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(PRODUCT_SERVICE_BASE_URI + "/" + PRODUCT.getId())
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
