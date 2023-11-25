package br.pucpr.authserver.portfolio

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.SortDir
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PortfolioService(
        val repository: PortfolioRepository
) {
    fun insert(portfolio: Portfolio): Portfolio {

        return repository.save(portfolio)
            .also { log.info("Product inserted: {}", it.id) }
    }

    fun update(id: Long, name: String): Portfolio? {
        val portfolio = findByIdOrThrow(id)
        if (portfolio.title == name) return null
        portfolio.title = name
        return repository.save(portfolio)
    }

    fun findAll(dir: SortDir = SortDir.ASC): List<Portfolio> = when (dir) {
        SortDir.ASC -> repository.findAll(Sort.by("title").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("title").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findById(id).getOrNull()
    private fun findByIdOrThrow(id: Long) =
        findByIdOrNull(id) ?: throw NotFoundException(id)

    fun delete(id: Long): Boolean {
        val portfolio = findByIdOrNull(id) ?: return false
        repository.delete(portfolio)
        log.info("Product deleted: {}", portfolio.id)
        return true
    }




    companion object {
        private val log = LoggerFactory.getLogger(PortfolioService::class.java)
    }
}