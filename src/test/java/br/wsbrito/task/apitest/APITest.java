package br.wsbrito.task.apitest;

import java.time.LocalDate;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
//	public void test() {
//		RestAssured
//			.given()
//				.log().all()
//			.when()
//				.get("http://localhost:8080/tasks-backend/todo")
//			.then()
//				.log().all()
//			;
//	}
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8080/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured
			.given()
				//.log().all()
			.when()
				.get("/todo")
			.then()
				//.log().all()
				.statusCode(200)
			;
	}

	@Test
	public void deveAdicionarTarefaComSucesso() {
		LocalDate futureDate = LocalDate.now().plusYears(10L);
		//System.out.println(futureDate);
		RestAssured
			.given()
				.body(String.format("{ \"task\":\"Teste via API\",\"dueDate\":\"%s\"}", futureDate.toString()))
				.contentType(ContentType.JSON)
				//.log().all()
			.when()
				.post("/todo")
			.then()
				//.log().all()
				.statusCode(201)
			;
	}

	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured
			.given()
				.body("{ \"task\":\"Teste via API\",\"dueDate\":\"2011-04-07\"}")
				.contentType(ContentType.JSON)
				//.log().all()
			.when()
				.post("/todo")
			.then()
				.log().all()
				.statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"))
			;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		// Inclusão da tarefe
		LocalDate futureDate = LocalDate.now().plusYears(10L);
		Integer id = 
			RestAssured
				.given()
					.body(String.format("{ \"task\":\"Tarefa para remocao\",\"dueDate\":\"%s\"}", futureDate.toString()))
					.contentType(ContentType.JSON)
					//.log().all()
				.when()
					.post("/todo")
				.then()
					//.log().all()
					.statusCode(201)
					.extract().path("id" )
				;
		// Exclusão da tarefa
		RestAssured.given()
			.when()
				.delete("/todo/"+id)
			.then()
				.statusCode(204)
		;
	}

}
