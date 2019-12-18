package com.nosbielc.reactive.products.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@ToString
public class ProductAvailability {

  private Product product;
  private long availability;

}
