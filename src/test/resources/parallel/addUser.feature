Feature: POST NEW USER/  Authenticated user should be able to add new user to the system

  Scenario: As authenticated user, I should be able to add user with unique email
    Given I have valid authentication token for creating new user
    When I add new user with unique email
    Then A new user is added to the system


  Scenario: As authenticated user, I should not be able to add user with redundant email
    Given I have valid authentication token for creating new user
    And There is a user in the system with certain email
    When I add new user with redundant email
    Then A new User is not added and I should receive response that email is taken

  Scenario: As non-authenticated user, I should not be able to add user
    Given I did not add authentication token for creating new user
    When I try to add new user with unique email
    Then A new User is not added and I should receive response that authentication is required

  Scenario: As non-authenticated user, I should not be able to add user
    Given I have invalid authentication token for creating new user
    When I try to add new user with unique email while having invalid token
    Then A new User is not added and I should receive response that token is invalid

  Scenario: As authenticated user, I should not be able to add user invalid email format
    Given I have valid authentication token for creating new user
    When I try to add new user with invalid email format
    Then A new User is not added and I should receive response that email format is not correct