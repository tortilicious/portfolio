package org.mlc.gastosapi.model

import jakarta.persistence.*

/**
 * Representa a un usuario en el sistema.
 *
 * @property id El identificador único del usuario, generado por la base de datos.
 * @property nombre El nombre completo del usuario.
 * @property email La dirección de correo electrónico única del usuario. Se usará para el login.
 * @property passwordHash El hash de la contraseña del usuario.
 * @property grupos El conjunto de [Grupo]s a los que pertenece este usuario. La relación es gestionada por la entidad Grupo.
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

    @ManyToMany(mappedBy = "miembros")
    val grupos: MutableSet<Grupo> = mutableSetOf()
)
