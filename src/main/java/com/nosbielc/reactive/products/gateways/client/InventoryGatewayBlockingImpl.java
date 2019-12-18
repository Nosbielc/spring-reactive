package com.nosbielc.reactive.products.gateways.client;

import com.nosbielc.reactive.products.gateways.InventoryGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Slf4j
//@Component
public class InventoryGatewayBlockingImpl implements InventoryGateway {

  @Value("${inventory.uri:https://www.random.org}")
  private String inventoryUri;
  @Value("${inventory.endpoint:/integers/?num=1&min=0&max=20&col=1&base=10&format=plain}")
  private String inventoryEndpoint;

  @Override
  public Mono<Long> getAvailability(String productCode) {
    final String url = inventoryUri+inventoryEndpoint;
    return Mono.just(getRandomAsString(url))
        .map(c -> Long.valueOf(sanitize(c)))
        .doOnNext(c -> log.debug("Get [blocking] inventory availability: {} | product: {}", c, productCode));
  }

  private String getRandomAsString(final String url) {
    final HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

    final RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
  }

}
