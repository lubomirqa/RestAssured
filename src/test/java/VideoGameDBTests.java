import config.VideoGameConfig;
import config.VideoGameEndpoints;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class VideoGameDBTests extends VideoGameConfig {

    @Test
    public void getAllGames() {
        given().
        when().get(VideoGameEndpoints.ALL_VIDEO_GAMES);
    }

    @Test
    public void createNewGameByJson() {

        String gameBodyJson = "{\n" +
                "  \"id\": 13,\n" +
                "  \"name\": \"MyNewGame\",\n" +
                "  \"reviewScore\": 88,\n" +
                "  \"category\": \"Shooter\",\n" +
                "  \"rating\": \"Mature\"\n" +
                "}";

        given()
                .body(gameBodyJson).
        when()
                .post(VideoGameEndpoints.ALL_VIDEO_GAMES).
        then();
    }

    @Test
    public void updateGame() {

        String gameBodyJson = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"MyNewGame\",\n" +
                "  \"releaseDate\": \"2020-08-31T13:25:27.866Z\",\n" +
                "  \"reviewScore\": 80,\n" +
                "  \"category\": \"Driving\",\n" +
                "  \"rating\": \"string\"\n" +
                "}";

        given().
                body(gameBodyJson).
        when().
                put("videogames/1").
        then();
    }

    @Test
    public void deleteGame() {
        given().
        when()
                .delete("videogames/1").
        then();
    }

    @Test
    public void getSingleGame() {
        given()
                .pathParam("videoGameId", 5).
        when()
                .get(VideoGameEndpoints.SINGLE_VIDEO_GAME).
        then();
    }

    @Test
    public void testVideoGameSerializationByJSON() {
        VideoGame videoGame = new VideoGame("99", "2018-04-04", "Newcastle", "Mature", "15", "Shooter");

        given()
                .body(videoGame).
        when()
                .post(VideoGameEndpoints.ALL_VIDEO_GAMES).
        then();
        System.out.println(videoGame.toString());
    }

    @Test
    public void testVideoGameSchemaXML() {
       given().
               pathParam("videoGameId", 5).
               header("Content-Type", "application/xml").
               header("Accept", "application/xml").
        when().
                get(VideoGameEndpoints.SINGLE_VIDEO_GAME).
        then().
               body(matchesXsdInClasspath("VideoGameXSD.xsd"));
    }

    @Test
    public void testVideoGameSchemaJSON() {
        given().
                pathParam("videoGameId", 6).

        when().
                get(VideoGameEndpoints.SINGLE_VIDEO_GAME).
        then().
                body(matchesJsonSchemaInClasspath("VideoGameJsonSchema.json"));
    }

    @Test
    public void convertJSONToPogo() {
        Response response =
        given()
                .pathParam("videoGameId", 5).
        when().
        get(VideoGameEndpoints.SINGLE_VIDEO_GAME);

        VideoGame videoGame = response.getBody().as(VideoGame.class);

        System.out.println(videoGame.toString());
    }

    @Test
    public void captureResponseTime() {
        long responseTime = get(VideoGameEndpoints.ALL_VIDEO_GAMES).time();
        System.out.println("Response time in MS: " + responseTime);
    }

    @Test
    public void assertOnResponseTime() {
        when().
                get(VideoGameEndpoints.ALL_VIDEO_GAMES).
        then().
                time(lessThan(1000L));
    }

    @Test
    public void assertOnSingleRequestResponseTime() {
        when().
                get(VideoGameEndpoints.SINGLE_VIDEO_GAME, 5).
        then().
                time(lessThan(500L));

    }

    @Test
    public void assertNameWithId() {
        when().
                get("videogames").
        then().
                body("findAll { it.id == 3 }.name", hasItems("Tetris"));
    }

    @Test
    public void responseAsString() {
        String response = get("videogames").asString();

        List<String> names = from(response).getList("findAll { it.id > 3 }.name");

        names.forEach(System.out::println);
    }

    @Test
    public void checkIfEqualToValue() {
        get("videogames").
        then().
                body("find { it.id == 3 }.name", equalTo("Tetris"));
    }

    @Test
    public void checkIfAllHasItems() {
        get("videogames").
        then().
                body("rating", hasItems("Universal", "PG-13", "Mature"));
    }

    @Test
    public void xsdSchemaValidation() {
        given()
                //.header("Content-Type", "application/xml").
                .header("Accept", "application/xml").
        when().
        get("videogames").
        then()
              .assertThat()
              .body(matchesXsdInClasspath("videogames.xsd"));

    }
}
