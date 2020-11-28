package com.cwl.webfluxdemo.handler;

import com.cwl.webfluxdemo.entity.User;
import com.cwl.webfluxdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UserHandler {

    private final Map<Integer, User> users = new HashMap<>();

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
        this.users.put(1, new User("lxm", 18));
        this.users.put(2, new User("mm", 18));
    }

    public Mono<ServerResponse> findUserById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        Mono<ServerResponse> notfound = ServerResponse.notFound().build();

        log.info("1");
        /*
        Mono<User> user = Mono.fromSupplier(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.users.get(id);});

         */
        Mono<User> user = this.userService.getUserById(id);
        log.info("2");

        //Mono<User> user2 = this.userService.getUserById(2);
        //user2.subscribe(System.out::println);

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
