package com.robinelvin.sbc.repositories;

import com.robinelvin.sbc.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Robin Elvin
 */
public interface MyUserDetailsService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;

    User getUserFromSession();

    @Transactional
    User register(String username, String firstName, String lastName, String password);
}
