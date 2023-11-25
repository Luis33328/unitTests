package br.pucpr.authserver.products

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.portfolio.PortfolioService

import br.pucpr.authserver.users.SortDir
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(
        val repository: ProductRepository
) {
    fun insert(product: Product): Product {

        return repository.save(product)
            .also { log.info("Product inserted: {}", it.id) }
    }

    fun update(id: Long, name: String): Product? {
        val product = findByIdOrThrow(id)
        if (product.name == name) return null
        product.name = name
        return repository.save(product)
    }

    fun findAll(dir: SortDir = SortDir.ASC): List<Product> = when (dir) {
        SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
        SortDir.DESC -> repository.findAll(Sort.by("name").descending())
    }

    fun findByIdOrNull(id: Long) = repository.findById(id).getOrNull()
    private fun findByIdOrThrow(id: Long) =
        findByIdOrNull(id) ?: throw NotFoundException(id)

    fun delete(id: Long): Boolean {
        val product = findByIdOrNull(id) ?: return false
        repository.delete(product)
        log.info("Product deleted: {}", product.id)
        return true
    }




    companion object {
        private val log = LoggerFactory.getLogger(ProductService::class.java)
    }
}