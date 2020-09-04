import config.FootballAPIConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GPathJSONTests extends FootballAPIConfig {

    @Test
    public void extractMapOfElementsWithFind() {

        Response response = get("competitions/2021/teams");

        Map<String, ?> allTeamDataForSingleTeam = response.
                path("teams.find{ it.name == 'Manchester United FC' }");

        System.out.println("Map of Team Data = " + allTeamDataForSingleTeam);
    }

    @Test
    public void extractSingleValueWithFind() {
        Response response = get("teams/57");
        String certainPlayer = response.path("squad.find{ it.nationality == 'England' }.name");

        System.out.println(certainPlayer);
    }

    @Test
    public void extractValuesWithFindAll() {
        Response response = get("teams/57");
        List<String> playerNames = response.path("squad.findAll { it.nationality == 'England' }.name");

        playerNames.forEach(System.out::println);
    }

    @Test
    public void extractSingleValueWithHighestNumber() {
        Response response = get("teams/57");
        String playerName = response.path("squad.max { it.id }.name");
        System.out.println("Highest id number = " + playerName);
    }

    @Test
    public void extractMultipleValuesAndSumThem() {
        Response response = get("teams/57");
        int sumOfIds = response.path("squad.collect { it.id }.sum()");
        System.out.println("Sum of all ids is " + sumOfIds);
    }

    @Test
    public void extractMapOfObjectWithFindAndFindAll() {
        Response response = get("teams/57");
        Map<String, ?> playerOfCertainPosition = response.path(
                "squad.findAll { it.position == 'Defender' }.find { it.nationality == 'Greece' }"
        );

        System.out.println("Details of the players " + playerOfCertainPosition);
    }

    @Test
    public void extractMapOfObjectWithFindAllWithParameters() {
        Response response = get("teams/57");
        String position = "Defender";
        String nationality = "Greece";

        Map<String, ?> playerOfCertainPosition = response.path(
                "squad.findAll { it.position == '%s' }.find { it.nationality == '%s' }", position, nationality
        );

        System.out.println("Details of players" + playerOfCertainPosition);

    }

    @Test
    public void extractMultiplePlayers() {

        String position = "Midfielder";
        String nationality = "England";
        Response response = get("teams/57");

        ArrayList<Map<String, ?>> allPlayersCertainNation = response.path(
                "squad.findAll { it.position == '%s' }.findAll { it.nationality == '%s' }"
                , position, nationality);

        allPlayersCertainNation.forEach(System.out::println);
    }
}
