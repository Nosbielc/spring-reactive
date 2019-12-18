package com.nosbielc.reactive.products.gateways.mongo;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.nosbielc.reactive.products.domains.Product;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryIntegrationTest {

  @Autowired
  ProductRepository repository;

  @Autowired
  ReactiveMongoOperations operations;

  @Before
  public void setUp() {

    final Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(Product.class)
        .flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists))
        .then(operations.createCollection(Product.class, CollectionOptions.empty()));

    StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

    final Flux<Product> insertAll = operations.insertAll(
        Flux.just(
            new Product("AAA-5555", "Nike", "Tenis Nike", "Preto", 19900),
            new Product("AAA-5554", "Nike", "Tenis Nike", "Branco", 19700),
            new Product("BBB-3333", "Adidas", "Tenis Adidas", "Branco", 29900)
        ).collectList());

    StepVerifier.create(insertAll).expectNextCount(3).verifyComplete();
  }

  @Test
  public void findOneByCodeShouldReturnOneProduct() {
    final Mono<Product> product =  repository.findById("AAA-5555");
    StepVerifier.create(product)
        .expectNextCount(1l)
        .verifyComplete();
  }

  @Test
  public void findOneByInvalidCodehouldReturnEmpty() {
    final Mono<Product> product =  repository.findById("123");
    StepVerifier.create(product)
        .expectNextCount(0l)
        .verifyComplete();
  }

  @Test
  public void findByBrandShouldReturnTwoProducts() {
    final Flux<Product> products = repository.findByBrand(Mono.just("Nike"));
    StepVerifier.create(products)
        .expectNextCount(2l)
        .verifyComplete();
  }

  @Test
  public void deleteAndFindOneProductShouldOk() {
    final Flux<Product> deleteAndFind =
        repository.deleteById(Mono.just("BBB-3333"))
            .thenMany(repository.findById("BBB-3333"));

    StepVerifier.create(deleteAndFind)
        .expectSubscription()
        .expectNextCount(0l)
        .expectComplete();
  }

  @Test
  public void saveNewProductShouldOk() {
    final Mono<Product> newProduct = repository.save(
        new Product("BBB-3332", "Adidas", "Tenis Adidas", "Azul", 33900));
    StepVerifier.create(newProduct)
        .expectNextCount(1)
        .expectComplete();
  }

}
