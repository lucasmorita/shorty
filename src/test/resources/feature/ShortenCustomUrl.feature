Feature: Shorten using custom Url
  Scenario: Shorten a valid url with a customized url
    Given a valid url
    When a custom url shorten is requested
    Then the response was CREATED

  Scenario: Shorten a valid url but custom url already exists
    Given a valid url
    And custom url already exists in database
    When a custom url shorten is requested
    Then the response was FORBIDDEN
    And custom url is unique