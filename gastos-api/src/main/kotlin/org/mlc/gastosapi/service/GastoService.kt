package org.mlc.gastosapi.service

import org.mlc.gastosapi.dto.gasto.PeticionGasto
import org.mlc.gastosapi.dto.gasto.RespuestaGasto

/**
 * Interfaz para el servicio que gestiona la lógica de negocio de los Gastos.
 */
interface GastoService {

    /**
     * Crea un nuevo gasto dentro de un grupo, validando los permisos y los datos.
     *
     * @param idGrupo El ID del grupo donde se crea el gasto.
     * @param idUsuarioActual El ID del usuario que está creando el gasto (para validación de permisos).
     * @param peticion El DTO con todos los detalles del gasto a crear.
     * @return El DTO del gasto recién creado y guardado.
     */
    fun crearGasto(idGrupo: Long, idUsuarioActual: Long, peticion: PeticionGasto): RespuestaGasto

    /**
     * Obtiene los detalles de un gasto específico, previa validación de permisos.
     *
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idUsuarioActual El ID del usuario que solicita la información.
     * @param idGasto El ID del gasto a obtener.
     * @return El DTO del gasto encontrado.
     */
    fun obtenerGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long): RespuestaGasto

    /**
     * Obtiene una lista con todos los gastos de un grupo específico.
     *
     * @param idGrupo El ID del grupo.
     * @param idUsuarioActual El ID del usuario que solicita la información.
     * @return Una lista de DTOs con los gastos del grupo.
     */
    fun obtenerGastosGrupo(idGrupo: Long, idUsuarioActual: Long): List<RespuestaGasto>

    /**
     * Elimina un gasto, previa validación de permisos.
     * La lógica de negocio podría permitir que solo el pagador o un admin del grupo lo elimine.
     *
     * @param idGrupo El ID del grupo al que pertenece el gasto.
     * @param idUsuarioActual El ID del usuario que realiza la acción.
     * @param idGasto El ID del gasto a eliminar.
     */
    fun eliminarGasto(idGrupo: Long, idUsuarioActual: Long, idGasto: Long)
}