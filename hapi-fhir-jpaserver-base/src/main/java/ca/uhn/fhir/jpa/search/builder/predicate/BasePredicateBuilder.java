package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.sql.SearchSqlBuilder;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class BasePredicateBuilder {

	private final SearchSqlBuilder mySearchSqlBuilder;

	public BasePredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		mySearchSqlBuilder = theSearchSqlBuilder;
	}


	PartitionSettings getPartitionSettings() {
		return mySearchSqlBuilder.getPartitionSettings();
	}

	RequestPartitionId getRequestPartitionId() {
		return mySearchSqlBuilder.getRequestPartitionId();
	}

	String getResourceType() {
		return mySearchSqlBuilder.getResourceType();
	}

	ModelConfig getModelConfig() {
		return mySearchSqlBuilder.getModelConfig();
	}

	@Nonnull
	String generatePlaceholder(Object theInput) {
		return mySearchSqlBuilder.generatePlaceholder(theInput);
	}

	@Nonnull
	List<String> generatePlaceholders(Collection<?> theValues) {
		return mySearchSqlBuilder.generatePlaceholders(theValues);
	}

	protected FhirContext getFhirContext() {
		return mySearchSqlBuilder.getFhirContext();
	}

	protected void setMatchNothing() {
		mySearchSqlBuilder.setMatchNothing();
	}


	protected BinaryCondition createConditionForValueWithComparator(ParamPrefixEnum theComparator, DbColumn theColumn, Object theValue) {
		return mySearchSqlBuilder.createConditionForValueWithComparator(theComparator, theColumn, theValue);
	}

	protected BaseJoiningPredicateBuilder getOrCreateQueryRootTable() {
		return mySearchSqlBuilder.getOrCreateLastPredicateBuilder();
	}

	public void addJoin(DbTable theFromTable, DbTable theToTable, DbColumn theFromColumn, DbColumn theToColumn) {
		mySearchSqlBuilder.addJoin(theFromTable, theToTable, theFromColumn, theToColumn);
	}

}
