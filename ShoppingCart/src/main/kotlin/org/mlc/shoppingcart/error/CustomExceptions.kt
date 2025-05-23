package org.mlc.shoppingcart.error

class ProductAlreadyExistsException(message: String) : RuntimeException(message)
class ProductNotFoundException(message: String) : RuntimeException(message)
class CategoryNotFoundException(message: String) : RuntimeException(message)
class CategoryAlreadyExistsException(message: String) : RuntimeException(message)
class ImageNotFoundException(message: String) : RuntimeException(message)
class ImageProcessingException(message: String) : RuntimeException(message)