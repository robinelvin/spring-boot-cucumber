package com.robinelvin.sbc.config;

import com.robinelvin.sbc.repositories.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Robin Elvin
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/resources/**", "/web/register").permitAll()
                //.antMatchers("/web/**")
                //.anyRequest().authenticated()
                .antMatchers("/web/**").authenticated()
                .and()
                        //.exceptionHandling().accessDeniedPage("/login?authorization_error=true")
                        //.and()
                .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/users/register")).disable()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/web/home");
        //.formLogin().loginProcessingUrl("/login").failureUrl("/login?authorization_error=true").defaultSuccessUrl("/web/home").loginPage("/login");
    }

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("robin").password("robpass").roles("USER", "CLIENT");
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
