package br.pucpr.authserver.users

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.products.ProductRepository
import br.pucpr.authserver.products.ProductService
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.users.Stubs.roleStub
import br.pucpr.authserver.users.Stubs.userStub
import br.pucpr.authserver.users.Stubs.productStub
import br.pucpr.authserver.users.controller.responses.LoginResponse
import br.pucpr.authserver.users.controller.responses.UserResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import java.util.Optional

internal class ProductServiceTest {
    private val repositoryMock = mockk<ProductRepository>()
    private val jwt = mockk<Jwt>()

    private val service = ProductService(repositoryMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(repositoryMock, jwt)
    }

    @Test
    fun `insert must throw BadRequestException if a product with the same name is found`() {
        val product = productStub(id = null)
        every { repositoryMock.findByName(product.name) } returns productStub()
        assertThrows<BadRequestException> {
            service.insert(product)
        } shouldHaveMessage "Product already exists"
    }

    /*@Test
    fun `insert must return the saved product if it's inserted`() {
        val product = productStub(id = null)
        every { repositoryMock.findByName(product.name) } returns null

        val saved = productStub()
        every { repositoryMock.save(product) } returns saved
        service.insert(product) shouldBe saved
    }*/

    @Test
    fun `update must throw NotFoundException if the product does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        assertThrows<NotFoundException> {
            service.update(1, "name")
        }
    }

    @Test
    fun `update must return null if there's no changes`() {
        val product = productStub()
        every { repositoryMock.findById(1) } returns Optional.of(product)
        service.update(1, "Camiseta") shouldBe null
    }

    @Test
    fun `update update and save the product with slot and capture`() {
        val product = productStub()
        every { repositoryMock.findById(1) } returns Optional.of(product)

        val saved = productStub(1, "name")
        val slot = slot<Product>()
        every { repositoryMock.save(capture(slot)) } returns saved

        service.update(1, "name") shouldBe saved
        slot.isCaptured shouldBe true
        slot.captured.name shouldBe "name"
    }

    @Test
    fun `update update and save the product with answers`() {
        every { repositoryMock.findById(1) } returns Optional.of(productStub())
        every { repositoryMock.save(any()) } answers { firstArg() }

        val saved = service.update(1, "name")!!
        saved.name shouldBe "name"
    }

    @Test
    fun `findAll should request an ascending list if SortDir ASC is used`() {
        val sortDir = SortDir.ASC
        val productList = listOf(productStub(1), productStub(2), productStub(3))
        every { repositoryMock.findAll(Sort.by("name").ascending()) } returns productList
        service.findAll(sortDir) shouldBe productList
    }

    @Test
    fun `findAll should request an descending list if SortDir DESC is used`() {
        val sortDir = SortDir.DESC
        val productList = listOf(productStub(1), productStub(2), productStub(3))
        every { repositoryMock.findAll(Sort.by("name").descending()) } returns productList
        service.findAll(sortDir) shouldBe productList
    }

    @Test
    fun `findByIdOrNull should delegate to repository`() {
        val product = productStub()
        every { repositoryMock.findById(1) } returns Optional.of(product)
        service.findByIdOrNull(1) shouldBe product
    }

    @Test
    fun `delete must return false if the product does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        service.delete(1) shouldBe false
    }

    @Test
    fun `delete must call delete and return true if the product exists`() {
        val product = productStub()
        every { repositoryMock.findById(1) } returns Optional.of(product)
        justRun { repositoryMock.delete(product) }
        service.delete(1) shouldBe true
    }


}