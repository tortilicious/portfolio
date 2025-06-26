package org.mlc.gastosapi.utils

import org.mlc.gastosapi.dto.division_gasto.RespuestaDivisionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaMiembroGrupo
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.*

/**
 * Este fichero contiene funciones de extensión para mapear las entidades de JPA
 * a sus correspondientes DTOs (Data Transfer Objects).
 * Esto permite desacoplar el modelo de datos interno de la representación
 * que se expone en la API, asegurando que no se filtren datos sensibles
 * o innecesarios.
 */

/**
 * Convierte una entidad [Usuario] a su DTO [RespuestaUsuario].
 * Expone solo la información pública del usuario.
 *
 * @return Un objeto [RespuestaUsuario] con el id, nombre y email.
 */
fun Usuario.dto(): RespuestaUsuario {
    return RespuestaUsuario(
        id = id,
        nombre = nombre,
        email = email
    )
}

/**
 * Convierte una entidad [Membresia] a su DTO [RespuestaMiembroGrupo].
 * Muestra el usuario y su rol dentro del grupo.
 *
 * @return Un objeto [RespuestaMiembroGrupo] con los datos del usuario y su rol.
 */
fun Membresia.dto(): RespuestaMiembroGrupo {
    return RespuestaMiembroGrupo(
        usuario = usuario.dto(),
        rol = rol
    )
}

/**
 * Convierte una entidad [Grupo] a su DTO [RespuestaGrupo].
 * Incluye la información del creador y una lista de todos sus miembros,
 * ordenada por rol para mayor claridad.
 *
 * @return Un objeto [RespuestaGrupo] con todos los detalles del grupo.
 */
fun Grupo.dto(): RespuestaGrupo {
    return RespuestaGrupo(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        creador = creador.dto(),
        miembros = miembros.map { it.dto() }.sortedBy { it.rol }
    )
}

/**
 * Convierte una entidad [Gasto] a su DTO [RespuestaGasto].
 * Incluye los detalles del gasto, quién lo pagó y cómo se dividió.
 *
 * @return Un objeto [RespuestaGasto] con todos los detalles del gasto.
 */
fun Gasto.dto(): RespuestaGasto {
    return RespuestaGasto(
        id = id,
        descripcion = descripcion,
        monto = monto,
        fecha = fecha,
        pagador = pagador.dto(),
        divisionGasto = divisiones.map { it.dto() }
    )
}

/**
 * Convierte una entidad [DivisionGasto] a su DTO [RespuestaDivisionGasto].
 * Muestra quién es el deudor y la cantidad que le corresponde pagar.
 *
 * @return Un objeto [RespuestaDivisionGasto] con los detalles de la división.
 */
fun DivisionGasto.dto(): RespuestaDivisionGasto {
    return RespuestaDivisionGasto(
        deudor = deudor.dto(),
        montoAdeudado = cantidadDebida
    )
}
