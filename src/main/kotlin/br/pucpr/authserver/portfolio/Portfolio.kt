package br.pucpr.authserver.portfolio


import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "TblProduct")
class Portfolio(
    @Id @GeneratedValue
    var id: Long? = null,

    var title: String = "",

    var description: String = "",

    var image: String = "",



)