package com.nosbielc.reactive.products.gateways.http;

import static org.mockito.BDDMockito.given;

import com.nosbielc.reactive.products.domains.Product;
import com.nosbielc.reactive.products.gateways.ProductGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ProductController.class)
public class ProductControllerTest {

  @Autowired
  WebTestClient client;

  @MockBean
  ProductGateway gateway;

  @Test
  public void testGetProductByCodeShouldBeOk() {
    final Product product =
        new Product("BBB-1111", "Adidas", "Tenis Adidas", "Branco", 59900);

    given(gateway.findByCode("BBB-1111")).willReturn(Mono.just(product));

    client.get().uri("/products/BBB-1111")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.code").isEqualTo("BBB-1111")
        .jsonPath("$.brand").isEqualTo("Adidas")
        .jsonPath("$.description").isEqualTo("Tenis Adidas")
        .jsonPath("$.color").isEqualTo("Branco")
        .jsonPath("$.price").isEqualTo(59900);
  }

  @Test
  public void testGetInvalidProductShouldReturnNotFound() {
    given(gateway.findByCode("BBB-1111")).willReturn(Mono.empty());

    client.get().uri("/products/BBB-1111").exchange()
        .expectStatus().isNotFound();
  }

  @Test
  public void testPostValidProductShouldReturnCreate() {
    final Product product =
        new Product("AAA-3333", "Nike", "Tenis Nike", "Azul", 29900);

    given(gateway.save(BDDMockito.any(Product.class))).willReturn(Mono.just(product));

    client.post().uri("/products").body(BodyInserters.fromObject(product))
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .jsonPath("$.code").isEqualTo("AAA-3333")
        .jsonPath("$.brand").isEqualTo("Nike")
        .jsonPath("$.description").isEqualTo("Tenis Nike")
        .jsonPath("$.color").isEqualTo("Azul")
        .jsonPath("$.price").isEqualTo(29900);
  }

  @Test
  public void testPostInvalidProductShouldReturnBadRequest() {
    final Product product = new Product();
    product.setCode("ZZZ-0000");

    client.post().uri("/products").body(BodyInserters.fromObject(product))
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  public void testDeleteValidProductShouldReturnAccept() {
    given(gateway.deleteByCode("AAA-4444")).willReturn(Mono.empty());

    client.delete().uri("/products/BBB-1111")
        .exchange()
        .expectStatus().isAccepted();
  }

  @Test
  public void testGetAllProductShouldBeOk() {
    final Product p1 =
        new Product("BBB-1111", "Adidas", "Tenis Adidas", "Branco", 59900);
    final Product p2 =
        new Product("CCC-4334", "Mizuno", "Tenis Mizuno", "Cinza", 69900);

    given(gateway.findAll()).willReturn(Flux.just(p1, p2));

    client.get().uri("/products" )
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
        .expectBodyList(Product.class);
  }

}
