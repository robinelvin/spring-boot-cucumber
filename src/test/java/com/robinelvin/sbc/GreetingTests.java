package com.robinelvin.sbc;

import com.robinelvin.sbc.config.Neo4jTestConfiguration;
import com.robinelvin.sbc.config.TestConfiguration;
import com.robinelvin.sbc.models.Greeting;
import com.robinelvin.sbc.repositories.GreetingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Robin Elvin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, Neo4jTestConfiguration.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GreetingTests {
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        greetingRepository.save(new Greeting(1, "Testing!"));
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private GreetingRepository greetingRepository;

    @Test
    @WithMockUser
    public void testGreeting() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Hello, World!")))
                .andDo(print())
        ;
    }

}
