package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BaseIndexTable {

	private final DbTable myTable;
	private final DbColumn myColumnPartitionId;
	private final SearchSqlBuilder mySearchSqlBuilder;

	BaseIndexTable(SearchSqlBuilder theSearchSqlBuilder, DbTable theTable) {
		mySearchSqlBuilder = theSearchSqlBuilder;
		myTable = theTable;
		myColumnPartitionId = theTable.addColumn("PARTITION_ID");
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


	DbTable getTable() {
		return myTable;
	}

	abstract DbColumn getResourceIdColumn();

	DbColumn getPartitionIdColumn() {
		return myColumnPartitionId;
	}

	public void addPartitionIdPredicate(Integer thePartitionId) {
		Condition condition = createPartitionIdPredicate(thePartitionId);
		addCondition(condition);
	}

	@Nonnull
	Condition createPartitionIdPredicate(Integer theThePartitionId) {
		Condition condition;
		if (theThePartitionId != null) {
			Object placeholder = generatePlaceholder(theThePartitionId);
			condition = BinaryCondition.equalTo(getPartitionIdColumn(), placeholder);
		} else {
			condition = UnaryCondition.isNull(getPartitionIdColumn());
		}
		return condition;
	}

	public Condition createPredicateResourceIds(boolean theInverse, List<Long> theResourceIds) {
		Validate.notNull(theResourceIds, "theResourceIds must not be null");

		// Handle the _id parameter by adding it to the tail
		Condition inResourceIds = new InCondition(getResourceIdColumn(), generatePlaceholders(theResourceIds));
		if (theInverse) {
			inResourceIds = new NotCondition(inResourceIds);
		}
		return inResourceIds;

	}

	void addCondition(Condition theCondition) {
		assert theCondition != null;
		mySearchSqlBuilder.addCondition(theCondition);
	}

	@Nonnull
	String generatePlaceholder(Object theInput) {
		return mySearchSqlBuilder.generatePlaceholder(theInput);
	}

	@Nonnull
	List<String> generatePlaceholders(List<?> theValues) {
		return mySearchSqlBuilder.generatePlaceholders(theValues);
	}

	protected FhirContext getFhirContext() {
		return mySearchSqlBuilder.getFhirContext();
	}

	protected void setMatchNothing() {
		mySearchSqlBuilder.setMatchNothing();
	}
}
