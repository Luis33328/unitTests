package br.pucpr.authserver.portfolio.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchPortfolioRequest(
    @field:NotBlank
    val title: String?
)