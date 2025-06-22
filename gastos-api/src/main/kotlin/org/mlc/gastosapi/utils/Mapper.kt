package org.mlc.gastosapi.utils

import org.mlc.gastosapi.dto.division_gasto.RespuestaDivisionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaMiembroGrupo
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.*

fun Usuario.dto(): RespuestaUsuario {
    return RespuestaUsuario(
        id = id,
        nombre = nombre,
        email = email
    )
}

fun Membresia.dto(): RespuestaMiembroGrupo {
    return RespuestaMiembroGrupo(
        usuario = usuario.dto(),
        rol = rol
    )
}

fun Grupo.dto(): RespuestaGrupo {
    return RespuestaGrupo(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        creador = creador.dto(),
        miembros = miembros.map { it.dto() }.sortedBy { it.rol }
    )
}

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

fun DivisionGasto.dto(): RespuestaDivisionGasto {
    return RespuestaDivisionGasto(
        deudor = deudor.dto(),
        montoAdeudado = cantidadDebida
    )
}