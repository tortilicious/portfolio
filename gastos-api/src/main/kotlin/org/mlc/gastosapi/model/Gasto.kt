package org.mlc.gastosapi.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Representa un gasto individual realizado dentro de un grupo.
 *
 * @property id El identificador único del gasto, generado por la base de datos.
 * @property descripcion Una descripción del gasto (ej: "Cena en pizzería").
 * @property monto El importe total del gasto.
 * @property fecha La fecha y hora en que se registró el gasto. Por defecto, es el momento de la creación.
 * @property grupo El [Grupo] al que pertenece este gasto. La relación es de carga perezosa (LAZY).
 * @property pagador El [Usuario] que pagó el gasto. La relación es de carga perezosa (LAZY).
 * @property divisiones La lista de cómo se divide este gasto entre los deudores.
 */
@Entity
@Table(name = "gastos")
data class Gasto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var descripcion: String,

    @Column(nullable = false)
    var monto: BigDecimal,

    @Column(nullable = false)
    val fecha: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    val grupo: Grupo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagador_id", nullable = false)
    val pagador: Usuario,

    @OneToMany(
        mappedBy = "gasto",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val divisiones: MutableList<DivisionGasto> = mutableListOf()
) {
    /**
     * Método de ayuda para añadir una división al gasto.
     * Encapsula la lógica de añadir un elemento a la lista de divisiones.
     *
     * @param division La [DivisionGasto] a añadir.
     */
    fun agregarDivisionGasto(division: DivisionGasto) {
        divisiones.add(division)
    }
}