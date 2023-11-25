package br.pucpr.authserver.products


import br.pucpr.authserver.roles.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import jakarta.persistence.Transient

@Entity
@Table(name = "TblProduct")
class Product(
    @Id @GeneratedValue
    var id: Long? = null,

    var name: String = "",

    var price: String = "",

    var image: String = "",

    var description: String = "",


)