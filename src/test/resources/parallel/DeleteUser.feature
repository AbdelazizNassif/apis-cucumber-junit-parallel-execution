Feature: Delete USER/  Authenticated user should be able to delete existing user

  Background: A new user is added to be deleted as pre-condition for each scenario
    Given a new user is just created to be deleted

  Scenario: As authenticated user, I should be able to delete existing user
    Given I have valid authentication token for deleting existing user
    When I delete existing user
    Then The user should be removed from the system

  Scenario: As non-authenticated user, I should not be able to delete existing user
    Given I did not add authentication token for deleting existing user
    When I delete existing while being unauthenticated
    Then I should receive response that authentication is required for deleting users