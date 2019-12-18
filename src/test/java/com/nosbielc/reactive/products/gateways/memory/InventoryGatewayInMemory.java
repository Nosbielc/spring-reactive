package com.nosbielc.reactive.products.gateways.memory;

import com.nosbielc.reactive.products.gateways.InventoryGateway;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class InventoryGatewayInMemory implements InventoryGateway {

  private Map<String, Long> inventory = new HashMap<>();

  public void addAvailability(final String productCode, Long quantity) {
    this.inventory.put(productCode, quantity);
  }

  @Override
  public Mono<Long> getAvailability(final String productCode) {
    return Mono.just(inventory.getOrDefault(productCode, 0l));
  }

}
