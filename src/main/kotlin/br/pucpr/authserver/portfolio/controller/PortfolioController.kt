package br.pucpr.authserver.users.controller

import br.pucpr.authserver.portfolio.PortfolioService
import br.pucpr.authserver.portfolio.controller.requests.CreatePortfolioRequest
import br.pucpr.authserver.portfolio.controller.requests.PatchPortfolioRequest
import br.pucpr.authserver.portfolio.controller.responses.PortfolioResponse
import br.pucpr.authserver.users.SortDir

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/portfolio")
class PortfolioController(val service: PortfolioService) {
    @PostMapping
    fun insert(@Valid @RequestBody portfolio: CreatePortfolioRequest) =
        PortfolioResponse(service.insert(portfolio.toProduct()))
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PatchMapping("/{id}")
    fun update(
            @PathVariable id: Long,
            @Valid @RequestBody request: PatchPortfolioRequest,
            auth: Authentication
    ): ResponseEntity<PortfolioResponse> {
        //val token = auth.principal as? UserToken ?: throw ForbiddenException()
        //if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id, request.title!!)
            ?.let { ResponseEntity.ok(PortfolioResponse(it)) }
            ?: ResponseEntity.noContent().build()
    }

    @GetMapping
    fun list(@RequestParam sortDir: String? = null) =
            service.findAll(SortDir.findOrThrow(sortDir ?: "ASC"))


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(PortfolioResponse(it)) }
            ?: ResponseEntity.notFound().build()

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()


}