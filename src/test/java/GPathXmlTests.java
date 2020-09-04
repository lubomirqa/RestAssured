import config.FootballAPIConfig;
import config.VideoGameConfig;
import config.VideoGameEndpoints;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.response.Response;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class GPathXmlTests extends VideoGameConfig {

    @Test
    public void getFirstGameList() {
        Response response = get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        String name = response.path("videoGames.videoGame.name[0]");
        System.out.println(name);
    }

    @Test
    public void getAttribute() {

        Response response = get(VideoGameEndpoints.ALL_VIDEO_GAMES);

        String category = response.path("videoGames.videoGame[0].@category");

        System.out.println(category);
    }

    @Test
    public void getListOfXmlNodes() {

        String responseAsString = get(VideoGameEndpoints.ALL_VIDEO_GAMES).asString();

        List<Node> allResults = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { element -> return element }"
        );

        System.out.println(allResults.get(2).get("name").toString());
    }

    @Test
    public void getListOfXmlNodesByFindAllOnAttribute() {

        String responseAsString = get(VideoGameEndpoints.ALL_VIDEO_GAMES).asString();

        List<Node> allDrivingGames = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { videoGame -> def category = videoGame.@category; category == 'Driving' }"
        );

        extractString(allDrivingGames).forEach(System.out::println);

        System.out.println("First driving game: " + allDrivingGames.get(0).get("name").toString());

    }

    @Test
    public void getSingleNode() {
        String responseAsString = get(VideoGameEndpoints.ALL_VIDEO_GAMES).asString();

        Node videoGame = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.find { videoGame -> def name = videoGame.name; name == 'Tetris' }"
        );

        String videoGameName = videoGame.get("name").toString();

        System.out.println(videoGameName);
    }

    @Test
    public void getSingleElementDepthFirst() {
        String responseAsString = get(VideoGameEndpoints.ALL_VIDEO_GAMES).asString();

        int reviewScore = XmlPath.from(responseAsString).getInt(
                "**.find { it.name == 'Gran Turismo 3' }.reviewScore"
        );

        System.out.println(reviewScore);
    }

    @Test
    public void getAllNodesBasedOnACondition() {

        String responseAsString = get(VideoGameEndpoints.ALL_VIDEO_GAMES).asString();

        int reviewScore = 90;

        List<Node> allVideoGameOverCertainScore = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { it.reviewScore.toFloat() >= " + reviewScore + "}"
        );

        extractString(allVideoGameOverCertainScore).forEach(System.out::println);

    }


    public ArrayList<String> extractString(List<Node> nodes){
        ArrayList<String> words = new ArrayList<>();
        for(Node node: nodes){
            String word = node.toString();
            String[] part = word.split("(?<=\\D)(?=\\d)");
            words.add(part[0]);
        }
        return words;
    }

}
