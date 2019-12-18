package com.nosbielc.reactive.products.gateways;

import com.nosbielc.reactive.products.domains.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductGateway {

  Mono<Product> save(Product product);

  Mono<Product> findByCode(String code);

  Mono<Void> deleteByCode(String code);

  Flux<Product> findAll();

}
