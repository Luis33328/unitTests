package br.pucpr.authserver.cart

import br.pucpr.authserver.cart.CartRepository
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.products.ProductService
import br.pucpr.authserver.portfolio.PortfolioService
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull


@Service
class CartService(
        val repository: CartRepository,

        @Autowired
        private val productService: ProductService,

        @Autowired
        private val userService: UserService
) {
    /*fun insert(product: Product): Product {

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
    }*/

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
        private val log = LoggerFactory.getLogger(CartService::class.java)
    }





    /*public Carrinho addAoCarrinho(Integer guidBeat, String user, String price) {
		Beat beat = beatServico.buscarPorId(guidBeat);
		Usuario userGet = usuarioServico.buscarPorId(Integer.parseInt(price));
		if(beat != null && user != null ) {
			Carrinho cart = new Carrinho(beat, userGet, price);
			return carrinhoRepositorio.save(cart);
		}

		return null;
	}*/

    /*public Carrinho addAoCarrinho(Integer guidBeat, String user, String price) {
		Beat beat = beatServico.buscarPorId(guidBeat);
		Usuario userGet = usuarioServico.buscarPorId(Integer.parseInt(price));
		if(beat != null && user != null ) {
			Carrinho cart = new Carrinho(beat, userGet, price);
			return carrinhoRepositorio.save(cart);
		}

		return null;
	}*/
    fun addToCart(guidProduct: Long, cart: Cart?){
        val product: Product? = productService.findByIdOrNull(guidProduct)
        val user: User = cart!!.user
        val update: Cart? = repository.findByUserAndProduct(cart!!.user, product)
        if (product != null && user != null) {
            if (update == null) {
                val cart = Cart(1, product, user, cart!!.priceProduct)
                repository.save(cart)
            } else {
                update.priceProduct = cart!!.priceProduct
                repository.save(update)
            }
        } else null
    }

    fun getCart(user: User?): List<Cart?>? {
        return repository.findByUser(user)
    }
}