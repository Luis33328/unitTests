package br.pucpr.authserver.users

import br.pucpr.authserver.roles.Role
import br.pucpr.authserver.security.Jwt.Companion.createAuthentication
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.portfolio.Portfolio

object Stubs {
    fun userStub(
        id: Long? = 1,
        name: String = "user",
        password: String = "Str4ngP@ss!",
        email: String? = "user@email.com",
        roles: List<String> = listOf()
    ) = User(
        id = id,
        email = email ?: "$name@email.com",
        name = name,
        password = password,
        roles = roles
            .mapIndexed { i, it -> Role(i.toLong(), it, "$it role") }
            .toMutableSet()
    )

    fun adminStub() = userStub(
        id = 1000,
        email = "admin@authserver.com",
        password = "admin",
        name = "Auth Server Administrator",
        roles = listOf("ADMIN")
    )

    fun roleStub(
        id: Long? = 1,
        name: String = "USER",
        description: String = "Role description"
    ) = Role(id = id, name = name, description = description)

    fun authStub(
        user: User,
    ) = createAuthentication(UserToken(user))

    fun productStub(

            id: Long? = 1,
            name: String = "Camiseta",
            price: String = "35,00",
            image: String = "image",
            description: String = "Uma camiseta..."
    ) = Product(
            id = id,
            name = name,
            price = price,
            image = image,
            description = description
    )


    fun portfolioStub(

            id: Long? = 1,
            title: String = "Obra",
            description: String = "Obra descrição",
            image: String = "image"
    ) = Portfolio(
            id = id,
            title = title,
            description = description,
            image = image
    )
}