package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftMatchLikeExpression;

public abstract class BaseResourceHistoryPredicateBuilder extends BaseJoiningPredicateBuilder
		implements ISourcePredicateBuilder {
	protected DbColumn myColumnSourceUri;
	protected DbColumn myColumnRequestId;
	protected DbColumn myResourceIdColumn;

	public BaseResourceHistoryPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_VER"));

		myResourceIdColumn = getTable().addColumn("RES_ID");
		myColumnSourceUri = getTable().addColumn("SOURCE_URI");
		myColumnRequestId = getTable().addColumn("REQUEST_ID");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}

	@Override
	public Condition createPredicateSourceUri(String theSourceUri) {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), BinaryCondition.equalTo(myColumnSourceUri, generatePlaceholder(theSourceUri)));
	}

	@Override
	public Condition createPredicateMissingSourceUri() {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), UnaryCondition.isNull(myColumnSourceUri));
	}

	@Override
	public Condition createPredicateSourceUriWithModifiers(
			IQueryParameterType theQueryParameter, JpaStorageSettings theStorageSetting, String theSourceUri) {

		if (theQueryParameter.getMissing() != null && !theQueryParameter.getMissing()) {
			UnaryCondition condition = UnaryCondition.isNotNull(myColumnSourceUri);
			return combineWithRequestPartitionIdPredicate(getRequestPartitionId(), condition);
		}

		if (theQueryParameter instanceof UriParam uriParam && theQueryParameter.getQueryParameterQualifier() != null) {
			Condition condition =
					switch (uriParam.getQualifier()) {
						case ABOVE -> createPredicateSourceAbove(theSourceUri);
						case BELOW -> createPredicateSourceBelow(theSourceUri);
						case CONTAINS -> createPredicateSourceContains(theStorageSetting, theSourceUri);
					};
			return combineWithRequestPartitionIdPredicate(getRequestPartitionId(), condition);
		}

		return createPredicateSourceUri(theSourceUri);
	}

	private Condition createPredicateSourceAbove(String theSourceUri) {
		List<String> aboveUriCandidates = UrlUtil.getAboveUriCandidates(theSourceUri);
		List<String> aboveUriPlaceholders = generatePlaceholders(aboveUriCandidates);
		return QueryParameterUtils.toEqualToOrInPredicate(myColumnSourceUri, aboveUriPlaceholders);
	}

	private Condition createPredicateSourceBelow(String theSourceUri) {
		String belowLikeExpression = createLeftMatchLikeExpression(theSourceUri);
		return BinaryCondition.like(myColumnSourceUri, generatePlaceholder(belowLikeExpression));
	}

	private Condition createPredicateSourceContains(JpaStorageSettings theStorageSetting, String theSourceUri) {
		if (theStorageSetting.isAllowContainsSearches()) {
			FunctionCall upperFunction = new FunctionCall("UPPER");
			upperFunction.addCustomParams(myColumnSourceUri);
			String normalizedString = StringUtil.normalizeStringForSearchIndexing(theSourceUri);
			String containsLikeExpression = createLeftAndRightMatchLikeExpression(normalizedString);
			return BinaryCondition.like(upperFunction, generatePlaceholder(containsLikeExpression));
		} else {
			throw new MethodNotAllowedException(
					Msg.code(getContainsModifierDisabledCode()) + ":contains modifier is disabled on this server");
		}
	}

	protected abstract int getContainsModifierDisabledCode();

	@Override
	public Condition createPredicateRequestId(String theRequestId) {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId)));
	}
}
