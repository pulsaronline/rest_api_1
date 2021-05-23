package homework;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;

public class RegresInTests {
    @BeforeAll
    static void setup(){
        RestAssured.baseURI = "https://reqres.in";
    }

    // single user list: successful (200)
    @Test
    void successfulUsersList(){
        given()
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .body("page", is(2))
                .body("per_page", is(6))
                .body("total", is(12))
                .body("total_pages", is(2))
                .body("data", not(nullValue()))
                .body("data[0].id", is(7))                              // check first element of the list
                .body("data[0].email", is("michael.lawson@reqres.in"))
                .body("data[0].first_name", is("Michael"))
                .body("data[0].last_name", is("Lawson"))
                .body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    // single user: successful (200)
    @Test
    void successfulSingleUser(){
        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.avatar", is("https://reqres.in/img/faces/2-image.jpg"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    // single user: not found (404)
    @Test
    void unSuccessfulSingleUser(){
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body("data", is(nullValue()));
    }

    // list <resource>: successful (200)
    @Test
    void successfulResourceList(){
        given()
                .when()
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .body("page", is(1))
                .body("per_page", is(6))
                .body("total", is(12))
                .body("total_pages", is(2))
                .body("data", not(nullValue()))
                .body("data[0].id", is(1))                              // check first element of the list
                .body("data[0].name", is("cerulean"))
                .body("data[0].year", is(2000))
                .body("data[0].color", is("#98B2D1"))
                .body("data[0].pantone_value", is("15-4020"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    // single <resource>: successful (200)
    @Test
    void successfulResourceSingle(){
        given()
                .when()
                .get("/api/unknown/2")
                .then()
                .statusCode(200)
                .body("data", not(nullValue()))
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"))
                .body("data.year", is(2001))
                .body("data.color", is("#C74375"))
                .body("data.pantone_value", is("17-2031"))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    // single <resource>: not found (404)
    @Test
    void unSuccessfulResourceSingle(){
        given()
                .when()
                .get("/api/unknown/23")
                .then()
                .statusCode(404)
                .body("data", is(nullValue()));
    }

    // single user create: successful (201)
    @Test
    public void successfulUserCreate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        given().contentType(JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id", not(nullValue()))
                .body("createdAt", containsString(currentDate));
    }

    // single user update: successful (201)
    @Test
    public void successfulUserUpdate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        given().contentType(JSON)
                .body("{ \"name\": \"neo\", \"job\": \"the chosen one\" }")
                .when()
                .post("/api/users/2")
                .then()
                .statusCode(201)
                .body("name", is("neo"))
                .body("job", is("the chosen one"))
                .body("createdAt", containsString(currentDate));
    }

    // single user delete: successful (204)
    @Test
    void successfulUserDelete() {
        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    // user register: successful (200)
    @Test
    void successfulUserRegister() {
        given().contentType(JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }")
                .when()
                .post("api/register")
                .then()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    // user login: successful (200)
    @Test
    void successfulUserLogin(){
        given().contentType(JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    // user login: unsuccessful (400)
    @Test
    void unSuccessfulUserLogin(){
        given().contentType(JSON)
                .body("{ \"email\": \"peter@klaven\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
