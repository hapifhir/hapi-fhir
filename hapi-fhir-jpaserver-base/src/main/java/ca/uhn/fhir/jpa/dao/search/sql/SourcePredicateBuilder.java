package ca.uhn.fhir.jpa.dao.search.sql;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourcePredicateBuilder extends BasePredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SourcePredicateBuilder.class);
	private final DbColumn myColumnSourceUri;
	private final DbColumn myColumnRequestId;
	private final DbColumn myResourceIdColumn;

	/**
	 * Constructor
	 */
	public SourcePredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_VER_PROV"));

		myResourceIdColumn = getTable().addColumn("RES_PID");
		myColumnSourceUri = getTable().addColumn("SOURCE_URI");
		myColumnRequestId = getTable().addColumn("REQUEST_ID");
	}


	@Override
	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}

	public Condition createPredicateSourceUri(String theSourceUri) {
		return BinaryCondition.equalTo(myColumnSourceUri, generatePlaceholder(theSourceUri));
	}

	public Condition createPredicateRequestId(String theRequestId) {
		return BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId));
	}

}
