package com.nosbielc.reactive.products.gateways.client;

import com.nosbielc.reactive.products.gateways.InventoryGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class InventoryGatewayImpl implements InventoryGateway {

  private final WebClient client;

  @Value("${inventory.uri:https://www.random.org}")
  private String inventoryUri;
  @Value("${inventory.endpoint:/integers/?num=1&min=0&max=20&col=1&base=10&format=plain}")
  private String inventoryEndpoint;

  @Autowired
  public InventoryGatewayImpl(final WebClient.Builder webClientBuilder) {
    this.client = webClientBuilder.baseUrl(inventoryUri).build();
  }

  @Override
  public Mono<Long> getAvailability(final String productCode) {
    return getRandomAsString(inventoryUri+inventoryEndpoint)
        .map(c -> Long.valueOf(sanitize(c)))
        .doOnNext(c -> log.debug("Get inventory availability: {} | product: {}", c, productCode));
  }

  private Mono<String> getRandomAsString(final String url) {
    return client.get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty("0")
        //fallback
        .onErrorReturn("0");
  }
}
