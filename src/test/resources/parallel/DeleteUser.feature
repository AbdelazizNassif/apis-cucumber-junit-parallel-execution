@delete-user
Feature: Delete Existing User/  Our system user should be able to delete existing user

  Background: A new user is created as precondition for all scenarios
    Given a new user is just created to be deleted

  @regression @users @smoke @automated
  Scenario: As logged in system user, I should be able to delete existing user
    And I am a logged in system user, who have permission to delete existing user
    When I delete existing user
    Then The user should be removed from the system

  @users @automated
  Scenario: As non-logged in system user, I should not be able to delete existing user
    And I am not logged in for deleting existing user
    When I delete existing while being non-authorized to delete
    Then I should receive response that "Resource not found" as protection for our users