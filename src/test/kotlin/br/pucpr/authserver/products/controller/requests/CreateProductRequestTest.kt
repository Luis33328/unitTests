package br.pucpr.authserver.products.controller.requests

import br.pucpr.authserver.users.Stubs.productStub
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class CreateProductRequestTest {
    @Test
    fun `toProduct creates a new product based on the request`() {
        /*with(productStub()) {
            val req = CreateProductRequest(name, price, image, description).toProduct()
            req.id shouldBe null
            req.name shouldBe name
            req.price shouldBe price
            req.image shouldBe image
            req.description shouldBe description
        }*/
    }
}