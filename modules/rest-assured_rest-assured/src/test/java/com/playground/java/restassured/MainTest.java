package com.playground.java.restassured;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.Runner;
import com.playground.java.restassured.Person;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

class MainTest {

    private final HttpServer server = Moco.httpServer(12306, Moco.fileRoot("src/test/resources"));
    private final Runner runner = Runner.runner(server);

    @BeforeEach
    void resetServerResponses() {
        runner.start();
        baseURI = String.format("http://localhost:%s/", server.port());
    }

    @AfterEach
    void tearDownSuite() {
        runner.stop();
    }

    @DisplayName("It can check response information using Hamcrest")
    @Test
    void itCanCheckResponseInformationUsingHamcrest() {
        server.response(Moco.file("stub-get.json"));

        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(equalTo(200))
                .contentType(equalTo("application/json"))
                .header("Content-Length", not(isEmptyOrNullString()));
    }

    @DisplayName("It can receive XML and inspect it using XPath")
    @Test
    void itCanReceiveXMLAndNavigateThroughItUsingXPath() {
        server.response("<?xml version='1.0' encoding='us-ascii'?><slideshow><slide type=\"all\"><title>Wake up to " +
                "WonderWidgets!</title></slide></slideshow>");

        given()
                .accept(ContentType.XML)
                .when()
                .get()
                .then()
                .body(hasXPath("/slideshow/slide[@type='all']"))
                .body(hasXPath("/slideshow/slide/title/text()", equalTo("Wake up to WonderWidgets!")));
    }

    @DisplayName("It can receive JSON and inspect it using dots")
    @Test
    void itCanReceiveJSONAndNavigateThroughItUsingDots() {
        server.response(Moco.file("stub-get.json"));

        given()
                .accept(ContentType.JSON)
                .when()
                .then()
                .body("url", equalTo(baseURI + "get"))
                .body("headers.Host", equalTo(baseURI));
    }

    @DisplayName("It can validate a JSON structure based on a schema")
    @Test
    void itCanValidateJSONStructureUsingJSONSchema() {
        server.response(Moco.file("stub-headers.json"));

        given()
                .accept(ContentType.JSON)
                .when()
                .then()
                .body(matchesJsonSchemaInClasspath("slash-headers-schema.json"));
    }

    @DisplayName("It supports basic authentication")
    @Test
    void itCanAuthenticateViaBasicAuth() {
        server.response(
                Moco.status(200),
                Moco.header("Content-Type", "application/json"),
                Moco.with(Moco.json("{\"authenticated\": true,\"user\": \"username\"}"))
        );

        given()
                .auth().basic("username", "password")
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .body("user", equalTo("username"));
    }

    @DisplayName("It can serialize data in JSON using different libraries")
    @Test
    void itCanSerializeDataInJSON() {
        server.response(
                Moco.header("Content-Type", "application/json"),
                Moco.with(Moco.json("{\"person\":{\"name\":\"John\",\"age\":35,\"livingInCanada\":true}}"))
        );

        final Person john = new Person("John", 35, true);

        given()
                .accept(ContentType.JSON)
                .body(john, ObjectMapperType.JACKSON_2)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("person.name", equalTo(john.getName()))
                .body("person.age", equalTo(john.getAge()))
                .body("person.livingInCanada", equalTo(john.isLivingInCanada()));
    }

    @DisplayName("It is possible to specify query parameters")
    @Test
    void itCanSendQueryParameters() {
        server.response(
                Moco.header("Content-Type", "application/json"),
                Moco.with(Moco.json("{\"args\":{\"name\":\"John\",\"age\":\"35\"}}"))
        );

        given()
                .queryParam("name", "John")
                .queryParam("age", 35)
                .when()
                .get()
                .then()
                .body("args.name", equalTo("John"))
                .body("args.age", equalTo("35"));
    }
}
