package br.pucpr.authserver.portfolio.controller.responses

import br.pucpr.authserver.portfolio.Portfolio


data class PortfolioResponse(
    val id: Long,
    val title: String,
    val image: String,
    val description: String,

) {
    constructor(portfolio: Portfolio) : this(portfolio.id!!, portfolio.title, portfolio.image, portfolio.description)
}