package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import static config.Constants.*;


public class FootballAPIConfig {

    public static RequestSpecification football_requestSpec;
    public static ResponseSpecification football_responseSpec;

    @BeforeClass
    public static void setup() {
        football_requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath("/v2/")
                .addHeader("X-Auth-Token", TOKEN)
                .addHeader("X-Response-Control", "minified")
                .addHeader("Content-Type", "application/json")
                .addFilter(new RequestLoggingFilter())  // -> adds logging to every test
                .addFilter(new ResponseLoggingFilter()) // -//-
                .build();

        football_responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = football_requestSpec;
        RestAssured.responseSpecification = football_responseSpec;
    }


}
