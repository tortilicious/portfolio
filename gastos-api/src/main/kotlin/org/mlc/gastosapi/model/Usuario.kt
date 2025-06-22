package org.mlc.gastosapi.model

import jakarta.persistence.*

/**
 * Representa a un usuario en el sistema.
 *
 * @property id El identificador único del usuario.
 * @property nombre El nombre completo del usuario.
 * @property email La dirección de correo electrónico única del usuario.
 * @property passwordHash El hash de la contraseña del usuario.
 * @property pertenencias El conjunto de relaciones de pertenencia de este usuario a diferentes grupos.
 */
@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var nombre: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var passwordHash: String,

    @OneToMany(mappedBy = "usuario", cascade = [CascadeType.ALL], orphanRemoval = true)
    val pertenencias: MutableSet<PertenenciaGrupo> = mutableSetOf()
)
