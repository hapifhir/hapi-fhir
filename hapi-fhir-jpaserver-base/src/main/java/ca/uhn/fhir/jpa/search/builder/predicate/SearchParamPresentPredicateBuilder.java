package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toOrPredicate;

public class SearchParamPresentPredicateBuilder extends BaseJoiningPredicateBuilder {

	private final DbColumn myColumnResourceId;
	private final DbColumn myColumnHashPresence;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public SearchParamPresentPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_PARAM_PRESENT"));
		myColumnResourceId = getTable().addColumn("RES_ID");
		myColumnHashPresence = getTable().addColumn("HASH_PRESENCE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResourceId;
	}


	public Condition createPredicateParamMissingForReference(String theResourceName, String theParamName, boolean theMissing, RequestPartitionId theRequestPartitionId) {
		Long hash = SearchParamPresent.calculateHashPresence(myPartitionSettings, theRequestPartitionId, theResourceName, theParamName, !theMissing);
		BinaryCondition predicate = BinaryCondition.equalTo(myColumnHashPresence, generatePlaceholder(hash));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

}
