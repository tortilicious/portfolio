package org.mlc.gastosapi.controller

import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.model.Grupo
import org.mlc.gastosapi.repository.UsuarioRepository
import org.mlc.gastosapi.service.GrupoService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/grupos")
class GrupoController(
    private val grupoService: GrupoService,
    private val usuarioRepository: UsuarioRepository
) {

    @PostMapping
    fun crearGrupo(
        @RequestBody peticion: PeticionGrupo,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<RespuestaGrupo> {

        val email = userDetails.username
        val usuario = usuarioRepository.findByEmail(email)!!
        val grupo = grupoService.crearGrupo(
            idUsuario = usuario.id,
            peticion = peticion,
        )
        return ResponseEntity.ok().body(grupo)
    }
}