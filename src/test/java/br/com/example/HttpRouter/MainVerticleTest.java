package br.com.example.HttpRouter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class MainVerticleTest {

    private Vertx vertx;
    private int port;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        this.vertx = vertx;

        // implanta the MainVerticle
        vertx.deployVerticle(MainVerticle.class.getName(), testContext.succeeding(id -> testContext.completeNow()));

        // Porta do server.
        port = 8080;
    }

    @AfterEach
    void tearDown(VertxTestContext testContext) {
        vertx.close(testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testHelloVertxEndpoint(VertxTestContext testContext) {
        // Create an HTTP client
        HttpClient client = vertx.createHttpClient();

        // Send a GET request to the Hello Vert.x endpoint
        client.request(HttpMethod.GET, port, "localhost", "/api/usuarios")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onSuccess(body -> {
                	System.out.println(body);
                    // Assert the response
                    testContext.verify(() -> {
                    	assertEquals("{\"status\":\"200\",\"message\":\"Listagem de usuários\",\"users\":[{\"id\":1,\"name\":\"John Doe\",\"age\":30},{\"id\":2,\"name\":\"Jane Smith\",\"age\":35}]}",
                    			body.toString());
                    });

                    testContext.completeNow();
                })
                .onFailure(testContext::failNow);
                
    }
    
//    @Test
//    void testHelloVertxEndpoint(VertxTestContext testContext) {
//        // Create an HTTP client
//        HttpClient client = vertx.createHttpClient();
//
//        // Send a GET request to the Hello Vert.x endpoint
//        client.request(HttpMethod.GET, port, "localhost", "/api/usuarios")
//                .compose(req -> req.send().compose(HttpClientResponse::body))
//                .onSuccess(body -> {
//                	System.out.println(body);
//                    // Assert the response
//                    testContext.verify(() -> {
//                    	assertEquals("{\"status\":\"200\",\"message\":\"Listagem de usuários\",\"users\":[{\"name\":\"John Doe\",\"age\":30},{\"name\":\"Jane Smith\",\"age\":35}]}",
//                    			body.toString());
//                    });
//
//                    testContext.completeNow();
//                })
//                .onFailure(testContext::failNow);
//                
//    }
}

