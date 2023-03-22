import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class FakeRestTest {

    private String token;
    private static Utils utils;
    @BeforeAll
    public static void setUp() {
        utils = new Utils();
        RestAssured.baseURI = "https://serverest.dev/";

    }

    @BeforeEach
    public void getToken() {
        String payLoad = "{\n" +
                "  \"email\": \"fulano@qa.com\",\n" +
                "  \"password\": \"teste\"\n" +
                "}";
        this.token = given().contentType(ContentType.JSON).body(payLoad).when().post("login").then()
        .extract().path("authorization");

    }

    @Test
    public void haveToDeleteUserTest() {
        String id = utils.inserirUsuario("fulano", "fulanoamilton@email.com"
                , "password", "true");
        //Execução do teste
       given().contentType(ContentType.JSON).header("Authorization", token).when().delete("usuarios/" + id)
               .then().statusCode(200).body("message", is("Registro excluído com sucesso"));
        }
    @Test
    public void userUpdateTest() {
        Map <String,Object> payLoad = new HashMap<String,Object>();
        payLoad.put("nome", "fulano");
        payLoad.put("email", "fulano213@email.com");
        payLoad.put("password", "fulano123");
        payLoad.put("administrador", "true");
        String id = given().contentType(ContentType.JSON).header("Authorization", token)
                .body(payLoad).when().post("usuarios").then().extract().path("_id");
        System.out.println(id);
        payLoad.put("nome", "beltrano");
        given().contentType(ContentType.JSON).header("Authorization", token).body(payLoad).when().put("usuarios/" + id)
                .then().statusCode(200).body("message", is("Registro alterado com sucesso"));
        given().contentType(ContentType.JSON).header("Authorization", token).when().get("usuarios/" + id)
                .then().statusCode(200).body("nome", is("beltrano"));
        utils.deletarUsuario("id");
    }


}
