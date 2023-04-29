@get-user
Feature: Get Existing User Data/ Our system user should be able to get users

  @regression @users @smoke  @automated
  Scenario: As logged in system user, I should be able to get other user data
    Given I am a logged in system user, who tries to get user data
    And New user is just added to our system
    When I try to get the newly created user
    Then I should see the same created user data

  @regression @users @automated
  Scenario: As logged in system user, I should not be able to check non-existent users
    Given I am a logged in system user, who tries to get user data
    When I try to get data of user who does not exist in our system
    Then I could not get such user user with "Resource not found" 404 error shown

  @users @automated
  Scenario: As non-logged in user, I should not be able to get existing user data
    Given I am not logged in for getting user data
    When I try to get the data of an existing user
    Then I could not get such user user with "Resource not found" 404 error shown

  @users @automated
  Scenario: As logged in user with expired session, I should not be able to get user
    Given I am logged in user with expired session and tries to get the data of existing user
    When I try to get the data of an existing user while having expired session
    Then I could not get such user user with "Invalid token" 401 error shown
