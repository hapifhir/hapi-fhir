package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toEqualToOrInPredicate;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createRightMatchLikeExpression;

public class UriPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(UriPredicateBuilder.class);
	private final DbColumn myColumnUri;
	private final DbColumn myColumnHashUri;

	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public UriPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_URI"));

		myColumnUri = getTable().addColumn("SP_URI");
		myColumnHashUri = getTable().addColumn("HASH_URI");
	}


	public Condition addPredicate(List<? extends IQueryParameterType> theUriOrParameterList, String theParamName, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequestDetails) {

		List<Condition> codePredicates = new ArrayList<>();
		boolean predicateIsHash = false;
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
					String msg = "Searching for candidate URI:above parameters for Resource["+getResourceType()+"] param["+theParamName+"]";
					ourLog.info(msg);

					StorageProcessingMessage message = new StorageProcessingMessage();
					ourLog.warn(msg);
					message.setMessage(msg);
					HookParams params = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(StorageProcessingMessage.class, message);
					CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_WARNING, params);

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

					Condition uriPredicate = toEqualToOrInPredicate(myColumnUri, generatePlaceholders(toFind));
					Condition hashAndUriPredicate = combineWithHashIdentityPredicate(getResourceType(), theParamName, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else if (param.getQualifier() == UriParamQualifierEnum.BELOW) {

					Condition uriPredicate = BinaryCondition.like(myColumnUri, generatePlaceholder(createLeftMatchLikeExpression(value)));
					Condition hashAndUriPredicate = combineWithHashIdentityPredicate(getResourceType(), theParamName, uriPredicate);
					codePredicates.add(hashAndUriPredicate);

				} else {

					Condition uriPredicate = null;
					if (theOperation == null || theOperation == SearchFilterParser.CompareOperation.eq) {
						long hashUri = ResourceIndexedSearchParamUri.calculateHashUri(getPartitionSettings(), getRequestPartitionId(), getResourceType(), theParamName, value);
						uriPredicate = BinaryCondition.equalTo(myColumnHashUri, generatePlaceholder(hashUri));
						predicateIsHash = true;
					} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
						uriPredicate = BinaryCondition.notEqualTo(myColumnUri, generatePlaceholder(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.co) {
						uriPredicate = BinaryCondition.like(myColumnUri, generatePlaceholder(createLeftAndRightMatchLikeExpression(value)));
					} else if (theOperation == SearchFilterParser.CompareOperation.gt) {
						uriPredicate = BinaryCondition.greaterThan(myColumnUri, generatePlaceholder(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.lt) {
						uriPredicate = BinaryCondition.lessThan(myColumnUri, generatePlaceholder(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.ge) {
						uriPredicate = BinaryCondition.greaterThanOrEq(myColumnUri, generatePlaceholder(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.le) {
						uriPredicate = BinaryCondition.lessThanOrEq(myColumnUri, generatePlaceholder(value));
					} else if (theOperation == SearchFilterParser.CompareOperation.sw) {
						uriPredicate = BinaryCondition.like(myColumnUri, generatePlaceholder(createLeftMatchLikeExpression(value)));
					} else if (theOperation == SearchFilterParser.CompareOperation.ew) {
						uriPredicate = BinaryCondition.like(myColumnUri, generatePlaceholder(createRightMatchLikeExpression(value)));
					} else {
						throw new IllegalArgumentException(Msg.code(1226) + String.format("Unsupported operator specified in _filter clause, %s",
							theOperation.toString()));
					}

					codePredicates.add(uriPredicate);
				}

			} else {
				throw new IllegalArgumentException(Msg.code(1227) + "Invalid URI type: " + nextOr.getClass());
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
		if (predicateIsHash) {
			return orPredicate;
		} else {
			return combineWithHashIdentityPredicate(getResourceType(), theParamName, orPredicate);
		}

	}

	public DbColumn getColumnValue() {
		return myColumnUri;
	}
}
