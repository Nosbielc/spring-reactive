package com.nosbielc.reactive.products.gateways.http;

import com.nosbielc.reactive.products.domains.ProductAvailability;
import com.nosbielc.reactive.products.gateways.InventoryGateway;
import com.nosbielc.reactive.products.usecases.GetAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AvailabilityController {

  private InventoryGateway gateway;
  private GetAvailability getAvailability;

  @Autowired
  public AvailabilityController(final InventoryGateway gateway, final GetAvailability getAvailability) {
    this.gateway = gateway;
    this.getAvailability = getAvailability;
  }

  @GetMapping("/products/available")
  @ResponseStatus(HttpStatus.OK)
  public Flux<ProductAvailability> findAllAvailable() {
    return getAvailability.execute();
  }

}
