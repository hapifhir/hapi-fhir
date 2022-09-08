package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;

public class MissingQueryParameterPredicateParams {
	/**
	 * Base ResourceTable predicate builder
	 */
	private ResourceTablePredicateBuilder myResourceTablePredicateBuilder;
	/**
	 * The missing boolean.
	 * True if looking for missing fields.
	 * False if looking for non-missing fields.
	 */
	private boolean myIsMissing;
	/**
	 * The Search Parameter Name
	 */
	private String myParamName;
	/**
	 * The partition id
	 */
	private RequestPartitionId myRequestPartitionId;

	public MissingQueryParameterPredicateParams(ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder theResourceTablePredicateBuilder,
															  boolean theTheMissing,
															  String theParamName,
															  ca.uhn.fhir.interceptor.model.RequestPartitionId theRequestPartitionId) {
		myResourceTablePredicateBuilder = theResourceTablePredicateBuilder;
		myIsMissing = theTheMissing;
		myParamName = theParamName;
		myRequestPartitionId = theRequestPartitionId;
	}

	public ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder getResourceTablePredicateBuilder() {
		return myResourceTablePredicateBuilder;
	}

	public boolean isMissing() {
		return myIsMissing;
	}

	public String getParamName() {
		return myParamName;
	}

	public ca.uhn.fhir.interceptor.model.RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}
}
