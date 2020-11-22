package com.cwl.webfluxdemo;

import com.cwl.webfluxdemo.handler.UserHandler;
import com.cwl.webfluxdemo.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

public class Server {

    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.createReactorServer();
        System.out.println("exit press any key");
        System.in.read();
    }


    public RouterFunction<ServerResponse> routerFunction() {
        UserService userService = new UserService();
        UserHandler userHandler = new UserHandler(userService);

        return RouterFunctions.route(
                GET("/user/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::findUserById)
                .andRoute(GET("/user").and(accept(MediaType.APPLICATION_JSON)), userHandler::findAllUsers);

    }

    public void createReactorServer () {

        // 路由和handler匹配
        RouterFunction<ServerResponse> route = routerFunction();
        HttpHandler httpHandler = toHttpHandler(route);
        ReactorHttpHandlerAdapter reactorHttpHandlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);

        // 创建服务器
        HttpServer httpServer = HttpServer.create();
        httpServer.handle(reactorHttpHandlerAdapter).bindNow();
    }

}
