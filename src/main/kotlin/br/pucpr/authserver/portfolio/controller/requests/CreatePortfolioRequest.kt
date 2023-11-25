package br.pucpr.authserver.portfolio.controller.requests

import br.pucpr.authserver.portfolio.Portfolio
import jakarta.validation.constraints.NotBlank

data class CreatePortfolioRequest(
    @field:NotBlank
    val name: String?,
    @field:NotBlank
    val price: String?,
    @field:NotBlank
    val image: String?,
    @field:NotBlank
    val description: String?
) {
    fun toProduct() = Portfolio(
            image = image!!,
            title = name!!,
            description = description!!,
    )
}