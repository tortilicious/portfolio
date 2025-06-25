package org.mlc.gastosapi.model

import jakarta.persistence.*


/**
 * Entidad que representa la relación entre un Usuario y un Grupo.
 * Actúa como la tabla intermedia "con datos extra", almacenando el rol del usuario en el grupo.
 *
 * @property id El identificador único de la pertenencia.
 * @property usuario El usuario que pertenece al grupo.
 * @property grupo El grupo al que el usuario pertenece.
 * @property rol El rol que el usuario desempeña en dicho grupo.
 */
@Entity
@Table(name = "membresia")
data class Membresia (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    val usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    val grupo: Grupo,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rol: RolGrupo
) {
    override fun hashCode(): Int {
        return id.hashCode()
    }
}