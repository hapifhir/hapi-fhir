package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.apache.commons.lang3.Validate;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

public class QueryRootEntryIndexTable extends QueryRootEntry {
	private final CriteriaBuilder myCriteriaBuilder;
	private final QueryRootEntry myTop;
	private final Subquery<Long> myQuery;
	private final Root<? extends BaseResourceIndex> myRoot;
	private final SearchBuilderJoinEnum myParamType;
	private final Expression<Long> myResourcePidColumn;

	public QueryRootEntryIndexTable(CriteriaBuilder theCriteriaBuilder, RestSearchParameterTypeEnum theParamType, QueryRootEntry theParent) {
		super(theCriteriaBuilder);
		myCriteriaBuilder = theCriteriaBuilder;
		myTop = theParent;

		AbstractQuery<Long> queryRoot = theParent.getQueryRoot();
		Subquery<Long> query = queryRoot.subquery(Long.class);
		myQuery = query;

		switch (theParamType) {
			case REFERENCE:
				myRoot = myQuery.from(ResourceLink.class);
				this.myResourcePidColumn = myRoot.get("mySourceResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.REFERENCE;
				break;
			case NUMBER:
				myRoot = myQuery.from(ResourceIndexedSearchParamNumber.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.NUMBER;
				break;
			case DATE:
				myRoot = myQuery.from(ResourceIndexedSearchParamDate.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.DATE;
				break;
			case STRING:
				myRoot = myQuery.from(ResourceIndexedSearchParamString.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.STRING;
				break;
			case TOKEN:
				myRoot = myQuery.from(ResourceIndexedSearchParamToken.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.TOKEN;
				break;
			case QUANTITY:
				myRoot = myQuery.from(ResourceIndexedSearchParamQuantity.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.QUANTITY;
				break;
			case URI:
				myRoot = myQuery.from(ResourceIndexedSearchParamUri.class);
				myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
				myParamType = SearchBuilderJoinEnum.URI;
				break;
			default:
			case COMPOSITE:
			case HAS:
			case SPECIAL:
				throw new IllegalStateException();
		}

		query.select(myResourcePidColumn);

	}

	@Override
	void orderBy(List<Order> theOrders) {
		throw new IllegalStateException();
	}

	@Override
	Expression<Date> getLastUpdatedColumn() {
		return myRoot.get("myUpdated").as(Date.class);
	}

	@Override
	<T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		Validate.isTrue(theType == myParamType, "Wanted %s but got %s for %s", myParamType, theType, theSearchParameterName);
		return (From<?, T>) myRoot;
	}

	@Override
	AbstractQuery<Long> getQueryRoot() {
		return myQuery;
	}

	@Override
	Root<?> getRoot() {
		return myRoot;
	}

	@Override
	public Expression<Long> getResourcePidColumn() {
		return myResourcePidColumn;
	}

	@Override
	public Subquery<Long> subqueryForTagNegation() {
		throw new IllegalStateException();
	}
}
