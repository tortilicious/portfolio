package org.mlc.tasktracker.exception


class TaskListNotFoundException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)
class AuthorizationException(message: String) : RuntimeException(message)
class TaskNotFoundException(message: String) : RuntimeException(message)
class UserAlreadyExistException(message: String) : RuntimeException(message)
class InvalidCredentialsException(message: String) : RuntimeException(message)