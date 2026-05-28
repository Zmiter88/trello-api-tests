package helper;

import io.restassured.RestAssured;
import service.BaseService;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class BoardCleanupService extends BaseService {

    public void deleteAllBoards() {

        List<String> boardIds =

   getRequestSpecification()
                .when()
                .get("/member/me/boards")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");

        for (String boardId : boardIds) {
           getRequestSpecification()
                    .pathParam("id", boardId)
                    .when()
                    .delete("/boards/{id}")
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
    }

    public List<String> getAllBoards() {
        return getRequestSpecification()
                .when()
                .get("/member/me/boards")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id");
    }
}
