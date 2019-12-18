package com.nosbielc.reactive.products.gateways.mongo;

import com.nosbielc.reactive.products.domains.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

  Flux<Product> findByBrand(Mono<String> brand);

}
