package org.mlc.shoppingcart.controller

import org.mlc.shoppingcart.dto.user.*
import org.mlc.shoppingcart.service.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
class UserController(
    val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val newUser = userService.registerNewUser(request)
        return ResponseEntity(newUser, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @GetMapping("/search")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserResponse> {
        val user = userService.getUserByEmail(email)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val userLogin = userService.userLogin(request)
        return ResponseEntity(userLogin, HttpStatus.OK)
    }

    @PutMapping("/profile/{id}")
    fun updateUser(
        @RequestBody request: UpdateUserRequest,
        @PathVariable("id") id: Long
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUserProfile(request, userId = id)
        return ResponseEntity(updatedUser, HttpStatus.OK)
    }

    @DeleteMapping("/profile/{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }
}