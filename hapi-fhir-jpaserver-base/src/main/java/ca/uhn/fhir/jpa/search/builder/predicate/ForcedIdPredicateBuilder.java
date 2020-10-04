package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.builder.sql.SearchSqlBuilder;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ForcedIdPredicateBuilder extends BaseJoiningPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(ForcedIdPredicateBuilder.class);
	private final DbColumn myColumnResourceId;
	private final DbColumn myColumnForcedId;

	@Autowired
	private DaoConfig myDaoConfig;

	/**
	 * Constructor
	 */
	public ForcedIdPredicateBuilder(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_FORCED_ID"));

		myColumnResourceId = getTable().addColumn("RESOURCE_PID");
		myColumnForcedId = getTable().addColumn("FORCED_ID");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResourceId;
	}


	public DbColumn getColumnForcedId() {
		return myColumnForcedId;
	}


}

