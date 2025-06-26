package org.mlc.gastosapi.controller

import org.mlc.gastosapi.dto.grupo.PeticionNuevoRol
import org.mlc.gastosapi.dto.grupo.PeticionAgregarMiembro
import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.RespuestaGrupo
import org.mlc.gastosapi.dto.saldo.RespuestaSaldoGrupo
import org.mlc.gastosapi.model.RolGrupo
import org.mlc.gastosapi.security.CustomUserDetails
import org.mlc.gastosapi.service.GrupoService
import org.mlc.gastosapi.service.SaldoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

// --- DTOs necesarios para las peticiones ---

data class PeticionAgregarMiembro(
    val email: String
)

data class PeticionActualizarRol(
    val rol: RolGrupo
)


/**
 * Controlador para gestionar todas las operaciones relacionadas con los Grupos.
 * Todos los endpoints en este controlador están protegidos y requieren un token JWT válido.
 */
@RestController
@RequestMapping("/api/grupos")
class GrupoController(
    private val grupoService: GrupoService,
    private val saldoService: SaldoService
) {

    // =================================================================================
    // CREATE (Crear Recursos)
    // =================================================================================

    @PostMapping
    fun crearGrupo(
        @RequestBody peticion: PeticionGrupo,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<RespuestaGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val grupoCreado = grupoService.crearGrupo(idUsuarioActual, peticion)
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoCreado)
    }

    @PostMapping("/{idGrupo}/miembros")
    fun agregarMiembro(
        @PathVariable idGrupo: Long,
        @RequestBody peticion: PeticionAgregarMiembro,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<RespuestaGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val grupoActualizado = grupoService.agregarMiembro(idGrupo, idUsuarioActual, peticion.email)
        return ResponseEntity.ok(grupoActualizado)
    }

    // =================================================================================
    // READ (Leer Recursos)
    // =================================================================================

    @GetMapping
    fun obtenerListadoGrupos(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<List<RespuestaGrupo>> {
        val idUsuarioActual = customUserDetails.usuario.id
        val listaGrupos = grupoService.obtenerGruposUsuario(idUsuarioActual)
        // Correcto: Usar 200 OK para una consulta exitosa.
        return ResponseEntity.ok(listaGrupos)
    }

    @GetMapping("/{idGrupo}")
    fun obtenerGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<RespuestaGrupo> {
        val grupo = grupoService.obtenerGrupo(customUserDetails.usuario.id, idGrupo)
        return ResponseEntity.ok(grupo)
    }

    @GetMapping("/{idGrupo}/saldos")
    fun obtenerSaldosGrupo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<RespuestaSaldoGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val saldos = saldoService.obtenerSaldosDeGrupo(idGrupo, idUsuarioActual)
        return ResponseEntity.ok(saldos)
    }

    // =================================================================================
    // UPDATE (Actualizar Recursos)
    // =================================================================================

    @PutMapping("/{idGrupo}/miembros/{idUsuarioAActualizar}")
    fun modificarRolUsuarioGrupo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long,
        @PathVariable idUsuarioAActualizar: Long,
        @RequestBody peticion: PeticionActualizarRol
    ): ResponseEntity<RespuestaGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val grupoActualizado = grupoService.actualizarRolMiembro(
            idGrupo = idGrupo,
            idUsuarioActual = idUsuarioActual,
            idUsuarioAActualizar = idUsuarioAActualizar,
            nuevoRol = peticion.rol
        )
        return ResponseEntity.ok(grupoActualizado)
    }

    // =================================================================================
    // DELETE (Eliminar Recursos)
    // =================================================================================

    @DeleteMapping("/{idGrupo}")
    fun eliminarGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<Unit> {
        grupoService.eliminarGrupo(idGrupo, customUserDetails.usuario.id)
        // Correcto: Usar 204 No Content para una eliminación exitosa sin devolver contenido.
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{idGrupo}/miembros/{idUsuarioAEliminar}")
    fun eliminarMiembro(
        @PathVariable idGrupo: Long,
        @PathVariable idUsuarioAEliminar: Long,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<Unit> {
        val idUsuarioActual = customUserDetails.usuario.id
        grupoService.eliminarMiembro(idGrupo, idUsuarioActual, idUsuarioAEliminar)
        return ResponseEntity.noContent().build()
    }
}
