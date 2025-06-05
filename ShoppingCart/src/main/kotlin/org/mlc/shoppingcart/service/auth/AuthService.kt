package org.mlc.shoppingcart.service.auth

import org.mlc.shoppingcart.dto.user.LoginRequest
import org.mlc.shoppingcart.dto.user.LoginResponse
import org.mlc.shoppingcart.service.security.JWTUtil
import org.mlc.shoppingcart.service.security.ShopUserDetails
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JWTUtil
) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )

        val userDetails = userDetailsService.loadUserByUsername(loginRequest.email)

        val token = jwtUtil.generateToken(userDetails)

        val userId = (userDetails as ShopUserDetails).getId()

        val roles = userDetails.authorities.map { it.authority }

        return LoginResponse(
            token = token,
            id = userId,
            email = userDetails.username,
            roles = roles
        )
    }
}