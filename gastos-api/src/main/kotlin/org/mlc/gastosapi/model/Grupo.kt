package org.mlc.gastosapi.model

import jakarta.persistence.*

/**
 * Representa un grupo de usuarios para compartir gastos.
 * Contiene la lógica para gestionar la relación bidireccional con sus miembros.
 *
 * @property id El identificador único del grupo, generado por la base de datos.
 * @property nombre El nombre del grupo
 * @property descripcion Una descripción opcional para el grupo.
 * @property creador El usuario que creó el grupo.
 * @property miembros El conjunto de usuarios que son miembros de este grupo.
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

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "grupo_miembros",
        joinColumns = [JoinColumn(name = "grupo_id")],
        inverseJoinColumns = [JoinColumn(name = "usuario_id")]
    )
    val miembros: MutableSet<Usuario> = mutableSetOf()
) {
    /**
     * Método de ayuda para añadir un miembro al grupo.
     * Sincroniza ambos lados de la relación bidireccional para mantener la consistencia
     * del grafo de objetos en memoria.
     *
     * @param miembro El [Usuario] a añadir al grupo.
     */
    fun agregarMiembro(miembro: Usuario) {
        miembros.add(miembro)
        miembro.grupos.add(this)
    }

    /**
     * Método de ayuda para eliminar un miembro del grupo.
     * Sincroniza ambos lados de la relación bidireccional para mantener la consistencia
     * del grafo de objetos en memoria.
     *
     * @param miembro El [Usuario] a eliminar del grupo.
     */
    fun eliminarMiembro(miembro: Usuario) {
        miembros.remove(miembro)
        miembro.grupos.remove(this)
    }
}