package org.mlc.shoppingcart.service.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JWTUtil(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val expirationTimeInMs: String
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap()
        val roles = userDetails.authorities
            .map { it.authority }
            .toSet()
        claims["roles"] = roles

        val nowMillis = System.currentTimeMillis()
        val expirationMillis = expirationTimeInMs.toLong()

        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(Date(nowMillis))
            .expiration(Date(nowMillis + expirationMillis))
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun getUserNameFromToken(token: String): String {
        val claims = getAllClaimsFromToken(token)
        return claims.subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        val claims = getAllClaimsFromToken(token)
        return claims.expiration
    }

    fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userNameFromToken = getUserNameFromToken(token)
        return (userNameFromToken == userDetails.username && !isTokenExpired(token))
    }

    fun getRolesFromToken(token: String): Set<String> {
        val claims = getAllClaimsFromToken(token)

        @Suppress("UNCHECKED_CAST")
        return claims["roles"] as? Set<String> ?: setOf()
    }


}