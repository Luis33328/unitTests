package br.pucpr.authserver.products.controller.responses


import br.pucpr.authserver.products.Product

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: String,
    val image: String,
    val description: String,

) {
    constructor(product: Product) : this(product.id!!, product.name, product.price, product.image, product.description)
}