package br.pucpr.authserver.cart.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchCartRequest(
    @field:NotBlank
    val id: Long?
)