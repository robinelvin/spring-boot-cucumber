Feature: Greeting
  Scenario: Greeting
    Given there are no greetings in the database
    When I ask for a greeting
    Then I should see "Hello, World!"