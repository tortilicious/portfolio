package org.mlc.gastosapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.mlc.gastosapi.dto.grupo.PeticionAgregarMiembro
import org.mlc.gastosapi.dto.grupo.PeticionGrupo
import org.mlc.gastosapi.dto.grupo.PeticionNuevoRol
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

/**
 * Controlador para gestionar todas las operaciones relacionadas con los Grupos.
 * Todos los endpoints en este controlador están protegidos y requieren un token JWT válido.
 *
 * @property grupoService El servicio que contiene la lógica de negocio para los grupos.
 * @property saldoService El servicio que contiene la lógica para el cálculo de saldos.
 */
@RestController
@RequestMapping("/api/grupos")
@SecurityRequirement(name = "BearerAuth") // Aplica el requisito de seguridad a todo el controlador.
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
    @Operation(summary = "Crea un nuevo grupo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Grupo creado exitosamente."),
        ApiResponse(responseCode = "403", description = "No autorizado (token inválido o ausente).")
    ])
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
    @Operation(summary = "Añade un nuevo miembro a un grupo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Miembro añadido exitosamente."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres administrador del grupo)."),
        ApiResponse(responseCode = "404", description = "El grupo o el usuario a añadir no existen.")
    ])
    @PostMapping("/{idGrupo}/miembros")
    fun agregarMiembro(
        @Parameter(description = "ID del grupo al que se añadirá el miembro.") @PathVariable idGrupo: Long,
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

    /**
     * Endpoint para obtener la lista de todos los grupos a los que pertenece el usuario autenticado.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @return Una respuesta 200 OK con la lista de grupos.
     */
    @Operation(summary = "Obtiene la lista de grupos del usuario")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de grupos obtenida."),
        ApiResponse(responseCode = "403", description = "No autorizado.")
    ])
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
    @Operation(summary = "Obtiene los detalles de un grupo específico")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Datos del grupo obtenidos."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres miembro del grupo)."),
        ApiResponse(responseCode = "404", description = "El grupo no existe.")
    ])
    @GetMapping("/{idGrupo}")
    fun obtenerGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Parameter(description = "ID del grupo a obtener.") @PathVariable idGrupo: Long
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
    @Operation(summary = "Obtiene los saldos de un grupo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Saldos del grupo calculados correctamente."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres miembro del grupo).")
    ])
    @GetMapping("/{idGrupo}/saldos")
    fun obtenerSaldosGrupo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Parameter(description = "ID del grupo para el que se calcularán los saldos.") @PathVariable idGrupo: Long
    ): ResponseEntity<RespuestaSaldoGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val saldos = saldoService.obtenerSaldosDeGrupo(idGrupo, idUsuarioActual)
        return ResponseEntity.ok(saldos)
    }

    // =================================================================================
    // UPDATE (Actualizar Recursos)
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
    @Operation(summary = "Actualiza el rol de un miembro del grupo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Rol actualizado correctamente."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres administrador)."),
        ApiResponse(responseCode = "404", description = "El grupo o el miembro no existen.")
    ])
    @PutMapping("/{idGrupo}/miembros/{idUsuarioAActualizar}")
    fun modificarRolUsuarioGrupo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Parameter(description = "ID del grupo.") @PathVariable idGrupo: Long,
        @Parameter(description = "ID del usuario cuyo rol se modificará.") @PathVariable idUsuarioAActualizar: Long,
        @RequestBody peticion: PeticionNuevoRol
    ): ResponseEntity<RespuestaGrupo> {
        val idUsuarioActual = customUserDetails.usuario.id
        val grupoActualizado = grupoService.actualizarRolMiembro(
            idGrupo = idGrupo,
            idUsuarioActual = idUsuarioActual,
            idUsuarioAActualizar = idUsuarioAActualizar,
            nuevoRol = peticion.nuevoRol
        )
        return ResponseEntity.ok(grupoActualizado)
    }

    // =================================================================================
    // DELETE (Eliminar Recursos)
    // =================================================================================

    /**
     * Endpoint para eliminar un grupo completo.
     * Solo el creador original del grupo tiene permiso para realizar esta acción.
     *
     * @param customUserDetails Objeto del usuario autenticado.
     * @param idGrupo El ID del grupo a eliminar.
     * @return Una respuesta 204 No Content si la operación fue exitosa.
     */
    @Operation(summary = "Elimina un grupo completo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Grupo eliminado exitosamente."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres el creador del grupo)."),
        ApiResponse(responseCode = "404", description = "El grupo no existe.")
    ])
    @DeleteMapping("/{idGrupo}")
    fun eliminarGrupoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @Parameter(description = "ID del grupo a eliminar.") @PathVariable idGrupo: Long
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
    @Operation(summary = "Elimina a un miembro de un grupo")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Miembro eliminado exitosamente."),
        ApiResponse(responseCode = "403", description = "Acceso denegado (no eres administrador)."),
        ApiResponse(responseCode = "404", description = "El grupo o el miembro no existen.")
    ])
    @DeleteMapping("/{idGrupo}/miembros/{idUsuarioAEliminar}")
    fun eliminarMiembro(
        @Parameter(description = "ID del grupo.") @PathVariable idGrupo: Long,
        @Parameter(description = "ID del usuario a eliminar del grupo.") @PathVariable idUsuarioAEliminar: Long,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<Unit> {
        val idUsuarioActual = customUserDetails.usuario.id
        grupoService.eliminarMiembro(idGrupo, idUsuarioActual, idUsuarioAEliminar)
        return ResponseEntity.noContent().build()
    }
}
