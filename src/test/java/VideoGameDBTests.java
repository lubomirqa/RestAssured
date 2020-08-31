import config.VideoGameConfig;
import config.VideoGameEndpoints;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class VideoGameDBTests extends VideoGameConfig {

    @Test
    public void getAllGames() {
        given().
        when().get(VideoGameEndpoints.ALL_VIDEO_GAMES);
    }

    @Test
    public void createNewGameByJson() {

        String gameBodyJson = "{\n" +
                "  \"id\": 11,\n" +
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

}
