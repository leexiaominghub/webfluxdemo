package com.cwl.webfluxdemo;

import com.cwl.webfluxdemo.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class Client {

    public static void main (String[] args) {

        String site = "http://127.0.0.1:57508";
        //WebClient client = WebClient.create("http://127.0.0.1:56609");
        WebClient client = WebClient.create();

        User user = client.get().uri(site + "/user/1").accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(User.class).block();

        System.out.println(user.getName());

        //Flux<User> users = client.get().uri(site + "/user").accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(User.class);
        //users.map(u -> u.getName()).buffer().doOnNext(System.out::println).blockFirst();
    }
}
