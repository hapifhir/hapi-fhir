package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;

public class MissingQueryParameterPredicateParams {
	/**
	 * Base ResourceTable predicate builder
	 */
	public ResourceTablePredicateBuilder ResourceTablePredicateBuilder;
	/**
	 * The missing boolean.
	 * True if looking for missing fields.
	 * False if looking for non-missing fields.
	 */
	public boolean IsMissing;
	/**
	 * The Search Parameter Name
	 */
	public String ParamName;
	/**
	 * The partition id
	 */
	public RequestPartitionId RequestPartitionId;

	public MissingQueryParameterPredicateParams(ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder theResourceTablePredicateBuilder,
															  boolean theTheMissing,
															  String theParamName,
															  ca.uhn.fhir.interceptor.model.RequestPartitionId theRequestPartitionId) {
		ResourceTablePredicateBuilder = theResourceTablePredicateBuilder;
		IsMissing = theTheMissing;
		ParamName = theParamName;
		RequestPartitionId = theRequestPartitionId;
	}
}
