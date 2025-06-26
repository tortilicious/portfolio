package org.mlc.gastosapi.controller

import org.mlc.gastosapi.dto.gasto.PeticionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto
import org.mlc.gastosapi.security.CustomUserDetails
import org.mlc.gastosapi.service.GastoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * Controlador para gestionar todas las operaciones relacionadas con los Gastos
 * dentro de un grupo específico. Todos los endpoints están anidados bajo la ruta
 * de un grupo y requieren autenticación por token JWT.
 *
 * @property gastoService El servicio que contiene la lógica de negocio para los gastos.
 */
@RestController
@RequestMapping("/api/grupos/{idGrupo}/gastos")
class GastoController(
    private val gastoService: GastoService
) {

    // =================================================================================
    // CREATE
    // =================================================================================

    /**
     * Endpoint para crear un nuevo gasto dentro de un grupo específico.
     *
     * @param customUserDetails El usuario autenticado que realiza la petición.
     * @param idGrupo El ID del grupo donde se creará el gasto (de la URL).
     * @param peticion DTO con los detalles del nuevo gasto (descripción, monto, división).
     * @return Una respuesta 201 Created con los datos del gasto recién creado.
     */
    @PostMapping
    fun nuevoGasto(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long,
        @RequestBody peticion: PeticionGasto
    ): ResponseEntity<RespuestaGasto> {
        val nuevoGasto = gastoService.crearGasto(idGrupo, customUserDetails.usuario.id, peticion)
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGasto)
    }

    // =================================================================================
    // READ
    // =================================================================================

    /**
     * Endpoint para obtener una lista con todos los gastos de un grupo.
     *
     * @param customUserDetails El usuario autenticado.
     * @param idGrupo El ID del grupo cuyos gastos se quieren obtener.
     * @return Una respuesta 200 OK con la lista de gastos del grupo.
     */
    @GetMapping
    fun obtenerGastosGrupo(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long
    ): ResponseEntity<List<RespuestaGasto>> {
        val gastos = gastoService.obtenerGastosGrupo(idGrupo, customUserDetails.usuario.id)
        return ResponseEntity.ok(gastos)
    }

    /**
     * Endpoint para obtener los detalles de un gasto específico por su ID.
     *
     * @param customUserDetails El usuario autenticado.
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idGasto El ID del gasto a obtener.
     * @return Una respuesta 200 OK con los datos del gasto.
     */
    @GetMapping("/{idGasto}")
    fun obtenerGastoPorId(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long,
        @PathVariable idGasto: Long
    ): ResponseEntity<RespuestaGasto> {
        val gasto = gastoService.obtenerGasto(idGrupo, customUserDetails.usuario.id, idGasto)
        return ResponseEntity.ok(gasto)
    }

    // =================================================================================
    // DELETE
    // =================================================================================

    /**
     * Endpoint para eliminar un gasto específico.
     * El usuario debe ser el pagador del gasto o un administrador del grupo para poder eliminarlo.
     *
     * @param customUserDetails El usuario autenticado.
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idGasto El ID del gasto a eliminar.
     * @return Una respuesta 204 No Content si la eliminación fue exitosa.
     */
    @DeleteMapping("/{idGasto}")
    fun eliminarGasto(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable idGrupo: Long,
        @PathVariable idGasto: Long
    ): ResponseEntity<Unit> {
        gastoService.eliminarGasto(idGrupo, customUserDetails.usuario.id, idGasto)
        return ResponseEntity.noContent().build()
    }
}