package org.mlc.gastosapi.security

import org.mlc.gastosapi.model.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Una implementación personalizada de la interfaz [UserDetails] de Spring Security.
 *
 * La principal ventaja de esta clase es que almacena la entidad completa [Usuario].
 * Esto nos permite acceder a toda la información del usuario (como su ID) directamente
 * desde el objeto de seguridad en los controladores, evitando así consultas adicionales
 * a la base de datos en cada petición.
 *
 * @property usuario La entidad completa del usuario, cargada desde la base de datos.
 */
class CustomUserDetails(
    val usuario: Usuario
) : UserDetails {

    /**
     * Devuelve los roles y permisos (autoridades) concedidos al usuario.
     * En esta aplicación, la autorización es contextual (depende del grupo),
     * por lo que no se cargan roles globales aquí.
     *
     * @return Una colección vacía de [GrantedAuthority].
     */
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    /**
     * Devuelve la contraseña hasheada del usuario.
     * Es requerido por Spring Security para la comparación interna.
     *
     * @return El hash de la contraseña del usuario.
     */
    override fun getPassword(): String = usuario.passwordHash

    /**
     * Devuelve el nombre de usuario utilizado para autenticar al usuario.
     * En nuestra aplicación, este es el email.
     *
     * @return El email del usuario.
     */
    override fun getUsername(): String = usuario.email

    /**
     * Indica si la cuenta del usuario ha expirado.
     * @return `true` ya que no manejamos la expiración de cuentas.
     */
    override fun isAccountNonExpired(): Boolean = true

    /**
     * Indica si el usuario está bloqueado o no.
     * @return `true` ya que no manejamos el bloqueo de cuentas.
     */
    override fun isAccountNonLocked(): Boolean = true

    /**
     * Indica si las credenciales del usuario (contraseña) han expirado.
     * @return `true` ya que no manejamos la expiración de credenciales.
     */
    override fun isCredentialsNonExpired(): Boolean = true

    /**
     * Indica si el usuario está habilitado o deshabilitado.
     * @return `true` ya que no manejamos la deshabilitación de cuentas.
     */
    override fun isEnabled(): Boolean = true
}