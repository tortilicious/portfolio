// domain/usecase/AddIngredientsToShoppingListUseCase.kt
package com.example.cookhelpapp.domain.usecase

import android.util.Log
import com.example.cookhelpapp.data.local.entity.ShoppingListItemEntity
import com.example.cookhelpapp.domain.model.IngredientDetailed // Usa tu modelo existente
import com.example.cookhelpapp.domain.repository.RecipeRepository
import kotlin.Result

/**
 * Caso de Uso para añadir ingredientes de una receta a la lista de la compra.
 * Si un ingrediente ya existe y las unidades son compatibles, suma las cantidades.
 *
 * @property repository El repositorio de recetas (que también manejará la lista de la compra).
 */
class AddIngredientsToShoppingListUseCase(
    private val repository: RecipeRepository
) {
    /**
     * Añade los ingredientes a la lista de la compra.
     *
     * @param recipeIngredients La lista completa de ingredientes de la receta, usando [IngredientDetailed].
     * @param userOwnedIngredients Lista opcional de ingredientes que el usuario ya tiene (nombres en minúscula).
     * Solo se usa si se quiere evitar añadir estos.
     * @param recipeName Nombre opcional de la receta para asociar a los items.
     * @return Un [Result] indicando éxito o fracaso.
     */
    suspend operator fun invoke(
        recipeIngredients: List<IngredientDetailed>,
        userOwnedIngredients: List<String>,
        recipeName: String? = null
    ): Result<Unit> {
        return try {
            val itemsToInsert = mutableListOf<ShoppingListItemEntity>()
            val itemsToUpdate = mutableListOf<ShoppingListItemEntity>()

            for (ingredient in recipeIngredients) {
                val ingredientNameTrimmed = ingredient.name.trim()
                val ingredientNameLower = ingredientNameTrimmed.lowercase()

                // 1. Si se proporcionan ingredientes del usuario, no añadir si ya lo tiene.
                if (userOwnedIngredients.contains(ingredientNameLower)) {
                    continue
                }

                // 2. Comprobar si el ingrediente (por nombre) ya existe en la lista de la compra actual.
                val existingItem = repository.getShoppingListItemByName(ingredientNameLower)

                if (existingItem != null) {
                    // El ingrediente ya está en la lista. Intentar sumar cantidades.
                    val newAmount = ingredient.amount
                    val newUnit = ingredient.unit?.trim()?.lowercase()
                    val existingAmount = existingItem.amount
                    val existingUnit = existingItem.unit?.trim()?.lowercase()

                    // Solo sumar si ambas cantidades están presentes y las unidades son iguales (y no nulas/vacías).
                    if (newAmount != null && existingAmount != null &&
                        !newUnit.isNullOrBlank() && newUnit == existingUnit
                    ) {
                        val totalAmount = existingAmount + newAmount
                        itemsToUpdate.add(
                            existingItem.copy(
                                amount = totalAmount
                            )
                        )
                    } else {
                        continue
                    }
                } else {
                    // 3. Si no es un ingrediente del usuario y no está en la lista, prepararlo para añadir.
                    itemsToInsert.add(
                        ShoppingListItemEntity(
                            ingredientName = ingredientNameTrimmed,
                            amount = ingredient.amount,
                            unit = ingredient.unit,
                            isChecked = false
                        )
                    )
                }
            }

            // Realizar operaciones en la base de datos
            if (itemsToUpdate.isNotEmpty()) {
                itemsToUpdate.forEach { repository.updateShoppingListItem(it) }
            }
            if (itemsToInsert.isNotEmpty()) {
                repository.addItemsToShoppingList(itemsToInsert) // Este método debería manejar inserciones múltiples
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
