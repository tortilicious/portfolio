// --- Entidad para la Lista de la Compra ---
// data/local/entity/ShoppingListItemEntity.kt
package com.example.cookhelpapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_items")
data class ShoppingListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ingredientName: String,
    val amount: Double?,
    val unit: String?,
    var isChecked: Boolean = false
)