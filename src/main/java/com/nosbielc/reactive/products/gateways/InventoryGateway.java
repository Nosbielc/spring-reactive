package com.nosbielc.reactive.products.gateways;

import reactor.core.publisher.Mono;

public interface InventoryGateway {

  Mono<Long> getAvailability(String productCode);

  default String sanitize(final String content) {
    return content.replace("\n", "").replace("\r", "");
  }

}
