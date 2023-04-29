@add-user
Feature: Create New User/  Our system user should be able to add new user to our system

  @regression @users @smoke @automated
  Scenario: As logged in system user, I should be able to add user with unique email
    Given I am a logged in system user, who have permission to create new user
    When I add new user with unique email
    Then A new user is added to the system

  @regression @users @automated
  Scenario: As logged in system user, I should not be able to add user with redundant email
    Given I am a logged in system user, who have permission to create new user
    And There is a user in the system with certain email
    When I add new user with redundant email
    Then A new User is not added and I should receive response that email is taken

  @regression @users @automated
  Scenario: As non-logged in system user, I should not be able to add user
    Given I am not logged in system user, forgot to login
    When I try to add new user with unique email
    Then User is not added to the system because "Authentication failed" with 401 shown

  @users @automated
  Scenario: As non-logged in system user, I should not be able to add user
    Given I am not logged in system user, having expired session
    When I try to add new user with unique email while having expired session
    Then User is not added to the system because "Invalid token" with 401 shown

  @users @automated
  Scenario: As logged in system user, I should not be able to add user invalid email format
    Given I am a logged in system user, who have permission to create new user
    When I try to add new user with invalid email format
    Then User is not added to the system because "email" "is invalid"