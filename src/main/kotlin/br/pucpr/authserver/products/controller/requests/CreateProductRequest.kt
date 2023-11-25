package br.pucpr.authserver.products.controller.requests


import br.pucpr.authserver.products.Product
import jakarta.validation.constraints.NotBlank

data class CreateProductRequest(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    val price: String?,
    @field:NotBlank
    val image: String?,
    @field:NotBlank
    val description: String?
) {
    fun toProduct() = Product(
            name = name!!,
            price = price!!,
            image = image!!,
            description = description!!,
    )
}