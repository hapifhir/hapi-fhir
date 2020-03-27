package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.dao.SearchBuilder;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Scope("prototype")
class PredicateBuilderUri extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderUri.class);
	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;

	PredicateBuilderUri(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
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

					Predicate uriPredicate = myCriteriaBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
					Predicate hashAndUriPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else {
					if (myDontUseHashesForSearch) {
						Predicate predicate = myCriteriaBuilder.equal(join.get("myUri").as(String.class), value);
						codePredicates.add(predicate);
					} else {

						Predicate uriPredicate = null;
						if (operation == null || operation == SearchFilterParser.CompareOperation.eq) {
							long hashUri = ResourceIndexedSearchParamUri.calculateHashUri(theResourceName, theParamName, value);
							Predicate hashPredicate = myCriteriaBuilder.equal(join.get("myHashUri"), hashUri);
							codePredicates.add(hashPredicate);
						} else if (operation == SearchFilterParser.CompareOperation.ne) {
							uriPredicate = myCriteriaBuilder.notEqual(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.co) {
							uriPredicate = myCriteriaBuilder.like(join.get("myUri").as(String.class), createLeftAndRightMatchLikeExpression(value));
						} else if (operation == SearchFilterParser.CompareOperation.gt) {
							uriPredicate = myCriteriaBuilder.greaterThan(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.lt) {
							uriPredicate = myCriteriaBuilder.lessThan(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.ge) {
							uriPredicate = myCriteriaBuilder.greaterThanOrEqualTo(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.le) {
							uriPredicate = myCriteriaBuilder.lessThanOrEqualTo(join.get("myUri").as(String.class), value);
						} else if (operation == SearchFilterParser.CompareOperation.sw) {
							uriPredicate = myCriteriaBuilder.like(join.get("myUri").as(String.class), createLeftMatchLikeExpression(value));
						} else if (operation == SearchFilterParser.CompareOperation.ew) {
							uriPredicate = myCriteriaBuilder.like(join.get("myUri").as(String.class), createRightMatchLikeExpression(value));
						} else {
							throw new IllegalArgumentException(String.format("Unsupported operator specified in _filter clause, %s",
								operation.toString()));
						}

						if (uriPredicate != null) {
							long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
							Predicate hashIdentityPredicate = myCriteriaBuilder.equal(join.get("myHashIdentity"), hashIdentity);
							codePredicates.add(myCriteriaBuilder.and(hashIdentityPredicate, uriPredicate));
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
			Predicate predicate = myCriteriaBuilder.isNull(join.get("myMissing").as(String.class));
			myQueryRoot.addPredicate(predicate);
			return null;
		}

		Predicate orPredicate = myCriteriaBuilder.or(toArray(codePredicates));

		Predicate outerPredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName,
			theParamName,
			join,
			orPredicate);
		myQueryRoot.addPredicate(outerPredicate);
		return outerPredicate;
	}
}
