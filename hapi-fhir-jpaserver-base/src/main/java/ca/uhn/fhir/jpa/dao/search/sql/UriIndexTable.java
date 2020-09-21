package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.jpa.dao.search.BasePredicateBuilder2.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.dao.search.BasePredicateBuilder2.createLeftMatchLikeExpression;
import static ca.uhn.fhir.jpa.dao.search.BasePredicateBuilder2.createRightMatchLikeExpression;

public class UriIndexTable extends BaseSearchParamIndexTable {

	private static final Logger ourLog = LoggerFactory.getLogger(UriIndexTable.class);
	private final DbColumn myColumnUri;
	private final DbColumn myColumnHashUri;

	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;

	/**
	 * Constructor
	 */
	public UriIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_URI"));

		myColumnUri = getTable().addColumn("SP_URI");
		myColumnHashUri = getTable().addColumn("HASH_URI");
	}


	public Condition addPredicate(List<? extends IQueryParameterType> theUriOrParameterList, String theParamName, SearchFilterParser.CompareOperation theOperation) {

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theUriOrParameterList) {

			if (nextOr instanceof UriParam) {
				UriParam param = (UriParam) nextOr;

				String value = param.getValue();
				if (value == null) {
					continue;
				}

				if (param.getQualifier() == UriParamQualifierEnum.ABOVE) {

					/*
					 * :above is an inefficient query- It means that the user is supplying a more specific URL (say
					 * http://example.com/foo/bar/baz) and that we should match on any URLs that are less
					 * specific but otherwise the same. For example http://example.com and http://example.com/foo would both
					 * match.
					 *
					 * We do this by querying the DB for all candidate URIs and then manually checking each one. This isn't
					 * very efficient, but this is also probably not a very common type of query to do.
					 *
					 * If we ever need to make this more efficient, lucene could certainly be used as an optimization.
					 */
					ourLog.info("Searching for candidate URI:above parameters for Resource[{}] param[{}]", getResourceType(), theParamName);
					// FIXME: send perftrace warning?
					Collection<String> candidates = myResourceIndexedSearchParamUriDao.findAllByResourceTypeAndParamName(getResourceType(), theParamName);
					List<String> toFind = new ArrayList<>();
					for (String next : candidates) {
						if (value.length() >= next.length()) {
							if (value.startsWith(next)) {
								toFind.add(next);
							}
						}
					}

					if (toFind.isEmpty()) {
						continue;
					}

					InCondition uriPredicate = new InCondition(myColumnUri, toFind);
					Condition hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(getResourceType(), theParamName, uriPredicate, getRequestPartitionId());
					codePredicates.add(hashAndUriPredicate);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {

					Condition uriPredicate = BinaryCondition.like(myColumnUri, createLeftMatchLikeExpression(value));
					Condition hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(getResourceType(), theParamName, uriPredicate, getRequestPartitionId());
					codePredicates.add(hashAndUriPredicate);

				} else {

					Condition uriPredicate = null;
					if (theOperation == null || theOperation == SearchFilterParser.CompareOperation.eq) {
						long hashUri = ResourceIndexedSearchParamUri.calculateHashUri(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName, value);
						Condition hashPredicate = BinaryCondition.equalTo(myColumnHashUri, generatePlaceholder(hashUri));
						codePredicates.add(hashPredicate);
					} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
						uriPredicate = BinaryCondition.greaterThanOrEq(myColumnUri, value);
					} else if (theOperation == SearchFilterParser.CompareOperation.co) {
						uriPredicate = BinaryCondition.like(myColumnUri, createLeftAndRightMatchLikeExpression(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.gt) {
						uriPredicate = BinaryCondition.greaterThan(myColumnUri, value);
					} else if (theOperation == SearchFilterParser.CompareOperation.lt) {
						uriPredicate = BinaryCondition.lessThan(myColumnUri, value);
					} else if (theOperation == SearchFilterParser.CompareOperation.ge) {
						uriPredicate = BinaryCondition.greaterThanOrEq(myColumnUri, value);
					} else if (theOperation == SearchFilterParser.CompareOperation.le) {
						uriPredicate = BinaryCondition.lessThanOrEq(myColumnUri, value);
					} else if (theOperation == SearchFilterParser.CompareOperation.sw) {
						uriPredicate = BinaryCondition.like(myColumnUri, createLeftMatchLikeExpression(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.ew) {
						uriPredicate = BinaryCondition.like(myColumnUri, createRightMatchLikeExpression(value));
					} else {
						throw new IllegalArgumentException(String.format("Unsupported operator specified in _filter clause, %s",
							theOperation.toString()));
					}

					if (uriPredicate != null) {
						long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName);
						BinaryCondition hashIdentityPredicate = BinaryCondition.equalTo(getColumnHashIdentity(), generatePlaceholder(hashIdentity));
						codePredicates.add(ComboCondition.and(hashIdentityPredicate, uriPredicate));
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid URI type: " + nextOr.getClass());
			}

		}

		/*
		 * If we haven't found any of the requested URIs in the candidates, then the whole query should match nothing
		 */
		if (codePredicates.isEmpty()) {
			setMatchNothing();
			return null;
		}

		ComboCondition orPredicate = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		Condition outerPredicate = combineParamIndexPredicateWithParamNamePredicate(getResourceType(), theParamName, orPredicate, getRequestPartitionId());
		return outerPredicate;

	}
}
