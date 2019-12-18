package com.nosbielc.reactive.products;

import reactor.core.publisher.Flux;

public class Main {

  public static void main(String[] args) {
    final Flux<String> brands = Flux.just("Under Armour", "Asics", "Nike", "Adidas", "Mizuno");
    brands.sort()
        .subscribe(System.out::println); //print all sorted items

    brands.skip(1)
        .groupBy(b -> b.charAt(0))
        .flatMap(group -> group.collectSortedList())
        .subscribe(System.out::println); //print items grouped by first char
  }

}
