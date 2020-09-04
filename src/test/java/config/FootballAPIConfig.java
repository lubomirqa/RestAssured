package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

import static config.VideoGameConfig.videoGame_requestSpec;

public class FootballAPIConfig {

    public static RequestSpecification football_requestSpec;
    public static ResponseSpecification football_responseSpec;

    @BeforeClass
    public static void setup() {
        football_requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://api.football-data.org")
                .setBasePath("/v2/")
                .addHeader("X-Auth-Token", "a6791a721dd04f24aa217163e8acb2fa")
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
