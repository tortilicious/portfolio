package org.mlc.gastosapi.security

import org.mlc.gastosapi.model.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val usuario: Usuario
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        emptyList<GrantedAuthority>().toMutableList()

    override fun getPassword(): String = usuario.passwordHash

    override fun getUsername(): String = usuario.email
}
