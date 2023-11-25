package br.pucpr.authserver.users.controller

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.ForbiddenException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.Stubs.adminStub
import br.pucpr.authserver.users.Stubs.authStub
import br.pucpr.authserver.users.Stubs.userStub
import br.pucpr.authserver.users.Stubs.productStub
import br.pucpr.authserver.products.ProductService
import br.pucpr.authserver.products.controller.ProductController
import br.pucpr.authserver.products.controller.requests.CreateProductRequest
import br.pucpr.authserver.users.controller.requests.LoginRequest
import br.pucpr.authserver.products.controller.requests.PatchProductRequest
import br.pucpr.authserver.users.controller.responses.LoginResponse
import br.pucpr.authserver.products.controller.responses.ProductResponse
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

class ProductControllerTest {
    private val serviceMock = mockk<ProductService>()
    private val controller = ProductController(serviceMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(serviceMock)
    }

    @Test
    fun `insert must return the new product with CREATED code`() {
        val product = productStub(id = 1)

        val request = CreateProductRequest(product.name, product.price, product.image, product.description)
        every { serviceMock.insert(any()) } returns product
        with(controller.insert(request)) {
            statusCode shouldBe HttpStatus.CREATED
            body shouldBe ProductResponse(product)
        }
    }

    @Test
    fun `update should return NO_CONTENT if the service returns null`() {
        val product = productStub(id = 1)
        val user = userStub(id = 1)

        every { serviceMock.update(product.id!!, product.name) } returns null
        with(controller.update(product.id!!, PatchProductRequest(product.name), authStub(user))) {
            statusCode shouldBe HttpStatus.NO_CONTENT
            body shouldBe null
        }
    }

    /*@Test
    fun `update should work if the ADMIN is updating any user`() {
        val user = userStub(id = 1)
        val request = PatchUserRequest(user.name)

        every { serviceMock.update(user.id!!, user.name) } returns user
        with(controller.update(user.id!!, request, authStub(adminStub()))) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe UserResponse(user)
        }
    }*/


    @Test
    fun `update should forward NotFoundException if the product is not found`() {
        val product = productStub(id = 1)
        val user = userStub(id = 1)

        every { serviceMock.update(product.id!!, product.name) } throws NotFoundException()
        assertThrows<NotFoundException> {
            controller.update(product.id!!, PatchProductRequest(product.name), authStub(user))
        }
    }

    /*@Test
    fun `list should return all found product with the given sort parameter`() {
        val products = listOf(
            productStub(1, "Camiseta"), productStub(2, "Bone")
        )

        every { serviceMock.findAll(SortDir.DESC) } returns products
        with(controller.list("DESC")) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe products.map { ProductResponse(it) }
        }
    }*/

    /*@Test
    fun `list should use ASC as default sort parameter`() {
        val products = listOf(
                productStub(1, "Camiseta"), productStub(2, "Bone")
        )

        every { serviceMock.findAll(SortDir.ASC) } returns products
        with(controller.list(null)) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe products.map { ProductResponse(it) }
        }
    }*/



    @Test
    fun `list should throw BadRequestException with a invalid sort parameter`() {
        assertThrows<BadRequestException> {
            controller.list("INVALID")
        }
    }

    @Test
    fun `getById must returns the product`() {
        val product = productStub(id = 1)
        every { serviceMock.findByIdOrNull(product.id!!) } returns product
        with(controller.getById(product.id!!)) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe ProductResponse(product)
        }
    }

    @Test
    fun `getById must return NOT FOUND if the product is not found`() {
        every { serviceMock.findByIdOrNull(1) } returns null
        controller.getById(1).statusCode shouldBe HttpStatus.NOT_FOUND
    }

    @Test
    fun `delete should return OK if the product gets deleted`() {
        every { serviceMock.delete(1) } returns true
        with(controller.delete(1)) {
            statusCode shouldBe HttpStatus.OK
            body shouldBe null
        }
    }

    @Test
    fun `delete should return NOT_FOUND if the product does not exists`() {
        every { serviceMock.delete(1) } returns false
        controller.delete(1).statusCode shouldBe HttpStatus.NOT_FOUND
    }

}