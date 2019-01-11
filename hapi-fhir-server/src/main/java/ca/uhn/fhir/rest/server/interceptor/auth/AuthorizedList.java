package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Return type for {@link SearchNarrowingInterceptor#buildAuthorizedList(RequestDetails)}
 */
public class AuthorizedList {

	private List<String> myCompartments;
	private List<String> myResources;

	List<String> getCompartments() {
		return myCompartments;
	}

	List<String> getResources() {
		return myResources;
	}

	/**
	 * Adds a compartment that the user should be allowed to access
	 *
	 * @param theCompartment The compartment name, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 as well as Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addCompartment(String theCompartment) {
		Validate.notNull(theCompartment, "theCompartment must not be null");
		if (myCompartments == null) {
			myCompartments = new ArrayList<>();
		}
		myCompartments.add(theCompartment);

		return this;
	}

	/**
	 * Adds a compartment that the user should be allowed to access
	 *
	 * @param theCompartments The compartment names, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 as well as Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addCompartments(String... theCompartments) {
		Validate.notNull(theCompartments, "theCompartments must not be null");
		for (String next : theCompartments) {
			addCompartment(next);
		}
		return this;
	}

	/**
	 * Adds a resource that the user should be allowed to access
	 *
	 * @param theResource The resource name, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 but not Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addResource(String theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		if (myResources == null) {
			myResources = new ArrayList<>();
		}
		myResources.add(theResource);

		return this;
	}

	/**
	 * Adds a resource that the user should be allowed to access
	 *
	 * @param theResources The resource names, e.g. "Patient/123" (in this example the user would be allowed to access Patient/123 but not Observations where Observation.subject="Patient/123"m, etc.
	 * @return Returns <code>this</code> for easy method chaining
	 */
	public AuthorizedList addResources(String... theResources) {
		Validate.notNull(theResources, "theResources must not be null");
		for (String next : theResources) {
			addResource(next);
		}
		return this;
	}
}
