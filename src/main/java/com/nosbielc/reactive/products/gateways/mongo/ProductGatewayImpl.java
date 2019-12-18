package com.nosbielc.reactive.products.gateways.mongo;

import com.nosbielc.reactive.products.domains.Product;
import com.nosbielc.reactive.products.gateways.ProductGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductGatewayImpl implements ProductGateway {

  private ProductRepository repository;

  @Autowired
  public ProductGatewayImpl(final ProductRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Product> save(final Product product) {
    return repository.save(product);
  }

  @Override
  public Mono<Product> findByCode(final String code) {
    return repository.findById(code);
  }

  public Mono<Void> deleteByCode(final String code) {
    return repository.deleteById(code);
  }

  @Override
  public Flux<Product> findAll() {
    return repository.findAll();
  }
}
