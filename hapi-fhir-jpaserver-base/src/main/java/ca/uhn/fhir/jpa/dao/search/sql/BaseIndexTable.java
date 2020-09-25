package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BaseIndexTable extends BasePredicateBuilder3 {

	private final DbTable myTable;
	private final DbColumn myColumnPartitionId;

	BaseIndexTable(SearchSqlBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder);
		myTable = theTable;
		myColumnPartitionId = theTable.addColumn("PARTITION_ID");
	}

	DbTable getTable() {
		return myTable;
	}

	public abstract DbColumn getResourceIdColumn();

	DbColumn getPartitionIdColumn() {
		return myColumnPartitionId;
	}

	public Condition combineWithRequestPartitionIdPredicate(RequestPartitionId theRequestPartitionId, Condition theCondition) {
		if (theRequestPartitionId != null && !theRequestPartitionId.isAllPartitions()) {
			return ComboCondition.and(createPartitionIdPredicate_(theRequestPartitionId.getPartitionId()), theCondition);
		} else {
			return theCondition;
		}
	}


	@Nonnull
	Condition createPartitionIdPredicate_(Integer thePartitionId) {
		Condition condition;
		if (thePartitionId != null) {
			Object placeholder = generatePlaceholder(thePartitionId);
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



}
