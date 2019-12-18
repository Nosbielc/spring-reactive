package com.nosbielc.reactive.products.usecases

import com.nosbielc.reactive.products.domains.Product
import com.nosbielc.reactive.products.gateways.memory.InventoryGatewayInMemory
import com.nosbielc.reactive.products.gateways.memory.ProductGatewayInMemory
import reactor.test.StepVerifier
import spock.lang.Specification

class GetAvailabilitySpec extends Specification {

    def productGateway = new ProductGatewayInMemory()
    def inventoryGateway = new InventoryGatewayInMemory()
    def getAvailability = new GetAvailability(productGateway, inventoryGateway)

    def setup() {
        productGateway.clear()
    }

    def "get availability from a Tenis Nike" () {
        given: "available Tenis Nike - 10 items"
          def product = new Product("AAA-2222", "Nike", "Tenis Nike", "Preto", 29900)
          productGateway.save(product)
          inventoryGateway.addAvailability("AAA-2222", 10l)
        when: "get product and availability data"
          def productAvailable = getAvailability.execute()
        then: "a Tenis Nike with 10 elements should be found"
          StepVerifier.create(productAvailable)
                  .expectNextMatches({pa -> pa.product.code == "AAA-2222" && pa.availability == 10l})
                  .verifyComplete()
    }

    def "get availability from a Tenis Mizuno" () {
        given: "unavailable Tenis Mizuno"
        def product2 = new Product("CCC-4224", "Mizuno", "Tenis Mizuno", "Azul", 49900)
          productGateway.save(product2)
        when: "get product and availability data"
          def productAvailable = getAvailability.execute()
        then: "just Tenis Adidas with 20 items should be found"
          StepVerifier.create(productAvailable)
                .expectNextCount(0l)
                .verifyComplete()
    }

    def "get availability from a Tenis Adidas and Tenis Mizuno" () {
        given: "available Tenis Adidas - 20 items"
          def product1 = new Product("BBB-4444", "Adidas", "Tenis Adidas", "Branco", 19900)
          productGateway.save(product1)
          inventoryGateway.addAvailability("BBB-4444", 20)
        and: "unavailable Tenis Mizuno"
          def product2 = new Product("CCC-4224", "Mizuno", "Tenis Mizuno", "Azul", 49900)
          productGateway.save(product2)
        when: "get product and availability data"
          def productAvailable = getAvailability.execute()
        then: "just Tenis Adidas with 20 items should be found"
        StepVerifier.create(productAvailable)
                .expectNextMatches({pa -> pa.product.code == "BBB-4444" && pa.availability == 20l})
                .expectNextCount(0l)
                .verifyComplete()
    }


}
