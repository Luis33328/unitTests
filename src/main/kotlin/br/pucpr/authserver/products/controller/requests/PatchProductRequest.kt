package br.pucpr.authserver.products.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchProductRequest(
    @field:NotBlank
    val name: String?
)