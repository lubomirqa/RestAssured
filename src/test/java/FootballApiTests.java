import com.fasterxml.jackson.annotation.JsonIgnoreType;
import config.FootballAPIConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class FootballApiTests extends FootballAPIConfig {

    @Test
    public void getDetailsOfOneArea() {
        given()
                .queryParam("areas", 2072).
        when()
                .get("areas");
    }

    @Test
    public void getDateFounded() {
        given().
        when()
                .get("teams/57").
        then().
                body("founded", equalTo(1886));
    }

    @Test
    public void getFirstTeamName() {
        given().
                when()
                .get("competitions/2021/teams").
                then()
                .body("teams.name[0]", equalTo("Arsenal FC"));
    }

    @Test
    public void getAllTeamData() {
        String responseBody = get("teams/57").asString();
        System.out.println(responseBody);
    }

    @Test
    public void getAllTeamData_DoCheckFirst() {
        Response response =
                given().
                when()
                        .get("teams/57").
                then()
                        .contentType(ContentType.JSON)
                        .extract().response();

        String jsonResponseAsString = response.asString();
        //System.out.println(jsonResponseAsString);
    }

    @Test
    public void extractHeaders() {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Server", "nginx/1.6.2");
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("Transfer-Encoding", "chunked");

        Response response =
                given().
                when()
                    .get("teams/57").
                then()
                    .contentType(ContentType.JSON)
                    .headers(headerMap)
                    .extract().response();

        Headers headers = response.getHeaders();
        String contentType = response.getHeader("Content-Type");
        System.out.println(contentType);
    }

    @Test
    public void extractFirstTeamName() {
        String firstTeamName = get("competitions/2021/teams").jsonPath().getString("teams.name[0]");
        System.out.println(firstTeamName);
    }

    @Test
    public void extractAllTeamNames() {
        Response response =
                given().
                when()
                        .get("competitions/2021/teams").
                then().extract().response();

        List<String> teamNames = response.path("teams.name");

        for(String teamName: teamNames){
            System.out.print(teamName + " ");
        }
        System.out.println("");
    }

    @Test
    public void extractAllClubColors() {
        Response response =
                given().
                when()
                        .get("competitions/2021/teams").
                then().extract().response();

        List<String> clubColors = response.path("teams.clubColors");

        clubColors.forEach(System.out::println);
    }

    @Test
    public void extractLeagues() {
        Response response =
                given().
                when()
                        .get("competitions").
                then().extract().response();

        List<String> leagueNames = response.path("competitions.name");
        leagueNames.forEach(System.out::println);
    }

    @Test
    public void jsonSchemaValidation() {
        given()
                .pathParam("id", 57).
        when()
                .get("competitions/2021/teams").
        then()
              .assertThat()
              .body(matchesJsonSchemaInClasspath("competitions_schema.json"));

    }

    @Test
    public void getSpecificTeamById() {
        get("competitions/2021/teams").
        then()
                .body("teams.find { it.id == 57 }.name", equalTo("Arsenal FC"));
    }

    @Test
    public void allTeamsFoundedBeforeYear() {
        Response response =
        get("competitions/2021/teams").
        then().extract().response();

        List<String> names = response.path(
                "teams.findAll { it.founded < 1900 }.findAll { it.founded > 1880 }.name");

        names.forEach(System.out::println);
    }

    @Test
    public void extractSpecificPlayersWithValues() {
        int year = 1990;
        String color = "Red";

        Response response =
        get("competitions/2021/teams").
        then().extract().response();

        List<String> teams = response.path("teams.findAll { it.founded < " + year + " }.findAll { it.clubColors.contains('%s') }.name", color);

        teams.forEach(System.out::println);
    }

}
