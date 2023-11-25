package br.pucpr.authserver.products.controller

import br.pucpr.authserver.users.SortDir

import br.pucpr.authserver.products.ProductService
import br.pucpr.authserver.products.controller.requests.CreateProductRequest
import br.pucpr.authserver.products.controller.requests.PatchProductRequest
import br.pucpr.authserver.products.controller.responses.ProductResponse
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
@RequestMapping("/products")
class ProductController(val service: ProductService) {
    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PostMapping
    fun insert(@Valid @RequestBody product: CreateProductRequest) =
        ProductResponse(service.insert(product.toProduct()))
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PatchMapping("/{id}")
    fun update(
            @PathVariable id: Long,
            @Valid @RequestBody request: PatchProductRequest,
            auth: Authentication
    ): ResponseEntity<ProductResponse> {
        //val token = auth.principal as? UserToken ?: throw ForbiddenException()
        //if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id, request.name!!)
            ?.let { ResponseEntity.ok(ProductResponse(it)) }
            ?: ResponseEntity.noContent().build()
    }

    @GetMapping
    fun list(@RequestParam sortDir: String? = null) =
            service.findAll(SortDir.findOrThrow(sortDir ?: "ASC"))


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.findByIdOrNull(id)
            ?.let { ResponseEntity.ok(ProductResponse(it)) }
            ?: ResponseEntity.notFound().build()

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.ok().build()
        else ResponseEntity.notFound().build()


}