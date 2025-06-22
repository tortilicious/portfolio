package org.mlc.gastosapi.utils

import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaMiembroGrupo
import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.Grupo
import org.mlc.gastosapi.model.Membresia
import org.mlc.gastosapi.model.Usuario

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