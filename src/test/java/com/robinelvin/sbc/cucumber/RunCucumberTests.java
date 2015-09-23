package com.robinelvin.sbc.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author Robin Elvin
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        //tags = {"@web"},
        glue = {
                "cucumber.runtime.java.spring.webappconfig",
                "cucumber.runtime.java.spring.dirtiescontextconfig",
                "com.robinelvin.sbc.cucumber"
        },
        features = {"classpath:com/robinelvin/sbc/cucumber/greeting.feature"},
        plugin = {"pretty"})
public class RunCucumberTests {
}