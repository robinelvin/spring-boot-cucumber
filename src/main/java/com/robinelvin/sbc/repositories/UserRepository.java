package com.robinelvin.sbc.repositories;

import com.robinelvin.sbc.models.User;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author Robin Elvin
 */
public interface UserRepository extends GraphRepository<User>, MyUserDetailsService {
    User findByUsername(String username);
}
