import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SelenoidTests {
    // Selenoid alive or dead test
    // Make request to https://selenoid.autotests.cloud/status
    // Check status code 200 (response ok)
    // Check total:5
    /*
    {
        state: {},
        origin: "http://136.243.89.21:4445/",
                browsers: {},
        sessions: { },
        version: "1.10.3[2021-02-23_09:48:39AM]",
                errors: [ ]
    }
    */

    @Test
    void successStatusTest(){
    given().when()
            .get("https://selenoid.autotests.cloud/status")
            .then()
            .statusCode(200);
    }

    @Test
    void successStatusWithoutGivenWhenTest(){
            get("https://selenoid.autotests.cloud/status")
                    .then()
                    .statusCode(200);
    }

    @Test
    void successStatusWithResponseTest(){
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test
    void successStatusCheckTotalFieldKolhozTest(){
        Response response = get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .extract().response();
        assertTrue(response.asString().contains("\"total\":5"));
    }

    @Test
    void successStatusCheckTotalFieldWithBodyTest(){
                get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("state.total", is(5));
    }

    @Test
    void successStatusCheckTotalFieldWithPathTest(){
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .extract()
                .path("state.total");
        assertEquals(5, response);
    }

    @Test
    void successStatusCheckTotalFieldWithAssertJTest(){
        int response = get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .extract()
                .path("state.total");
        assertThat(response).isEqualTo(5);
    }

}
