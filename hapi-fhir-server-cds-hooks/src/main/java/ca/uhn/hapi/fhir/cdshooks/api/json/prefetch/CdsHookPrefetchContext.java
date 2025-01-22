package ca.uhn.hapi.fhir.cdshooks.api.json.prefetch;

import ca.uhn.fhir.rest.api.server.cdshooks.BaseCdsServiceJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains the context of a CDS Hooks Prefetch Request
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsHookPrefetchContext extends BaseCdsServiceJson {

	public static final String TEMPLATE = "template";
	public static final String QUERY = "query";
	public static final String USER_DATA = "userData";

	/**
	 * The prefetch template for the prefetch request
	 */
	@JsonProperty(value = TEMPLATE, required = true)
	String myTemplate;

	/**
	 * The actual prefetch query, generated based on the prefetch template using the prefetch context
	 */
	@JsonProperty(value = QUERY, required = true)
	String myQuery;

	/**
	 * Data to be stored between pointcut invocations of a prefetch request/response
	 */
	@JsonProperty(USER_DATA)
	private Map<String, Object> myUserData;

	public String getTemplate() {
		return myTemplate;
	}

	public void setTemplate(String theTemplate) {
		myTemplate = theTemplate;
	}

	public String getQuery() {
		return myQuery;
	}

	public void setQuery(String theQuery) {
		myQuery = theQuery;
	}

	public void addUserData(String theKey, Object theValue) {
		if (myUserData == null) {
			myUserData = new LinkedHashMap<>();
		}
		myUserData.put(theKey, theValue);
	}

	public Map<String, Object> getUserData() {
		if (myUserData == null) {
			myUserData = new LinkedHashMap<>();
		}
		return Collections.unmodifiableMap(myUserData);
	}
}
