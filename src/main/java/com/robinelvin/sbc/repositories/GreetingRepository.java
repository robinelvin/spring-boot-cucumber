package com.robinelvin.sbc.repositories;

import com.robinelvin.sbc.models.Greeting;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author Robin Elvin
 */
public interface GreetingRepository extends GraphRepository<Greeting> {
}
