package com.robinelvin.sbc.repositories;

import com.robinelvin.sbc.models.MyUserDetails;
import com.robinelvin.sbc.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * @author Robin Elvin
 */
@Primary
@Service
public class UserRepositoryImpl implements MyUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User findByUsername(String username) {
        return userRepository.findByUsername(username);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("username", username);
//        return template.queryForObject(User.class, "MATCH (user:User {username={username}}) RETURN user", params);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        final User user = findByUsername(username);
        if (user==null) throw new UsernameNotFoundException("Username not found: " + username);
        user.setPasswordEncoder(passwordEncoder);
        return new MyUserDetails(user);
    }

    @Override
    public User getUserFromSession() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) principal;
            return userDetails.getUser();
        }
        return null;
    }

    @Override
    @Transactional
    public User register(String username, String firstName, String lastName, String password) {
        User found = findByUsername(username);
        if (found!=null) throw new RuntimeException("Login already taken: " + username);
        if (firstName==null || firstName.isEmpty()) throw new RuntimeException("No first name provided.");
        if (lastName==null || lastName.isEmpty()) throw new RuntimeException("No last name provided.");
        if (password==null || password.isEmpty()) throw new RuntimeException("No password provided.");
        User user = new User(username, firstName, lastName, password, passwordEncoder, User.Roles.ROLE_USER);
        userRepository.save(user);
        setUserInSession(user);
        return user;
    }

    void setUserInSession(User user) {
        SecurityContext context = SecurityContextHolder.getContext();
        MyUserDetails userDetails = new MyUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),userDetails.getAuthorities());
        context.setAuthentication(authentication);
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @AuthenticationPrincipal
    public @interface CurrentUser {

    }

}