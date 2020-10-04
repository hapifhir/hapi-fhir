package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchSqlBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toEqualToOrInPredicate;

public abstract class BaseJoiningPredicateBuilder extends BasePredicateBuilder {

	private final DbTable myTable;
	private final DbColumn myColumnPartitionId;

	BaseJoiningPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder);
		myTable = theTable;
		myColumnPartitionId = theTable.addColumn("PARTITION_ID");
	}

	public DbTable getTable() {
		return myTable;
	}

	public abstract DbColumn getResourceIdColumn();

	DbColumn getPartitionIdColumn() {
		return myColumnPartitionId;
	}

	public Condition combineWithRequestPartitionIdPredicate(RequestPartitionId theRequestPartitionId, Condition theCondition) {
		Condition partitionIdPredicate = createPartitionIdPredicate(theRequestPartitionId);
		if (partitionIdPredicate == null) {
			return theCondition;
		}
		return toAndPredicate(partitionIdPredicate, theCondition);
	}


	@Nullable
	public Condition createPartitionIdPredicate(RequestPartitionId theRequestPartitionId) {
		if (theRequestPartitionId != null && !theRequestPartitionId.isAllPartitions()) {
			Condition condition;
			Integer partitionId = theRequestPartitionId.getPartitionId();
			if (partitionId != null) {
				Object placeholder = generatePlaceholder(partitionId);
				condition = BinaryCondition.equalTo(getPartitionIdColumn(), placeholder);
			} else {
				condition = UnaryCondition.isNull(getPartitionIdColumn());
			}
			return condition;
		} else {
			return null;
		}
	}

	public Condition createPredicateResourceIds(boolean theInverse, List<Long> theResourceIds) {
		Validate.notNull(theResourceIds, "theResourceIds must not be null");

		// Handle the _id parameter by adding it to the tail
		Condition inResourceIds = toEqualToOrInPredicate(getResourceIdColumn(), generatePlaceholders(theResourceIds));
		if (theInverse) {
			inResourceIds = new NotCondition(inResourceIds);
		}
		return inResourceIds;

	}


}
