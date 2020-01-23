package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredicateBuilderUri extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderUri.class);
	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;

	PredicateBuilderUri(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	public Predicate addPredicateUri(String theResourceName,
												String theParamName,
												List<? extends IQueryParameterType> theList,
												SearchFilterParser.CompareOperation operation) {

		Join<ResourceTable, ResourceIndexedSearchParamUri> join = createJoin(SearchBuilderJoinEnum.URI, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

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
					ourLog.info("Searching for candidate URI:above parameters for Resource[{}] param[{}]", myResourceName, theParamName);
					Collection<String> candidates = myResourceIndexedSearchParamUriDao.findAllByResourceTypeAndParamName(myResourceName, theParamName);
					List<String> toFind = new ArrayList<>();
					for (String next : candidates) {
						if (value.length() >= next.length()) {
							if (value.substring(0, next.length()).equals(next)) {
								toFind.add(next);
							}
						}
					}

					if (toFind.isEmpty()) {
						continue;
					}

					Predicate uriPredicate = join.get("myUri").as(String.class).in(toFind);
					Predicate hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {

					Predicate uriPredicate = myBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
					Predicate hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else {
					if (myDontUseHashesForSearch) {
						Predicate predicate = myBuilder.equal(join.get("myUri").as(String.class), value);
						codePredicates.add(predicate);
					} else {

						Predicate uriPredicate = null;
						if (operation == null || operation == SearchFilterParser.CompareOperation.eq) {
							long hashUri = ResourceIndexedSearchParamUri.calculateHashUri(theResourceName, theParamName, value);
							Predicate hashPredicate = myBuilder.equal(join.get("myHashUri"), hashUri);
							codePredicates.add(hashPredicate);
						} else if (operation == SearchFilterParser.CompareOperation.ne) {
							uriPredicate = myBuilder.notEqual(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.co) {
							uriPredicate = myBuilder.like(join.get("myUri").as(String.class), createLeftAndRightMatchLikeExpression(value));
						} else if (operation == SearchFilterParser.CompareOperation.gt) {
							uriPredicate = myBuilder.greaterThan(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.lt) {
							uriPredicate = myBuilder.lessThan(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.ge) {
							uriPredicate = myBuilder.greaterThanOrEqualTo(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.le) {
							uriPredicate = myBuilder.lessThanOrEqualTo(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.sw) {
							uriPredicate = myBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
						} else if (operation == SearchFilterParser.CompareOperation.ew) {
							uriPredicate = myBuilder.like(join.get("myUri").as(String.class), createRightMatchLikeExpression(value));
						} else {
							throw new IllegalArgumentException(String.format("Unsupported operator specified in _filter clause, %s",
								operation.toString()));
						}

						if (uriPredicate != null) {
							long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
							Predicate hashIdentityPredicate = myBuilder.equal(join.get("myHashIdentity"), hashIdentity);
							codePredicates.add(myBuilder.and(hashIdentityPredicate, uriPredicate));
						}
					}
				}

			} else {
				throw new IllegalArgumentException("Invalid URI type: " + nextOr.getClass());
			}

		}

		/*
		 * If we haven't found any of the requested URIs in the candidates, then we'll
		 * just add a predicate that can never match
		 */
		if (codePredicates.isEmpty()) {
			Predicate predicate = myBuilder.isNull(join.get("myMissing").as(String.class));
			myPredicates.add(predicate);
			return null;
		}

		Predicate orPredicate = myBuilder.or(toArray(codePredicates));

		Predicate outerPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName,
			theParamName,
			join,
			orPredicate);
		myPredicates.add(outerPredicate);
		return outerPredicate;
	}
}
