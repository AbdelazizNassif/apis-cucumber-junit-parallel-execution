@edit-user
Feature: Edit Existing User/  Our system user should be able to edit user details


  @regression @users @smoke @automated
  Scenario: As logged in system user, I should be able to change user email with another valid email
    Given I am a logged in system user and have permission for editing user
    And a new user is just created to be edited
    When I update existing user with another unique email
    Then The user data should be updated and the new email should be saved

  @regression @users @automated
  Scenario: As non-logged in system user, I should not be able to edit user
    Given I am non-logged in system user who tries to edit existing user
    When I update existing user with another unique email while being non-logged in
    Then Existing user is not updated with "Resource not found" with 404 is shown to non-logged in user as protection for our users

  @users @automated
  Scenario: As logged in with expired session user, I should not be able to edit user
    Given I have expired login session who tries to edit existing user
    When I update existing user with unique email while having expired session
    Then Existing user is not updated with "Invalid token" with 401 is shown to non-logged in user as protection for our users