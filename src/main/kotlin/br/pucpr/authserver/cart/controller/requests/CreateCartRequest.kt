package br.pucpr.authserver.cart.controller.requests


import br.pucpr.authserver.cart.Cart
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.users.User
import jakarta.validation.constraints.NotBlank

data class CreateCartRequest(
        @field:NotBlank
    val product: Product?,
        @field:NotBlank
    val user: User?,
        @field:NotBlank
    val priceProduct: String?,
) {
    fun toProduct() = Cart(
            product = product!!,
            user = user!!,
            priceProduct = priceProduct!!,
    )
}