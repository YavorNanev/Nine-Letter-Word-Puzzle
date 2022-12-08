package com.nineletterword.exercises;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class WebClientService {

    public <E> Flux<E> doGetFlux(String uri, Class<E> responseClass) {

        return WebClient.builder().build().get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(responseClass);
    }

}
