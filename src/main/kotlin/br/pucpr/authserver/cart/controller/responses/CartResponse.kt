package br.pucpr.authserver.cart.controller.responses


import br.pucpr.authserver.cart.Cart
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.users.User

data class CartResponse(
        val id: Long,
        val product: Product,
        val user: User,
        val priceProduct: String,

        ) {
    constructor(cart: Cart) : this(cart.id!!, cart.product, cart.user, cart.priceProduct)
}