package com.nosbielc.reactive.products.gateways.memory;

import com.nosbielc.reactive.products.domains.Product;
import com.nosbielc.reactive.products.gateways.ProductGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;

public class ProductGatewayInMemory implements ProductGateway {

    private Collection<Product> products = new ArrayList<>();

    @Override
    public Mono<Product> save(final Product product) {
        products.add(product);
        return Mono.just(product);
    }

    @Override
    public Mono<Product> findByCode(final String code) {
        return Flux.fromIterable(products)
                .filter(p -> p.getCode().equals(code))
                .next();
    }

    @Override
    public Mono<Void> deleteByCode(final String code) {
        products.removeIf(product -> product.getCode().equals(code));
        return Mono.empty();
    }

    @Override
    public Flux<Product> findAll() {
        return Flux.fromIterable(products);
    }

    public void clear() {
        products.clear();
    }
}
