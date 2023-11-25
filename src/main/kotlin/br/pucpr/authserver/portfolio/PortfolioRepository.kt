package br.pucpr.authserver.portfolio

import br.pucpr.authserver.products.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortfolioRepository : JpaRepository<Portfolio, Long> {

    fun findByTitle(title: String): Portfolio?
}