# Api Automation

# Used tools
- Java, rest assured, cucumber, junit

# Design patterns used:
- Object model design pattern
- Data-driven design pattern
  - Getting configuration variables from property files (like environment url & credentials)
  - Getting test data variables from json files (data to be used in testcases)
  - Behavior driver design patterns using cucumber as bdd tool.

# How to run:
- Download the project
- Open terminal in the project folder
- Run command mvn clean, mvn test

# The following features are implemented:
- Cucumber runner class using junit runner
- Cucumber parallel execution on feature file level
- Github actions and artifact the html report

# In progress:
- cucumber tags
