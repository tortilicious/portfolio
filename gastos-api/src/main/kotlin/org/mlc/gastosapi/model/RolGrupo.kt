package org.mlc.gastosapi.model

/**
 * Define los roles que un usuario puede tener dentro de un grupo.
 */
enum class RolGrupo {
    /** El usuario tiene permisos administrativos sobre el grupo (ej: añadir/eliminar miembros). */
    ADMIN,

    /** El usuario es un miembro estándar del grupo (ej: puede añadir gastos). */
    MEMBER
}