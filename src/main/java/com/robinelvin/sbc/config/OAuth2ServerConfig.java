package com.robinelvin.sbc.config;

import com.robinelvin.sbc.oauth.MyUserApprovalHandler;
import com.robinelvin.sbc.repositories.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Robin Elvin
 */

@Configuration
public class OAuth2ServerConfig {

    private static final String SENS_RESOURCE_ID = "sens";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(SENS_RESOURCE_ID).stateless(false); //.authenticationEntryPoint(new RestAuthenticationEntryPoint()); //.stateless(false);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {

//            http
//                    .authorizeRequests()
//                    .antMatchers("/api**")
//                    .hasRole("USER")
//                    .antMatchers("/api/users/register").permitAll()
//                    .and().csrf().disable();
                    //.and()
                    //.requestMatchers()
                    //.antMatchers("/api");

            http
//                     Since we want the protected resources to be accessible in the UI as well we need
//                     session creation to be allowed (it's disabled by default in 2.0.6)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    //.and().requestMatchers().antMatchers("/web/**")
                    .and().antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers("/api/users/register").permitAll()
                    .antMatchers("/api/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
                    //.antMatchers("/web/**").access("!#oauth2.isOAuth() and hasRole('ROLE_USER')")
            ;

            //        .and().authorizeRequests()
            //        .antMatchers("/api/users/register").permitAll() // Allow anyone to register
                    //.antMatchers("/web/register").permitAll() // Allow anyone to register
                    //.antMatchers("/web/**").access("(!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
            //        .antMatchers("/api/**").authenticated() // Secure all other URL
                    //.antMatchers("/resources/**").permitAll()
                    //.antMatchers("/web/**").authenticated().and().httpBasic().and().formLogin().loginPage("/web/login").defaultSuccessUrl("/web/").permitAll()
//                    .and().authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/web/login").defaultSuccessUrl("/web/").permitAll() // TODO fix this
            //        .and().csrf().disable(); // Turn off CSRF protection for everything
//            http
//                    .authorizeRequests()
//                    .antMatchers("/web/**").authenticated().and().formLogin().permitAll().and().logout().permitAll();
            // .hasRole("USER"); //.access("#outh2.hasScope('read')");
            //http.authorizeRequests().antMatchers("/web/**").authenticated().and().formLogin().permitAll();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private UserApprovalHandler userApprovalHandler;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private MyUserDetailsService userDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory().withClient("sensapp")
                    .resourceIds(SENS_RESOURCE_ID)
                    //.authorizedGrantTypes("authorization_code", "implicit")
                    .authorizedGrantTypes("authorization_code", "refresh_token",
                            "password")
                    .authorities("USER")
                    .scopes("read", "write")
                    .secret("secret");
        }

        @Bean
        public TokenStore tokenStore() {
            return new InMemoryTokenStore();
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore)
                    .userApprovalHandler(userApprovalHandler)
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService);
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.realm("sens/client");
        }
    }

    protected static class Approvals {

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Autowired
        private TokenStore tokenStore;

        @Bean
        public ApprovalStore approvalStore() throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        @Bean
        @Lazy
        @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        public MyUserApprovalHandler userApprovalHandler() throws Exception {
            MyUserApprovalHandler handler = new MyUserApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
            handler.setClientDetailsService(clientDetailsService);
            handler.setUseApprovalStore(true);
            return handler;
        }
    }
}
