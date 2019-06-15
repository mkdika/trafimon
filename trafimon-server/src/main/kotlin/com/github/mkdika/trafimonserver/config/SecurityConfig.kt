package com.github.mkdika.trafimonserver.config

import com.github.mkdika.trafimonserver.model.User
import com.github.mkdika.trafimonserver.repository.UserRepository
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .successHandler(customAuthenticationSuccessHandler)
    }
}

@Component
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun onAuthenticationSuccess(request: HttpServletRequest?,
                                         response: HttpServletResponse?,
                                         authentication: Authentication?) {

        val authUser = SecurityContextHolder.getContext().authentication.principal
            as DefaultOidcUser

        logger.info("""
            Success Auth User:
            - name       : ${authUser.name}
            - email      : ${authUser.email}
            - Shown Name : ${authUser.attributes["name"]}
            - picture    : ${authUser.attributes["picture"]}
        """.trimIndent())

        val authenticatedUser = User (
            id = authUser.name,
            name = authUser.attributes["name"].toString(),
            email = authUser.email,
            pictureUrl = authUser.attributes["picture"].toString()
        )

        return userRepository.save(authenticatedUser).let {
            response?.status = HttpServletResponse.SC_OK
            response?.sendRedirect("/api/trafi")
        }
    }

    companion object {
        val logger = LogFactory.getLog(this::class.java)
    }
}