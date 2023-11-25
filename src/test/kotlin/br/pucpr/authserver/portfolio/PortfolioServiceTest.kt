package br.pucpr.authserver.users

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.portfolio.PortfolioRepository
import br.pucpr.authserver.portfolio.PortfolioService
import br.pucpr.authserver.portfolio.Portfolio
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.users.Stubs.roleStub
import br.pucpr.authserver.users.Stubs.userStub
import br.pucpr.authserver.users.Stubs.productStub
import br.pucpr.authserver.users.Stubs.portfolioStub
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

internal class PortfolioServiceTest {
    private val repositoryMock = mockk<PortfolioRepository>()
    private val jwt = mockk<Jwt>()

    private val service = PortfolioService(repositoryMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(repositoryMock, jwt)
    }

    @Test
    fun `insert must throw BadRequestException if a portfolio with the same title is found`() {
        val Portfolio = portfolioStub(id = null)
        every { repositoryMock.findByTitle(Portfolio.title) } returns portfolioStub()
        assertThrows<BadRequestException> {
            service.insert(Portfolio)
        } shouldHaveMessage "Portfolio already exists"
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
    fun `update must throw NotFoundException if the portfolio does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        assertThrows<NotFoundException> {
            service.update(1, "title")
        }
    }

    @Test
    fun `update must return null if there's no changes`() {
        val portfolio = portfolioStub()
        every { repositoryMock.findById(1) } returns Optional.of(portfolio)
        service.update(1, "Obra" +
                "" +
                "") shouldBe null
    }

    @Test
    fun `update update and save the portfolio with slot and capture`() {
        val portfolio = portfolioStub()
        every { repositoryMock.findById(1) } returns Optional.of(portfolio)

        val saved = portfolioStub(1, "title")
        val slot = slot<Portfolio>()
        every { repositoryMock.save(capture(slot)) } returns saved

        service.update(1, "title") shouldBe saved
        slot.isCaptured shouldBe true
        slot.captured.title shouldBe "title"
    }

    @Test
    fun `update update and save the portfolio with answers`() {
        every { repositoryMock.findById(1) } returns Optional.of(portfolioStub())
        every { repositoryMock.save(any()) } answers { firstArg() }

        val saved = service.update(1, "title")!!
        saved.title shouldBe "title"
    }

    @Test
    fun `findAll should request an ascending list if SortDir ASC is used`() {
        val sortDir = SortDir.ASC
        val portfolioList = listOf(portfolioStub(1), portfolioStub(2), portfolioStub(3))
        every { repositoryMock.findAll(Sort.by("title").ascending()) } returns portfolioList
        service.findAll(sortDir) shouldBe portfolioList
    }

    @Test
    fun `findAll should request an descending list if SortDir DESC is used`() {
        val sortDir = SortDir.DESC
        val portfolioList = listOf(portfolioStub(1), portfolioStub(2), portfolioStub(3))
        every { repositoryMock.findAll(Sort.by("title").descending()) } returns portfolioList
        service.findAll(sortDir) shouldBe portfolioList
    }

    @Test
    fun `findByIdOrNull should delegate to repository`() {
        val portfolio = portfolioStub()
        every { repositoryMock.findById(1) } returns Optional.of(portfolio)
        service.findByIdOrNull(1) shouldBe portfolio
    }

    @Test
    fun `delete must return false if the product does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        service.delete(1) shouldBe false
    }

    @Test
    fun `delete must call delete and return true if the product exists`() {
        val portfolio = portfolioStub()
        every { repositoryMock.findById(1) } returns Optional.of(portfolio)
        justRun { repositoryMock.delete(portfolio) }
        service.delete(1) shouldBe true
    }


}