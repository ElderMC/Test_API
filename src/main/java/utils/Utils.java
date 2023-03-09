package utils;

import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class Utils {
    public String getToken() {
        String payLoad = "{\n" +
                "  \"email\": \"fulano@qa.com\",\n" +
                "  \"password\": \"teste\"\n" +
                "}";
        return given().contentType(ContentType.JSON).body(payLoad).when().post("login").then()
                .extract().path("authorization");
    }

        public String inserirUsuario (String nome, String email, String password, String administrador) {
            //Nas linhas abaixo está sendo criado o payload
            //Map é um tipo de dado do Java baseado em chave e valor
            Map<String, Object> payLoad = new HashMap<String, Object>();
            payLoad.put("nome", nome);
            payLoad.put("email", email);
            payLoad.put("password", password);
            payLoad.put("administrador", administrador);
            return given().contentType(ContentType.JSON).header("Authorization", this.getToken())
                    .body(payLoad).when().post("usuarios").then().extract().path("_id");
        }

        public void deletarUsuario (String id) {
            given().contentType(ContentType.JSON).header("Authorization", getToken()).when()
                    .delete("usuarios/" + id);
        }
    }

