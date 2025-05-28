package org.mlc.shoppingcart.error

class ProductAlreadyExistsException(message: String) : RuntimeException(message)
class ProductNotFoundException(message: String) : RuntimeException(message)
class CategoryNotFoundException(message: String) : RuntimeException(message)
class CategoryAlreadyExistsException(message: String) : RuntimeException(message)
class ImageNotFoundException(message: String) : RuntimeException(message)
class ImageProcessingException(message: String) : RuntimeException(message)
class CartNotFoundException(message: String) : RuntimeException(message)
class CartItemNotFoundException(message: String) : RuntimeException(message)
class UserAlreadyExistsException(message: String) : RuntimeException(message)
class InvalidCredentialsException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)