package com.nosbielc.reactive.products.gateways.http;

import com.nosbielc.reactive.products.domains.Product;
import com.nosbielc.reactive.products.exceptions.ProductNotFoundException;
import com.nosbielc.reactive.products.gateways.ProductGateway;
import java.time.Duration;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ProductController {

  private ProductGateway gateway;

  @Autowired
  public ProductController(final ProductGateway gateway) {
    this.gateway = gateway;
  }

  @PostMapping("/products")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Product> create(
      @Valid @RequestBody final Product product) {
    return gateway.save(product)
        .doOnNext(p -> log.debug("Create new product - {}", p));
  }

  @PutMapping("/products")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Product> update(
      @Valid @RequestBody final Mono<Product> product) {
    return product.flatMap(p -> gateway.save(p)
        .doOnNext(prod -> log.info("Updating product - {}", prod)));
  }

  @GetMapping("/products/{code}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Product> findOne(@PathVariable final String code) {
    return gateway.findByCode(code)
        .switchIfEmpty(Mono.error(new ProductNotFoundException(code)))
        .doOnNext(p -> log.debug("Get product by code {}", code));
  }

  @DeleteMapping("/products/{code}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> delete(@PathVariable final String code) {
    return gateway.deleteByCode(code);
  }

  @GetMapping(path = "/products", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Flux<Product> findAll() {
    return gateway.findAll()
        //delayElements just to create high latency effect...
        .delayElements(Duration.ofMillis(300))
        .doOnComplete(() -> log.debug("Get all products"));
  }

}
