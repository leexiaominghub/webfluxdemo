package com.cwl.webfluxdemo.service;

import com.cwl.webfluxdemo.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.security.action.PutAllAction;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();

    public UserService() {
        this.users.put(1, new User("lxm", 18));
        this.users.put(2, new User("mm", 18));
    }

    public Mono<User> getUserById(int id) {
        try {
            Thread.currentThread().sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.justOrEmpty(this.users.get(id));
    }

    public Flux<User> getAllUsers() {
        return Flux.fromIterable(this.users.values());
    }

    public Mono<Void> saveUser(Mono<User> user) {
        return user.doOnNext(p -> {this.users.put(users.size() + 1, p);}).thenEmpty(Mono.empty());

    }
}
