package br.com.example.HttpRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

	public static void main(String[] args) {
		
		 Vertx vertx = Vertx.vertx();
	     vertx.deployVerticle(new MainVerticle());
	}
	
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

   Router router = Router.router(vertx);

// Rota para listagem de usuários
    router.route("/api/usuarios").handler(ctx -> {
      // Manipulação da requisição
      ctx.response().putHeader("Content-Type", "application/json")
       //sua lógica de negócio devolvendo uma listagem de usuários
        .end(getUsers().encode());
    });

// Rota para detalhes de um usuário específico
    router.route("/api/usuarios/:id").handler(ctx -> {
      String id = ctx.request().getParam("id");
      // Manipulação da requisição
      int statusCode = 200;
      String message = "OK";
      String body = "";
      try {
		
    	  body =  findUser(Integer.parseInt(id)).encode();
	} catch (Exception e) {
		System.out.println(e);
		statusCode = 404;
		message = "User not found";
	} 
      ctx
          .response()
          .setStatusCode(statusCode)
          .setStatusMessage(message)
          .putHeader("Content-Type", "application/json")
        .end(body);
    });

// Criação do servidor HTTP e configuração do roteador
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080 , http -> {
          if (http.succeeded()) {
              startPromise.complete();
              System.out.println("Servidor HTTP Iniciado 8080");
            } else {
              startPromise.fail(http.cause());
            }
          });
  }

    private JsonObject findUser(int id) {
    	JsonArray users = createUsers();
    	
    	
	return users.getJsonObject(id - 1);
}

	private JsonObject getUsers() {
    	 // Create a JSON array for users
        JsonArray users = createUsers();

        // Create the response JSON object
        JsonObject responseJson = new JsonObject();
        responseJson.put("status", "200");
        responseJson.put("message", "Listagem de usuários");
        responseJson.put("users", users);
	 return responseJson;
}

	private JsonArray createUsers() {
		  JsonArray users = new JsonArray();
        users.add(new JsonObject()
        		.put("id", 1)
                .put("name", "John Doe")
                .put("age", 30));
        users.add(new JsonObject()
        		.put("id", 2)
                .put("name", "Jane Smith")
                .put("age", 35));
        
        return users;
				
	}
}
