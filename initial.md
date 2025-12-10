# Acceptance Criteria

* Please do a deep analysis of ca.uhn.fhir.rest.server.method.MethodUtil
* This class contains a massive "god" method that's unweildy and difficult to maintain 
* I want to refactor it, but be cautious because this is a critical component
* So in phase I, I want to write a comprehensive unit test that uses minimal mocks.  Be biased towards using real classes but fallback to mocks for any test case difficult or impossible to express with real classes
* Ensure all new unit tests pass in phase 1
* In phase 2, do the actual refactoring, generating a chain of reasonably compact private method calls with reusable patterns.
* Once this is done, run the new unit tests again
* Run all tests in the module to ensure they still pass
