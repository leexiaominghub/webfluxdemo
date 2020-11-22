package com.cwl.webfluxdemo.handler;

import com.cwl.webfluxdemo.entity.User;
import com.cwl.webfluxdemo.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> findUserById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        Mono<ServerResponse> notfound = ServerResponse.notFound().build();
        Mono<User> user = this.userService.getUserById(id);
        return user.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(user, User.class))
                .switchIfEmpty(notfound);
    }

    public Mono<ServerResponse> findAllUsers(ServerRequest request) {
        Flux<User> users = this.userService.getAllUsers();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, User.class);
    }

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok().build(this.userService.saveUser(userMono));

    }
}
