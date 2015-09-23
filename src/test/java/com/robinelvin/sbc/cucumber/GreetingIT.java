package com.robinelvin.sbc.cucumber;

import com.robinelvin.sbc.config.Neo4jTestConfiguration;
import com.robinelvin.sbc.config.TestConfiguration;
import com.robinelvin.sbc.repositories.GreetingRepository;
import cucumber.api.java8.En;
import org.fluentlenium.cucumber.adapter.FluentCucumberTest;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Robin Elvin
 */
//@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = {TestConfiguration.class, Neo4jTestConfiguration.class})
@ContextConfiguration(classes = {TestConfiguration.class, Neo4jTestConfiguration.class})
@WebIntegrationTest(randomPort = true)
public class GreetingIT extends FluentCucumberTest implements En {

    private WebDriver webDriver = new PhantomJSDriver();

    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    private GreetingRepository greetingRepository;

    private String baseURL() {
        return "http://localhost:" + serverPort;
    }

    @Override
    public WebDriver getDefaultDriver() {
        return webDriver;
    }

    @After
    public void after() {
        this.quit();
    }

    @Before
    public void before() {
        this.initFluent(webDriver);
        this.initTest();
    }

    public GreetingIT() {
        Given("^there are no greetings in the database$", () -> {
            // Write code here that turns the phrase above into concrete actions
            assertEquals(0, greetingRepository.count());
        });

        When("^I ask for a greeting$", () -> {
            // Write code here that turns the phrase above into concrete actions
            this.initFluent(webDriver);
            this.initTest();
            goTo(baseURL() + "/greeting");
        });

        Then("^I should see \"([^\"]*)\"$", (String arg1) -> {
            // Write code here that turns the phrase above into concrete actions
            //assertTrue(pageSource().contains(arg1));
            assertEquals(arg1, pageSource());
        });
    }
}
