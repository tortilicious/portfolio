// data/local/dao/ShoppingListDao.kt
package com.example.cookhelpapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cookhelpapp.data.local.entity.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    /**
     * Obtiene todos los items de la lista de la compra como un Flow.
     * Se ordenan por nombre y luego por si están marcados.
     */
    @Query("SELECT * FROM shopping_list_items ORDER BY isChecked ASC, ingredientName ASC")
    fun getAllItemsStream(): Flow<List<ShoppingListItemEntity>>

    /**
     * Inserta un nuevo item en la lista. Si ya existe un item con el mismo nombre
     * (considerando case-insensitivity para evitar duplicados simples), no lo inserta.
     * Esta lógica de "no duplicar" es básica. Una más avanzada podría sumar cantidades.
     * Por ahora, nos enfocaremos en evitar duplicados por nombre.
     *
     * Es mejor manejar la lógica de "no duplicar o sumar" en el UseCase.
     * Aquí, simplemente insertamos o reemplazamos si el ID es el mismo.
     * La unicidad por nombre se manejará en el UseCase.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE) // O OnConflictStrategy.REPLACE si quieres actualizar si el ID es el mismo
    suspend fun insertItem(item: ShoppingListItemEntity): Long // Devuelve el rowId o -1 si IGNORE y conflicto

    /**
     * Inserta una lista de items.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllItems(items: List<ShoppingListItemEntity>)


    /**
     * Obtiene un item por su nombre (case-insensitive).
     * Útil para comprobar si un ingrediente ya existe.
     */
    @Query("SELECT * FROM shopping_list_items WHERE LOWER(ingredientName) = LOWER(:name) LIMIT 1")
    suspend fun getItemByName(name: String): ShoppingListItemEntity?

    /**
     * Actualiza un item existente (ej. para marcar/desmarcar).
     */
    @Update
    suspend fun updateItem(item: ShoppingListItemEntity)

    /**
     * Elimina un item por su ID.
     */
    @Query("DELETE FROM shopping_list_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)

    /**
     * Elimina todos los items que están marcados (isChecked = true).
     */
    @Query("DELETE FROM shopping_list_items WHERE isChecked = 1")
    suspend fun deleteCheckedItems()

    /**
     * Elimina todos los items de la lista.
     */
    @Query("DELETE FROM shopping_list_items")
    suspend fun clearAllItems()
}