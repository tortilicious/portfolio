package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.user.*
// Ya no necesitas JWTUtil, AuthenticationManager, UserDetailsService directamente aquí para el login
import org.mlc.shoppingcart.service.auth.AuthService // Inyecta el nuevo AuthService
import org.mlc.shoppingcart.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController(
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping("/auth/register")
    fun registerUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val newUser = userService.registerNewUser(request)
        return ResponseEntity(newUser, HttpStatus.CREATED)
    }

    @PostMapping("/auth/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val loginResponse = authService.login(request)
        return ResponseEntity.ok(loginResponse)
    }

    // Las siguientes rutas deberían estar protegidas y requerir un token JWT
    // Asegúrate de que tu SecurityConfig las proteja.

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @GetMapping("/users/search")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserResponse> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PutMapping("/users/profile/{id}")
    fun updateUser(
        @RequestBody request: UpdateUserRequest,
        @PathVariable("id") id: Long
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUserProfile(request, userId = id)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/users/profile/{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}