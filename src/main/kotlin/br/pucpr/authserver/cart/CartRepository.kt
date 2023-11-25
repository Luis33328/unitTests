package br.pucpr.authserver.cart


import br.pucpr.authserver.products.Product
import br.pucpr.authserver.users.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<Cart, Long> {

    //fun findByName(name: String): Product?

    fun findByUser(user: User?): List<Cart?>?

    fun findByUserAndProduct(user: User?, product: Product?): Cart?

}