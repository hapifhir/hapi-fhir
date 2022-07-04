package ca.uhn.fhir.jpa.dao.search;

public interface IHSearchParamHelperProvider {

	HSearchParamHelper<?> provideHelper(String theResourceTypeName, String theParamName);
}
