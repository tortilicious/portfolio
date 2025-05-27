import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.mlc.shoppingcart.model.Image
import java.math.BigDecimal

// Request DTO for creating a product
data class CreateProductRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Brand is required")
    val brand: String,
    val description: String?,
    @field:Positive(message = "Price must be positive")
    val price: BigDecimal,
    @field:Min(0, message = "Inventory cannot be negative")
    val inventory: Int,
    val images: List<Image>?,
    @field:NotNull(message = "Category name is required")
    val categoryName: String
)
