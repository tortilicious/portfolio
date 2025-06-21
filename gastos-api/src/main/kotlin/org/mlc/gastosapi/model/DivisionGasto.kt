package org.mlc.gastosapi.model

import jakarta.persistence.*
import java.math.BigDecimal

/**
 * Representa la parte de un gasto que un deudor específico debe pagar.
 * Esta entidad es la clave para dividir un gasto entre múltiples usuarios.
 *
 * @property id El identificador único de esta división de gasto.
 * @property deudor El [Usuario] que debe esta parte del gasto.
 * @property gasto El [Gasto] al que pertenece esta división.
 * @property cantidadDebida El importe exacto que el deudor debe pagar para este gasto en concreto.
 */
@Entity
@Table(name = "divisiones_gasto")
data class DivisionGasto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deudor_id", nullable = false)
    val deudor: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasto_id", nullable = false)
    val gasto: Gasto,

    @Column(nullable = false)
    val cantidadDebida: BigDecimal
)
