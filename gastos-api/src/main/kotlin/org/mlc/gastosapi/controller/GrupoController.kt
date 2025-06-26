package org.mlc.gastosapi.controller

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

/**
 * DTO para la petición de añadir un nuevo miembro a un grupo.
 * @property email El email del usuario que se quiere añadir.
 */
data class PeticionAgregarMiembro(
    val email: String
)

/**
 * DTO para la petición de actualizar el rol de un miembro.
 * @property rol El nuevo rol que se le asignará al miembro.
 */
data class PeticionActualizarRol(
    val rol: RolGrupo
)

/**
 * Controlador para gestionar todas las operaciones relacionadas con los Grupos.
 * Todos los endpoints en este controlador están protegidos y requieren un token JWT válido.
 *
 * @property grupoService El servicio que contiene la lógica de negocio para los grupos.
 * @property saldoService El servicio que contiene la lógica para el cálculo de saldos.
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

    /**
     * Endpoint para crear un nuevo grupo. El usuario que realiza la petición
     * se convierte automáticamente en el creador y primer administrador del grupo.
     *
     * @param peticion DTO con el nombre y descripción del nuevo grupo.
     * @param customUserDetails Objeto del usuario autenticado, inyectado por Spring Security.
     * @return Una respuesta 201 Created con los datos del grupo recién creado.
     */
    @PostMapping
    fun crearGrupo(
        @RequestBody peticion: PeticionGrupo,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<RespuestaGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val grupoCreado = grupoService.crearGrupo(idUsuarioActual, peticion)
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoCreado)
    }

    /**
     * Endpoint para añadir un nuevo miembro a un grupo existente.
     * Requiere que el usuario que realiza la petición sea administrador del grupo.
     *
     * @param idGrupo El ID del grupo al que se añadirá el miembro.
     * @param peticion DTO que contiene el email del usuario a añadir.
     * @param customUserDetails Objeto del usuario autenticado.
     * @return Una respuesta 200 OK con los datos del grupo actualizado.
     */
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
    // READ
    // =================================================================================

    /**
     * Endpoint para obtener la lista de todos los grupos a los que pertenece el usuario autenticado.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @return Una respuesta 200 OK con la lista de grupos.
     */
    @GetMapping
    fun obtenerListadoGrupos(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<List<RespuestaGrupo>> {
        val idUsuarioActual = customUserDetails.usuario.id
        val listaGrupos = grupoService.obtenerGruposUsuario(idUsuarioActual)
        return ResponseEntity.ok(listaGrupos)
    }

    /**
     * Endpoint para obtener los detalles de un único grupo por su ID.
     * El usuario debe ser miembro del grupo para poder acceder a la información.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @param idGrupo El ID del grupo a obtener, extraído de la URL.
     * @return Una respuesta 200 OK con los datos del grupo.
     */
    @GetMapping("/{idGrupo}")
    fun obtenerGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<RespuestaGrupo> {
        val grupo = grupoService.obtenerGrupo(customUserDetails.usuario.id, idGrupo)
        return ResponseEntity.ok(grupo)
    }

    /**
     * Endpoint para obtener los saldos calculados de un grupo específico.
     * Muestra quién debe dinero y a quién dentro del grupo.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @param idGrupo El ID del grupo para el que se calcularán los saldos.
     * @return Una respuesta 200 OK con el objeto de saldos del grupo.
     */
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
    // UPDATE
    // =================================================================================

    /**
     * Endpoint para actualizar el rol de un miembro dentro de un grupo.
     * Requiere que el usuario que realiza la petición sea administrador del grupo.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @param idGrupo El ID del grupo donde se realizará el cambio.
     * @param idUsuarioAActualizar El ID del usuario cuyo rol será modificado.
     * @param peticion DTO con el nuevo rol a asignar.
     * @return Una respuesta 200 OK con los datos del grupo actualizado.
     */
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
    // DELETE
    // =================================================================================

    /**
     * Endpoint para eliminar un grupo completo.
     * Solo el creador original del grupo tiene permiso para realizar esta acción.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @param idGrupo El ID del grupo a eliminar.
     * @return Una respuesta 204 No Content si la operación fue exitosa.
     */
    @DeleteMapping("/{idGrupo}")
    fun eliminarGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<Unit> {
        grupoService.eliminarGrupo(idGrupo, customUserDetails.usuario.id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Endpoint para eliminar a un miembro de un grupo.
     * Requiere que el usuario que realiza la petición sea administrador del grupo.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioAEliminar El ID del usuario que será eliminado del grupo.
     * @param customUserDetails Objeto del usuario autenticado.
     * @return Una respuesta 204 No Content si la operación fue exitosa.
     */
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