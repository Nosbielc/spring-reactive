package com.nosbielc.reactive.products.usecases;

import com.nosbielc.reactive.products.domains.ProductAvailability;
import com.nosbielc.reactive.products.gateways.InventoryGateway;
import com.nosbielc.reactive.products.gateways.ProductGateway;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class GetAvailability {

  private ProductGateway productGateway;
  private InventoryGateway inventoryGateway;

  @Autowired
  public GetAvailability(final ProductGateway productGateway, final InventoryGateway inventoryGateway) {
    this.productGateway = productGateway;
    this.inventoryGateway = inventoryGateway;
  }

  public Flux<ProductAvailability> execute() {
    return productGateway.findAll()
        .flatMap(product ->
            Flux.combineLatest(
                Mono.just(product),
                inventoryGateway.getAvailability(product.getCode()),
                (p, a) -> new ProductAvailability(p, a))
    ).filter(pa -> pa.getAvailability() > 0l);
  }

  // uncomment for test blocking IO call (resttemplate) with parallel stream (enable InventoryGatewayBlockingImpl)
  /*public Flux<ProductAvailability> execute() {
    int coreCount = Runtime.getRuntime().availableProcessors();
    AtomicInteger assigner = new AtomicInteger(0);

    return productGateway.findAll()
        .groupBy(p -> assigner.incrementAndGet() % coreCount)
        .flatMap(grp->
            grp.publishOn(Schedulers.parallel())
                .map(product ->
                    Flux.combineLatest(
                        Mono.just(product),
                        inventoryGateway.getAvailability(product.getCode()),
                        (p, a) -> new ProductAvailability(p, a))
                )
        ).flatMap(f -> f)
        .filter(pa -> pa.getAvailability() > 0l)
        .doOnComplete(() -> log.info("Get products with availability"));
  }*/

}
