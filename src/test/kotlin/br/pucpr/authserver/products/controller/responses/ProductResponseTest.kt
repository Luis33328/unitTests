package br.pucpr.authserver.users.controller.responses

import br.pucpr.authserver.users.Stubs.productStub
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import br.pucpr.authserver.products.controller.responses.ProductResponse

class ProductResponseTest {
    @Test
    fun `constructor should copy all important values`() {
        val product = productStub()
        val response = ProductResponse(product)
        response.id shouldBe product.id
        response.name shouldBe product.name
        response.price shouldBe product.price
        response.image shouldBe product.image
        response.description shouldBe product.description

    }
}