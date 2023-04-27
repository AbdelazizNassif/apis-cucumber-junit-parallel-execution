Feature: Edit USER/  Authenticated user should be able to edit user details

  Scenario: As authenticated user, I should be able to change user email with another valid email
    Given I have valid authentication token for editing user
    And a new user is just created to be edited
    When I update existing user with another unique email
    Then The user data should be updated and the new email should be saved

  Scenario: As non-authenticated user, I should not be able to edit user
    Given I did not add authentication token for editing existing user
    When I update existing user with another unique email while being unauthenticated
    Then I should receive response that authentication is required for editing users

  Scenario: As non-authenticated user, I should not be able to edit user
    Given I have invalid authentication token for editing existing user
    When I update existing user with unique email while having invalid token
    Then I should receive response that token is invalid for editing existing users