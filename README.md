Cucumber Test Framework for Pet Store API
=========================================

### Tested with

- Java version: 1.8.0_144
- Apache Maven version: 3.6.0 

### How do I run tests?

- To run the tests, execute the following Maven command:

        mvn clean verify
  
### Reports

The framework will generate reports after executing the tests. These reports consist of Cluecumber (HTML) and Surefire reports.
They can be found at the following locations:
        
        /target/cluecumber-report
        /target/surefire-reports

### Issues found

There are a few failing tests. This is due to the following reasons:

- Rules are not followed strictly enough on the API side. For example, 'required' doesn't seem to be enforced right now and the same goes for missing or invalid parameters (like not picking one of the enum values)
- No checks on the API side for duplicate entries (it is a demo API, so it obviously doesn't store data)
- The API returns incorrect error codes in situations where the user made mistakes (400 vs 500 errors)

### Improvements

- Implement parallel execution (in batches) when number of tests increase
- If this was not a demo: check the unit tests and move several tests in this project to that level if not covered yet
- If this was not a demo: the error codes for failures such as duplicate entries is not defined in the API. Needs a conversation with the developers/Product Owner, as a decision needs to be made about the correct expected errors and to update Swagger