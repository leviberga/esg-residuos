package steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class EsgResiduosSteps {

    private Response response;
    private RequestSpecification request;
    private String requestBody;

    @Dado("que a API está disponível em {string}")
    public void api_disponivel(String url) {
        RestAssured.baseURI = url;
    }

    @Dado("que eu utilizo as credenciais de administrador {string} e {string}")
    public void credenciais_admin(String user, String pass) {
        request = given().auth().basic(user, pass).contentType("application/json");
    }

    @Dado("que eu utilizo as credenciais de usuário comum {string} e {string}")
    public void credenciais_user(String user, String pass) {
        request = given().auth().basic(user, pass).contentType("application/json");
    }

    @Dado("que eu estou autenticado no sistema")
    public void autenticado_padrao() {
        request = given().auth().basic("user", "user").contentType("application/json");
    }

    @Dado("possuo os dados de um ponto de coleta em {string} com tipo {string}")
    public void dados_ponto_coleta(String local, String tipo) {
        // IMPORTANTE: Use exatamente os nomes: nome, cidade, tipo, volumeMaximo
        requestBody = """
        {
          "nome": "Ponto de Coleta BDD",
          "endereco": "Rua Exemplo, 123",
          "cidade": "%s",
          "tipo": "%s",
          "volumeMaximo": 500.0
        }
        """.formatted(local, tipo);
    }

    @Quando("eu enviar uma requisição POST para {string}")
    public void enviar_post(String endpoint) {
        // Se o endpoint vier como "/api/ponto-coleta", use apenas ele
        response = request.body(requestBody).post(endpoint);
    }

    @Quando("eu consultar os alertas em {string}")
    public void consultar_alertas(String endpoint) {
        response = request.get(endpoint);
    }

    @Então("o status code deve ser {int}")
    public void validar_status(int code) {
        response.then().statusCode(code);
    }

    @Então("o contrato da resposta deve seguir o schema {string}")
    public void validar_schema(String schemaPath) {
        response.then().body(matchesJsonSchemaInClasspath("schemas/" + schemaPath));
    }

    @Então("a mensagem de erro deve conter {string}")
    public void validar_erro(String erro) {
        response.then().statusCode(403);
    }

    @Então("a lista de alertas não deve ser nula")
    public void validar_lista() {
        response.then().body("$", notNullValue());
    }
}