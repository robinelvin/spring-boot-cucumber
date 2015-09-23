package com.robinelvin.sbc;

import com.robinelvin.sbc.config.Neo4jTestConfiguration;
import com.robinelvin.sbc.config.TestConfiguration;
import com.robinelvin.sbc.models.User;
import com.robinelvin.sbc.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, Neo4jTestConfiguration.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class SecurityTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpass";

    @Before
    public void setUp() {
        testUser = new User(TEST_USERNAME, "Test", "User", TEST_PASSWORD, passwordEncoder, User.Roles.ROLE_USER);
        userRepository.save(testUser);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilters(springSecurityFilterChain)
//                .defaultRequest(get("/").with(testSecurityContext()))
//                .build();
    }

    @Test
    @WithMockUser
    public void testGreeting() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Hello, World!")))
        ;
    }

    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(get("/web/home"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(formLogin().user(TEST_USERNAME).password(TEST_PASSWORD))
                .andExpect(authenticated().withUsername(TEST_USERNAME));

//        mockMvc.perform(post("/login")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", TEST_USERNAME)
//                .param("password", TEST_PASSWORD)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection());

    }
}
