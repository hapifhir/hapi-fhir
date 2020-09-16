package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.Join;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbJoin;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchSqlBuilder {

	private final String myBindVariableSubstitutionBase = UUID.randomUUID().toString() + "-";
	private final ArrayList<Object> myBindVariableValues = new ArrayList<>();
	private final DbSpec mySpec;
	private final DbSchema mySchema;
	private final SelectQuery mySelect;
	private final PartitionSettings myPartitionSettings;
	private final RequestPartitionId myRequestPartitionId;
	private final String myResourceType;
	private BaseIndexTable myCurrentIndexTable;

	public SearchSqlBuilder(PartitionSettings thePartitionSettings, RequestPartitionId theRequestPartitionId, String theResourceType) {
		myPartitionSettings = thePartitionSettings;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;

		mySpec = new DbSpec();
		mySchema = mySpec.addDefaultSchema();
		mySelect = new SelectQuery();

	}


	public StringIndexTable addStringSelector() {
		StringIndexTable retVal = new StringIndexTable();
		addTable(retVal);
		return retVal;
	}

	public TokenIndexTable addTokenSelector() {
		TokenIndexTable retVal = new TokenIndexTable();
		addTable(retVal);
		return retVal;
	}

	private void addTable(BaseIndexTable theIndexTable) {
		if (myCurrentIndexTable == null) {
			mySelect.addColumns(theIndexTable.getResourceIdColumn());
			mySelect.addFromTable(theIndexTable.getTable());
		} else {
			Join join = new DbJoin(mySpec, myCurrentIndexTable.getTable(), theIndexTable.getTable(), new DbColumn[]{myCurrentIndexTable.getResourceIdColumn()}, new DbColumn[]{theIndexTable.getResourceIdColumn()});
			mySelect.addJoins(SelectQuery.JoinType.LEFT_OUTER, join);
		}
		myCurrentIndexTable = theIndexTable;
	}

	public GeneratedSql generate() {

		// If we don't have an index table at all, there are no search params
		if (myCurrentIndexTable == null) {
			ResourceTable resourceTable = new ResourceTable();
			resourceTable.addResourceTypeAndNonDeletedPredicates();
			addTable(resourceTable);
		}

		mySelect.validate();
		String sql = mySelect.toString();

		List<Object> bindVariables = new ArrayList<>();
		while (true) {

			int idx = sql.indexOf(myBindVariableSubstitutionBase);
			if (idx == -1) {
				break;
			}

			int endIdx = sql.indexOf("'", idx + myBindVariableSubstitutionBase.length());
			String substitutionIndexString = sql.substring(idx + myBindVariableSubstitutionBase.length(), endIdx);
			int substitutionIndex = Integer.parseInt(substitutionIndexString);
			bindVariables.add(myBindVariableValues.get(substitutionIndex));

			sql = sql.substring(0, idx - 1) + "?" + sql.substring(endIdx + 1);
		}

		return new GeneratedSql(sql, bindVariables);
	}

	private String generatePlaceholder(Object theValue) {
		String placeholder = myBindVariableSubstitutionBase + myBindVariableValues.size();
		myBindVariableValues.add(theValue);
		return placeholder;
	}

	public abstract class BaseIndexTable {

		public abstract DbTable getTable();

		public abstract DbColumn getResourceIdColumn();

	}

	public class ResourceTable extends BaseIndexTable {
		private final DbTable myTable;
		private final DbColumn myColumnResId;
		private final DbColumn myColumnResDeletedAt;
		private final DbColumn myColumnResType;

		/**
		 * Constructor
		 */
		public ResourceTable() {
			myTable = mySchema.addTable("HFJ_RESOURCE");
			myColumnResId = myTable.addColumn("RES_ID");
			myColumnResType = myTable.addColumn("RES_TYPE");
			myColumnResDeletedAt = myTable.addColumn("RES_DELETED_AT");
		}

		@Override
		public DbTable getTable() {
			return myTable;
		}

		@Override
		public DbColumn getResourceIdColumn() {
			return myColumnResId;
		}

		public void addResourceTypeAndNonDeletedPredicates() {
			mySelect.addCondition(BinaryCondition.equalTo(myColumnResType, myResourceType));
			mySelect.addCondition(UnaryCondition.isNull(myColumnResDeletedAt));
		}
	}

	public class StringIndexTable extends BaseIndexTable {

		private final DbTable myIndexStringTable;
		private final DbColumn myIndexStringColumnResId;
		private final DbColumn myIndexStringColumnValueExact;
		private final DbColumn myIndexStringColumnValueNormalized;
		private final DbColumn myIndexStringColumnValueNormPrefix;
		private final DbColumn myIndexStringColumnHashIdentity;
		private final DbColumn myIndexStringColumnHashExact;

		/**
		 * Constructor
		 */
		public StringIndexTable() {
			myIndexStringTable = mySchema.addTable("HFJ_SPIDX_STRING");
			myIndexStringColumnResId = myIndexStringTable.addColumn("RES_ID");
			myIndexStringColumnValueExact = myIndexStringTable.addColumn("SP_VALUE_EXACT");
			myIndexStringColumnValueNormalized = myIndexStringTable.addColumn("SP_VALUE_NORMALIZED");
			myIndexStringColumnValueNormPrefix = myIndexStringTable.addColumn("HASH_NORM_PREFIX");
			myIndexStringColumnHashIdentity = myIndexStringTable.addColumn("HASH_IDENTITY");
			myIndexStringColumnHashExact = myIndexStringTable.addColumn("HASH_EXACT");
		}

		@Override
		public DbTable getTable() {
			return myIndexStringTable;
		}

		@Override
		public DbColumn getResourceIdColumn() {
			return myIndexStringColumnResId;
		}

		public void addPredicateExact(String theParamName, String theValueExact) {
			long hash = ResourceIndexedSearchParamString.calculateHashExact(myPartitionSettings, myRequestPartitionId, myResourceType, theParamName, theValueExact);
			String placeholderValue = generatePlaceholder(hash);
			Condition condition = BinaryCondition.equalTo(myIndexStringColumnValueExact, placeholderValue);
			mySelect.addCondition(condition);
		}


	}

	public class TokenIndexTable extends BaseIndexTable {

		private final DbTable myTable;
		private final DbColumn myColumnResId;
		private final DbColumn myColumnHashSystemAndValue;

		/**
		 * Constructor
		 */
		public TokenIndexTable() {
			myTable = mySchema.addTable("HFJ_SPIDX_TOKEN");
			myColumnResId = myTable.addColumn("RES_ID");
			myColumnHashSystemAndValue = myTable.addColumn("HASH_SYS_AND_VALUE");
		}

		@Override
		public DbTable getTable() {
			return myTable;
		}

		@Override
		public DbColumn getResourceIdColumn() {
			return myColumnResId;
		}

		public void addPredicateSystemAndValue(String theParamName, String theSystem, String theValue) {
			long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(myPartitionSettings, myRequestPartitionId, myResourceType, theParamName, theSystem, theValue);
			String placeholderValue = generatePlaceholder(hash);
			Condition condition = BinaryCondition.equalTo(myColumnHashSystemAndValue, placeholderValue);
			mySelect.addCondition(condition);
		}


	}

	public class GeneratedSql {
		private final String mySql;
		private final List<Object> myBindVariables;

		public GeneratedSql(String theSql, List<Object> theBindVariables) {
			mySql = theSql;
			myBindVariables = theBindVariables;
		}

		public List<Object> getBindVariables() {
			return myBindVariables;
		}

		public String getSql() {
			return mySql;
		}
	}
}
