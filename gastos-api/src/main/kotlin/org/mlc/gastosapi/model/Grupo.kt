package org.mlc.gastosapi.model

import jakarta.persistence.*

/**
 * Representa un grupo de usuarios para compartir gastos.
 * La gestión de los miembros y sus roles se realiza a través de la entidad [PertenenciaGrupo].
 *
 * @property id El identificador único del grupo.
 * @property nombre El nombre del grupo.
 * @property descripcion Una descripción opcional para el grupo.
 * @property creador El usuario que creó originalmente el grupo. Este dato es inmutable.
 * @property pertenencias El conjunto de relaciones de pertenencia que define qué usuarios son miembros y qué rol tienen.
 */
@Entity
@Table(name = "grupos")
data class Grupo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var nombre: String,

    var descripcion: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    val creador: Usuario,

    @OneToMany(mappedBy = "grupo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val pertenencias: MutableSet<PertenenciaGrupo> = mutableSetOf()
)