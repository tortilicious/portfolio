package org.mlc.gastosapi.utils

import org.mlc.gastosapi.dto.usuario.RespuestaUsuario
import org.mlc.gastosapi.model.Usuario

fun Usuario.Dto(): RespuestaUsuario {
    return RespuestaUsuario(
        id = id,
        nombre = nombre,
        email = email
    )
}