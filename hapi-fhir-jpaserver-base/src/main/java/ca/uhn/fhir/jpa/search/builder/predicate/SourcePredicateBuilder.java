/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftMatchLikeExpression;

public class SourcePredicateBuilder extends BaseJoiningPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SourcePredicateBuilder.class);
	private final DbColumn myColumnSourceUri;
	private final DbColumn myColumnRequestId;
	private final DbColumn myResourceIdColumn;

	/**
	 * Constructor
	 */
	public SourcePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
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

	public Condition createPredicateSourceUriWithModifiers(
			IQueryParameterType theQueryParameter, JpaStorageSettings theStorageSetting, String theSourceUri) {
		if (theQueryParameter.getMissing() != null) {
			if (theQueryParameter.getMissing()) {
				return UnaryCondition.isNull(myColumnSourceUri);
			} else {
				return UnaryCondition.isNotNull(myColumnSourceUri);
			}
		} else if (theQueryParameter instanceof UriParam && theQueryParameter.getQueryParameterQualifier() != null) {
			UriParam uriParam = (UriParam) theQueryParameter;

			switch (uriParam.getQualifier()) {
				case ABOVE:
					List<String> aboveUriCandidates = getAboveUriCandidates(theSourceUri);
					List<String> aboveUriPlaceholders = generatePlaceholders(aboveUriCandidates);
					return QueryParameterUtils.toEqualToOrInPredicate(myColumnSourceUri, aboveUriPlaceholders);
				case BELOW:
					String belowLikeExpression = createLeftMatchLikeExpression(theSourceUri);
					return BinaryCondition.like(myColumnSourceUri, generatePlaceholder(belowLikeExpression));
				case CONTAINS:
					if (theStorageSetting.isAllowContainsSearches()) {
						String containsLikeExpression = createLeftAndRightMatchLikeExpression(theSourceUri);
						return BinaryCondition.like(myColumnSourceUri, generatePlaceholder(containsLikeExpression));
					} else {
						throw new MethodNotAllowedException(
								Msg.code(2417) + ":contains modifier is disabled on this server");
					}
				default:
					throw new InvalidRequestException(Msg.code(2418)
							+ String.format(
									"Unsupported qualifier specified, qualifier=%s",
									uriParam.getQualifier().getValue()));
			}
		} else {
			return createPredicateSourceUri(theSourceUri);
		}
	}

	public Condition createPredicateRequestId(String theRequestId) {
		return BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId));
	}

	/**
	 * Creates list of URI candidates for search with :above modifier
	 * Example input: http://[host]/[pathPart1]/[pathPart2]
	 * Example output: http://[host], http://[host]/[pathPart1], http://[host]/[pathPart1]/[pathPart2]
	 *
	 * @param theUri String URI parameter
	 * @return List of URI candidates
	 */
	private List<String> getAboveUriCandidates(String theUri) {
		List<String> candidates = new ArrayList<>();
		try {
			URI uri = new URI(theUri);

			if (uri.getScheme() == null || uri.getHost() == null) {
				throwInvalidRequestExceptionForNotValidUri(theUri, null);
			}
			StringBuilder sb =
					new StringBuilder().append(uri.getScheme()).append("://").append(uri.getHost());

			candidates.add(sb.toString());

			String[] pathParts = uri.getPath().split("/");
			Arrays.stream(pathParts)
					.filter(part -> !part.isEmpty())
					.forEach(part -> candidates.add(sb.append("/").append(part).toString()));

		} catch (URISyntaxException e) {
			throwInvalidRequestExceptionForNotValidUri(theUri, e);
		}
		return candidates;
	}

	public void throwInvalidRequestExceptionForNotValidUri(String theUri, Exception theCause) {
		throw new InvalidRequestException(
				Msg.code(2419) + String.format("%s is not valid URI: %s", Constants.PARAM_SOURCE, theUri), theCause);
	}
}
