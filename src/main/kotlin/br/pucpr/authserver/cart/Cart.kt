package br.pucpr.authserver.cart


import br.pucpr.authserver.products.Product
import br.pucpr.authserver.users.User
import jakarta.persistence.*

@Entity
@Table(name = "Cart")
class Cart(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @OneToOne
        var product: Product,

        @OneToOne
        var user: User,

        var priceProduct: String = ""


)

