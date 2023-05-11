package com.example.bookflix;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityAdapter extends WebSecurityConfigurerAdapter {

	/**
	 * Spring Security configuration that sets up OAuth login and enables all URLs
	 * to be accessible without authentication. Also sets up CSRF protection.
	 */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests(a -> a
                                .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .csrf(c -> c
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .logout(l -> l
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutSuccessUrl("/").permitAll()
                )
                .oauth2Login(withDefaults());
		// @formatter:on
    }
    
}


