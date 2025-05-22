package org.mlc.shoppingcart.error

class PoductAlreadyExistsException(message: String) : RuntimeException(message)
class ProductNotFoundException(message: String) : RuntimeException(message)
class CategoryNotFoundException(message: String) : RuntimeException(message)
class CategoryAlreadyExistException(message: String) : RuntimeException(message)