package com.nosbielc.reactive.products;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

public class Server {

  private static final String HOST = "localhost";
  private static final int PORT = 8080;

  public void startReactorServer() {
    RouterFunction<ServerResponse> route =
        route(GET("/hello"),
            request -> ok().body(fromObject("Hello reactive world!")));

    HttpServer server = HttpServer.create(HOST, PORT);
    server.newHandler(new ReactorHttpHandlerAdapter(toHttpHandler(route)))
        .block();
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server();
    server.startReactorServer();

    System.out.println("Press ENTER to exit.");
    System.in.read();
  }

}
