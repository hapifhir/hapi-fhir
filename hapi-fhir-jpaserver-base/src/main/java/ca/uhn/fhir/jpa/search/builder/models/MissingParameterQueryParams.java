package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.List;

public class MissingParameterQueryParams {
	/**
	 * The sql builder
	 */
	public SearchQueryBuilder SqlBuilder;

	/**
	 * The parameter type
	 */
	public RestSearchParameterTypeEnum ParamType;

	/**
	 * The list of query parameter types (only needed for validation)
	 */
	public List<? extends IQueryParameterType> QueryParameterTypes;

	/**
	 * The missing boolean value from :missing=true/false
	 */
	public boolean IsMissing;

	/**
	 * The name of the parameter.
	 */
	public String ParamName;

	/**
	 * The resource type
	 */
	public String ResourceType;

	/**
	 * The column on which to join.
	 */
	public DbColumn SourceJoinColumn;

	/**
	 * The partition id
	 */
	public RequestPartitionId RequestPartitionId;

	public MissingParameterQueryParams(
		SearchQueryBuilder theSqlBuilder,
		RestSearchParameterTypeEnum theParamType,
		List<? extends IQueryParameterType> theList,
		String theParamName,
		String theResourceType,
		DbColumn theSourceJoinColumn,
		RequestPartitionId theRequestPartitionId
	) {
		SqlBuilder = theSqlBuilder;
		ParamType = theParamType;
		QueryParameterTypes = theList;
		IsMissing = theList.get(0).getMissing();
		ParamName = theParamName;
		ResourceType = theResourceType;
		SourceJoinColumn = theSourceJoinColumn;
		RequestPartitionId = theRequestPartitionId;
	}
}
