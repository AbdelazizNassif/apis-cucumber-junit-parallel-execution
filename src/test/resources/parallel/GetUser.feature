Feature: GET USER/ Authenticated user should be able to get users

  Scenario: As authenticated user, I should be able to get with valid user id
    Given I have valid authentication token for getting user
    And New user is just created and had valid user id
    When I try to get the newly created user
    Then I should see the same created user data


  Scenario: As authenticated user, I should not be able to get with invalid user id
    Given I have valid authentication token for getting user
    When I try to get user with invalid user id
    Then I should receive response that this user is not found

  Scenario: As non-authenticated user, I should not be able to get user
    Given I did not add authentication token for getting user
    When I try to get user with valid user id
    Then I should receive response that authentication is required

  Scenario: As non-authenticated user, I should not be able to get user
    Given I have invalid authentication token for getting user
    When I try to get user with unique email while having invalid token
    Then I should receive response that token is invalid