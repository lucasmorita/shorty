Feature: Shorten URL
  Scenario: Shorten a valid url
    Given a valid url
    When a url shorten is requested
    Then the response was CREATED
    And the url was shortened

  Scenario: Shorten an invalid url
    Given an invalid url
    When a url shorten is requested
    Then the response was BAD_REQUEST
    And the url was not shortened

  Scenario: Shorten a url without protocol
    Given a url without protocol
    When a url shorten is requested
    Then the response was BAD_REQUEST
    And the url was not shortened

  Scenario: Shorten an unreachable url
    Given an unreachable url
    When a url shorten is requested
    Then the response was BAD_REQUEST
    And the url was not shortened

  Scenario: Find unregistered url
    When a url is requested
    Then the response was NOT_FOUND