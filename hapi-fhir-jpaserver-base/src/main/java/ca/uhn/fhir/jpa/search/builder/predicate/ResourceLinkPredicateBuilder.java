/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.sql.ColumnTupleObject;
import ca.uhn.fhir.jpa.search.builder.sql.JpaPidValueTuples;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.SearchForIdsParams.with;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TYPE;
import static ca.uhn.fhir.rest.api.Constants.VALID_MODIFIERS;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ResourceLinkPredicateBuilder extends BaseJoiningPredicateBuilder implements ICanMakeMissingParamPredicate {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceLinkPredicateBuilder.class);
	private static final Pattern MODIFIER_REPLACE_PATTERN = Pattern.compile(".*:");
	private final DbColumn myColumnSrcType;
	private final DbColumn myColumnSrcPath;
	private final DbColumn myColumnTargetResourceId;
	private final DbColumn myColumnTargetResourceUrl;
	private final DbColumn myColumnSrcResourceId;
	private final DbColumn myColumnTargetResourceType;
	private final QueryStack myQueryStack;
	private final boolean myReversed;

	private final DbColumn myColumnTargetPartitionId;
	private final DbColumn myColumnSrcPartitionId;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MatchUrlService myMatchUrlService;

	/**
	 * Constructor
	 */
	public ResourceLinkPredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theSearchSqlBuilder, boolean theReversed) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_LINK"));
		myColumnSrcResourceId = getTable().addColumn("SRC_RESOURCE_ID");
		myColumnSrcPartitionId = getTable().addColumn("PARTITION_ID");
		myColumnSrcType = getTable().addColumn("SOURCE_RESOURCE_TYPE");
		myColumnSrcPath = getTable().addColumn("SRC_PATH");
		myColumnTargetResourceId = getTable().addColumn("TARGET_RESOURCE_ID");
		myColumnTargetPartitionId = getTable().addColumn("TARGET_RES_PARTITION_ID");
		myColumnTargetResourceUrl = getTable().addColumn("TARGET_RESOURCE_URL");
		myColumnTargetResourceType = getTable().addColumn("TARGET_RESOURCE_TYPE");

		myReversed = theReversed;
		myQueryStack = theQueryStack;
	}

	@Override
	public DbColumn getResourceTypeColumn() {
		if (myReversed) {
			return myColumnTargetResourceType;
		} else {
			return myColumnSrcType;
		}
	}

	public DbColumn getColumnSourcePath() {
		return myColumnSrcPath;
	}

	public DbColumn getColumnTargetResourceId() {
		return myColumnTargetResourceId;
	}

	public DbColumn getColumnTargetPartitionId() {
		return myColumnTargetPartitionId;
	}

	public DbColumn[] getJoinColumnsForTarget() {
		return getSearchQueryBuilder().toJoinColumns(getColumnTargetPartitionId(), getColumnTargetResourceId());
	}

	public DbColumn[] getJoinColumnsForSource() {
		return getSearchQueryBuilder().toJoinColumns(getPartitionIdColumn(), myColumnSrcResourceId);
	}

	/**
	 * Note that this may return the SRC_RESOURCE_ID or TGT_RESOURCE_ID depending
	 * on whether we're building a forward or reverse link. If you need a specific
	 * one of these, use {@link #getJoinColumnsForSource()} or {@link #getJoinColumnsForTarget()}.
	 */
	@Override
	public DbColumn[] getJoinColumns() {
		return super.getJoinColumns();
	}

	public DbColumn getColumnSrcResourceId() {
		return myColumnSrcResourceId;
	}

	public DbColumn getColumnSrcPartitionId() {
		return myColumnSrcPartitionId;
	}

	public DbColumn getColumnTargetResourceType() {
		return myColumnTargetResourceType;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		if (myReversed) {
			return myColumnTargetResourceId;
		} else {
			return myColumnSrcResourceId;
		}
	}

	public Condition createPredicate(
			RequestDetails theRequest,
			String theResourceType,
			String theParamName,
			List<String> theQualifiers,
			List<? extends IQueryParameterType> theReferenceOrParamList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {

		List<IIdType> targetIds = new ArrayList<>();
		List<String> targetQualifiedUrls = new ArrayList<>();

		for (int orIdx = 0; orIdx < theReferenceOrParamList.size(); orIdx++) {
			IQueryParameterType nextOr = theReferenceOrParamList.get(orIdx);

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;

				if (isBlank(ref.getChain())) {

					/*
					 * Handle non-chained search, e.g. Patient?organization=Organization/123
					 */

					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);

					if (dt.hasBaseUrl()) {
						if (myStorageSettings.getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
							targetIds.add(dt);
						} else {
							targetQualifiedUrls.add(dt.getValue());
						}
					} else {
						validateModifierUse(theRequest, theResourceType, ref);
						validateResourceTypeInReferenceParam(ref.getResourceType());
						targetIds.add(dt);
					}

				} else {

					/*
					 * Handle chained search, e.g. Patient?organization.name=Kwik-e-mart
					 */

					return addPredicateReferenceWithChain(
							theResourceType,
							theParamName,
							theQualifiers,
							theReferenceOrParamList,
							ref,
							theRequest,
							theRequestPartitionId);
				}

			} else {
				throw new IllegalArgumentException(
						Msg.code(1241) + "Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}
		}

		for (IIdType next : targetIds) {
			if (!next.hasResourceType()) {
				warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, null);
			}
		}

		List<String> pathsToMatch = createResourceLinkPaths(theResourceType, theParamName, theQualifiers);
		boolean inverse;
		if ((theOperation == null) || (theOperation == SearchFilterParser.CompareOperation.eq)) {
			inverse = false;
		} else {
			inverse = true;
		}

		List<JpaPid> pids = myIdHelperService.resolveResourcePids(
				theRequestPartitionId,
				targetIds,
				ResolveIdentityMode.includeDeleted().cacheOk());
		List<Long> targetPidList = pids.stream().map(JpaPid::getId).collect(Collectors.toList());

		if (targetPidList.isEmpty() && targetQualifiedUrls.isEmpty()) {
			setMatchNothing();
			return null;
		} else {
			Condition retVal = createPredicateReference(inverse, pathsToMatch, targetPidList, targetQualifiedUrls);
			return combineWithRequestPartitionIdPredicate(getRequestPartitionId(), retVal);
		}
	}

	private void validateModifierUse(RequestDetails theRequest, String theResourceType, ReferenceParam theRef) {
		try {
			final String resourceTypeFromRef = theRef.getResourceType();
			if (StringUtils.isEmpty(resourceTypeFromRef)) {
				return;
			}
			// TODO: LD: unless we do this, ResourceProviderR4Test#testSearchWithSlashes will fail due to its
			// derived-from: syntax
			getFhirContext().getResourceDefinition(resourceTypeFromRef);
		} catch (DataFormatException e) {
			final List<String> nonMatching = Optional.ofNullable(theRequest)
					.map(RequestDetails::getParameters)
					.map(params -> params.keySet().stream()
							.filter(mod -> mod.contains(":"))
							.map(MODIFIER_REPLACE_PATTERN::matcher)
							.map(pattern -> pattern.replaceAll(":"))
							.filter(mod -> !VALID_MODIFIERS.contains(mod))
							.distinct()
							.collect(Collectors.toUnmodifiableList()))
					.orElse(Collections.emptyList());

			if (!nonMatching.isEmpty()) {
				final String msg = getFhirContext()
						.getLocalizer()
						.getMessageSanitized(
								SearchCoordinatorSvcImpl.class,
								"invalidUseOfSearchIdentifier",
								nonMatching,
								theResourceType,
								VALID_MODIFIERS);
				throw new InvalidRequestException(Msg.code(2498) + msg);
			}
		}
	}

	private void validateResourceTypeInReferenceParam(final String theResourceType) {
		if (StringUtils.isEmpty(theResourceType)) {
			return;
		}

		try {
			getFhirContext().getResourceDefinition(theResourceType);
		} catch (DataFormatException e) {
			throw newInvalidResourceTypeException(theResourceType);
		}
	}

	private Condition createPredicateReference(
			boolean theInverse,
			List<String> thePathsToMatch,
			List<Long> theTargetPidList,
			List<String> theTargetQualifiedUrls) {

		Condition targetPidCondition = null;
		if (!theTargetPidList.isEmpty()) {
			List<String> placeholders = generatePlaceholders(theTargetPidList);
			targetPidCondition =
					QueryParameterUtils.toEqualToOrInPredicate(myColumnTargetResourceId, placeholders, theInverse);
		}

		Condition targetUrlsCondition = null;
		if (!theTargetQualifiedUrls.isEmpty()) {
			List<String> placeholders = generatePlaceholders(theTargetQualifiedUrls);
			targetUrlsCondition =
					QueryParameterUtils.toEqualToOrInPredicate(myColumnTargetResourceUrl, placeholders, theInverse);
		}

		Condition joinedCondition;
		if (targetPidCondition != null && targetUrlsCondition != null) {
			joinedCondition = ComboCondition.or(targetPidCondition, targetUrlsCondition);
		} else if (targetPidCondition != null) {
			joinedCondition = targetPidCondition;
		} else {
			joinedCondition = targetUrlsCondition;
		}

		Condition pathPredicate = createPredicateSourcePaths(thePathsToMatch);
		joinedCondition = ComboCondition.and(pathPredicate, joinedCondition);

		return joinedCondition;
	}

	@Nonnull
	public Condition createPredicateSourcePaths(List<String> thePathsToMatch) {
		return QueryParameterUtils.toEqualToOrInPredicate(myColumnSrcPath, generatePlaceholders(thePathsToMatch));
	}

	public Condition createPredicateSourcePaths(String theResourceName, String theParamName) {
		List<String> pathsToMatch = createResourceLinkPaths(theResourceName, theParamName, Collections.emptyList());
		return createPredicateSourcePaths(pathsToMatch);
	}

	private void warnAboutPerformanceOnUnqualifiedResources(
			String theParamName, RequestDetails theRequest, @Nullable List<String> theCandidateTargetTypes) {
		StringBuilder builder = new StringBuilder();
		builder.append("This search uses an unqualified resource(a parameter in a chain without a resource type). ");
		builder.append("This is less efficient than using a qualified type. ");
		if (theCandidateTargetTypes != null) {
			builder.append("[" + theParamName + "] resolves to ["
					+ theCandidateTargetTypes.stream().collect(Collectors.joining(",")) + "].");
			builder.append("If you know what you're looking for, try qualifying it using the form ");
			builder.append(theCandidateTargetTypes.stream()
					.map(cls -> "[" + cls + ":" + theParamName + "]")
					.collect(Collectors.joining(" or ")));
		} else {
			builder.append("If you know what you're looking for, try qualifying it using the form: '");
			builder.append(theParamName).append(":[resourceType]=[id] or ");
			builder.append(theParamName).append("=[resourceType]/[id]");
			builder.append("'");
		}
		String message = builder.toString();
		StorageProcessingMessage msg = new StorageProcessingMessage().setMessage(message);

		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
		if (compositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING)) {
			HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
			compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}

	/**
	 * This is for handling queries like the following: /Observation?device.identifier=urn:system|foo in which we use a chain
	 * on the device.
	 */
	private Condition addPredicateReferenceWithChain(
			String theResourceName,
			String theParamName,
			List<String> theQualifiers,
			List<? extends IQueryParameterType> theList,
			ReferenceParam theReferenceParam,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {

		/*
		 * Which resource types can the given chained parameter actually link to? This might be a list
		 * where the chain is unqualified, as in: Observation?subject.identifier=(...)
		 * since subject can link to several possible target types.
		 *
		 * If the user has qualified the chain, as in: Observation?subject:Patient.identifier=(...)
		 * this is just a simple 1-entry list.
		 */
		final List<String> resourceTypes =
				determineCandidateResourceTypesForChain(theResourceName, theParamName, theReferenceParam);

		/*
		 * Handle chain on _type
		 */
		if (PARAM_TYPE.equals(theReferenceParam.getChain())) {

			List<String> pathsToMatch = createResourceLinkPaths(theResourceName, theParamName, theQualifiers);
			Condition typeCondition = createPredicateSourcePaths(pathsToMatch);

			String typeValue = theReferenceParam.getValue();

			validateResourceTypeInReferenceParam(typeValue);
			if (!resourceTypes.contains(typeValue)) {
				throw newInvalidTargetTypeForChainException(theResourceName, theParamName, typeValue);
			}

			Condition condition = BinaryCondition.equalTo(
					myColumnTargetResourceType, generatePlaceholder(theReferenceParam.getValue()));

			return QueryParameterUtils.toAndPredicate(typeCondition, condition);
		}

		boolean foundChainMatch = false;
		List<String> candidateTargetTypes = new ArrayList<>();
		List<Condition> orPredicates = new ArrayList<>();
		boolean paramInverted = false;
		QueryStack childQueryFactory = myQueryStack.newChildQueryFactoryWithFullBuilderReuse();

		String chain = theReferenceParam.getChain();

		String remainingChain = null;
		int chainDotIndex = chain.indexOf('.');
		if (chainDotIndex != -1) {
			remainingChain = chain.substring(chainDotIndex + 1);
			chain = chain.substring(0, chainDotIndex);
		}

		int qualifierIndex = chain.indexOf(':');
		String qualifier = null;
		if (qualifierIndex != -1) {
			qualifier = chain.substring(qualifierIndex);
			chain = chain.substring(0, qualifierIndex);
		}

		boolean isMeta = ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(chain);

		for (String nextType : resourceTypes) {

			RuntimeResourceDefinition typeDef = getFhirContext().getResourceDefinition(nextType);
			String subResourceName = typeDef.getName();

			IDao dao = myDaoRegistry.getResourceDao(nextType);
			if (dao == null) {
				ourLog.debug("Don't have a DAO for type {}", nextType);
				continue;
			}

			RuntimeSearchParam param = null;
			if (!isMeta) {
				param = mySearchParamRegistry.getActiveSearchParam(
						nextType, chain, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
				if (param == null) {
					ourLog.debug("Type {} doesn't have search param {}", nextType, param);
					continue;
				}
			}

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			for (IQueryParameterType next : theList) {
				String nextValue = next.getValueAsQueryToken(getFhirContext());
				IQueryParameterType chainValue = mapReferenceChainToRawParamType(
						remainingChain, param, theParamName, qualifier, nextType, chain, isMeta, nextValue);
				if (chainValue == null) {
					continue;
				}

				// For the token param, if it's a :not modifier, need switch OR to AND
				if (!paramInverted && chainValue instanceof TokenParam) {
					if (((TokenParam) chainValue).getModifier() == TokenParamModifier.NOT) {
						paramInverted = true;
					}
				}
				foundChainMatch = true;
				orValues.add(chainValue);
			}

			if (!foundChainMatch) {
				throw new InvalidRequestException(Msg.code(1242)
						+ getFhirContext()
								.getLocalizer()
								.getMessage(
										BaseStorageDao.class,
										"invalidParameterChain",
										theParamName + '.' + theReferenceParam.getChain()));
			}

			candidateTargetTypes.add(nextType);

			List<Condition> andPredicates = new ArrayList<>();

			List<List<IQueryParameterType>> chainParamValues = Collections.singletonList(orValues);
			andPredicates.add(
					childQueryFactory.searchForIdsWithAndOr(with().setSourceJoinColumn(getJoinColumnsForTarget())
							.setResourceName(subResourceName)
							.setParamName(chain)
							.setAndOrParams(chainParamValues)
							.setRequest(theRequest)
							.setRequestPartitionId(theRequestPartitionId)));

			orPredicates.add(QueryParameterUtils.toAndPredicate(andPredicates));
		}

		if (candidateTargetTypes.isEmpty()) {
			throw new InvalidRequestException(Msg.code(1243)
					+ getFhirContext()
							.getLocalizer()
							.getMessage(
									BaseStorageDao.class,
									"invalidParameterChain",
									theParamName + '.' + theReferenceParam.getChain()));
		}

		if (candidateTargetTypes.size() > 1) {
			warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, candidateTargetTypes);
		}

		// If :not modifier for a token, switch OR with AND in the multi-type case
		Condition multiTypePredicate;
		if (paramInverted) {
			multiTypePredicate = QueryParameterUtils.toAndPredicate(orPredicates);
		} else {
			multiTypePredicate = QueryParameterUtils.toOrPredicate(orPredicates);
		}

		List<String> pathsToMatch = createResourceLinkPaths(theResourceName, theParamName, theQualifiers);
		Condition pathPredicate = createPredicateSourcePaths(pathsToMatch);
		return QueryParameterUtils.toAndPredicate(pathPredicate, multiTypePredicate);
	}

	@Nonnull
	private List<String> determineCandidateResourceTypesForChain(
			String theResourceName, String theParamName, ReferenceParam theReferenceParam) {
		final List<Class<? extends IBaseResource>> resourceTypes;
		if (!theReferenceParam.hasResourceType()) {

			resourceTypes = determineResourceTypes(Collections.singleton(theResourceName), theParamName);

			if (resourceTypes.isEmpty()) {
				RuntimeSearchParam searchParamByName = mySearchParamRegistry.getActiveSearchParam(
						theResourceName, theParamName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
				if (searchParamByName == null) {
					throw new InternalErrorException(Msg.code(1244) + "Could not find parameter " + theParamName);
				}
				String paramPath = searchParamByName.getPath();
				if (paramPath.endsWith(".as(Reference)")) {
					paramPath = paramPath.substring(0, paramPath.length() - ".as(Reference)".length()) + "Reference";
				}

				if (paramPath.contains(".extension(")) {
					int startIdx = paramPath.indexOf(".extension(");
					int endIdx = paramPath.indexOf(')', startIdx);
					if (startIdx != -1 && endIdx != -1) {
						paramPath = paramPath.substring(0, startIdx + 10) + paramPath.substring(endIdx + 1);
					}
				}

				Class<? extends IBaseResource> resourceType =
						getFhirContext().getResourceDefinition(theResourceName).getImplementingClass();
				BaseRuntimeChildDefinition def = getFhirContext().newTerser().getDefinition(resourceType, paramPath);
				if (def instanceof RuntimeChildChoiceDefinition) {
					RuntimeChildChoiceDefinition choiceDef = (RuntimeChildChoiceDefinition) def;
					resourceTypes.addAll(choiceDef.getResourceTypes());
				} else if (def instanceof RuntimeChildResourceDefinition) {
					RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
					resourceTypes.addAll(resDef.getResourceTypes());
					if (resourceTypes.size() == 1) {
						if (resourceTypes.get(0).isInterface()) {
							throw new InvalidRequestException(
									Msg.code(1245) + "Unable to perform search for unqualified chain '" + theParamName
											+ "' as this SearchParameter does not declare any target types. Add a qualifier of the form '"
											+ theParamName + ":[ResourceType]' to perform this search.");
						}
					}
				} else {
					throw new ConfigurationException(Msg.code(1246) + "Property " + paramPath + " of type "
							+ getResourceType() + " is not a resource: " + def.getClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				for (BaseRuntimeElementDefinition<?> next : getFhirContext().getElementDefinitions()) {
					if (next instanceof RuntimeResourceDefinition) {
						RuntimeResourceDefinition nextResDef = (RuntimeResourceDefinition) next;
						resourceTypes.add(nextResDef.getImplementingClass());
					}
				}
			}

		} else {

			try {
				RuntimeResourceDefinition resDef =
						getFhirContext().getResourceDefinition(theReferenceParam.getResourceType());
				resourceTypes = new ArrayList<>(1);
				resourceTypes.add(resDef.getImplementingClass());
			} catch (DataFormatException e) {
				throw newInvalidResourceTypeException(theReferenceParam.getResourceType());
			}
		}

		return resourceTypes.stream()
				.map(t -> getFhirContext().getResourceType(t))
				.collect(Collectors.toList());
	}

	private List<Class<? extends IBaseResource>> determineResourceTypes(
			Set<String> theResourceNames, String theParamNameChain) {
		int linkIndex = theParamNameChain.indexOf('.');
		if (linkIndex == -1) {
			Set<Class<? extends IBaseResource>> resourceTypes = new HashSet<>();
			for (String resourceName : theResourceNames) {
				RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(
						resourceName, theParamNameChain, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);

				if (param != null && param.hasTargets()) {
					Set<String> targetTypes = param.getTargets();
					for (String next : targetTypes) {
						resourceTypes.add(
								getFhirContext().getResourceDefinition(next).getImplementingClass());
					}
				}
			}
			return new ArrayList<>(resourceTypes);
		} else {
			String paramNameHead = theParamNameChain.substring(0, linkIndex);
			String paramNameTail = theParamNameChain.substring(linkIndex + 1);
			Set<String> targetResourceTypeNames = new HashSet<>();
			for (String resourceName : theResourceNames) {
				RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(
						resourceName, paramNameHead, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);

				if (param != null && param.hasTargets()) {
					targetResourceTypeNames.addAll(param.getTargets());
				}
			}
			return determineResourceTypes(targetResourceTypeNames, paramNameTail);
		}
	}

	public List<String> createResourceLinkPaths(
			String theResourceName, String theParamName, List<String> theParamQualifiers) {
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(
				theResourceName, theParamName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
		if (param != null) {
			List<String> path = param.getPathsSplit();

			/*
			 * SearchParameters can declare paths on multiple resource
			 * types. Here we only want the ones that actually apply.
			 */
			path = new ArrayList<>(path);

			ListIterator<String> iter = path.listIterator();
			while (iter.hasNext()) {
				String nextPath = trim(iter.next());
				if (!nextPath.contains(theResourceName + ".")) {
					iter.remove();
				}
			}

			return path;
		}

		boolean containsChain = theParamName.contains(".");
		if (containsChain) {
			int linkIndex = theParamName.indexOf('.');
			String paramNameHead = theParamName.substring(0, linkIndex);
			String paramNameTail = theParamName.substring(linkIndex + 1);
			String qualifier = !theParamQualifiers.isEmpty() ? theParamQualifiers.get(0) : null;
			List<String> nextQualifiersList = !theParamQualifiers.isEmpty()
					? theParamQualifiers.subList(1, theParamQualifiers.size())
					: List.of();

			param = mySearchParamRegistry.getActiveSearchParam(
					theResourceName, paramNameHead, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
			if (param != null) {
				Set<String> tailPaths = param.getTargets().stream()
						.filter(t -> isBlank(qualifier) || qualifier.equals(t))
						.map(t -> createResourceLinkPaths(t, paramNameTail, nextQualifiersList))
						.flatMap(Collection::stream)
						.map(t -> t.substring(t.indexOf('.') + 1))
						.collect(Collectors.toSet());

				List<String> path = param.getPathsSplit();

				/*
				 * SearchParameters can declare paths on multiple resource
				 * types. Here we only want the ones that actually apply.
				 * Then append all the tail paths to each of the applicable head paths
				 */
				return path.stream()
						.map(String::trim)
						.filter(t -> t.startsWith(theResourceName + "."))
						.map(head -> tailPaths.stream()
								.map(tail -> head + "." + tail)
								.collect(Collectors.toSet()))
						.flatMap(Collection::stream)
						.collect(Collectors.toList());
			}
		}

		// This can happen during recursion, if not all the possible target types of one link in the chain
		// support the next link
		return new ArrayList<>();
	}

	private IQueryParameterType mapReferenceChainToRawParamType(
			String remainingChain,
			RuntimeSearchParam param,
			String theParamName,
			String qualifier,
			String nextType,
			String chain,
			boolean isMeta,
			String resourceId) {
		IQueryParameterType chainValue;
		if (remainingChain != null) {
			if (param == null || param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				ourLog.debug(
						"Type {} parameter {} is not a reference, can not chain {}", nextType, chain, remainingChain);
				return null;
			}

			chainValue = new ReferenceParam();
			chainValue.setValueAsQueryToken(getFhirContext(), theParamName, qualifier, resourceId);
			((ReferenceParam) chainValue).setChain(remainingChain);
		} else if (isMeta) {
			IQueryParameterType type = myMatchUrlService.newInstanceType(chain);
			type.setValueAsQueryToken(getFhirContext(), theParamName, qualifier, resourceId);
			chainValue = type;
		} else {
			chainValue = myQueryStack.newParameterInstance(param, qualifier, resourceId);
		}

		return chainValue;
	}

	@Nonnull
	private InvalidRequestException newInvalidTargetTypeForChainException(
			String theResourceName, String theParamName, String theTypeValue) {
		String searchParamName = theResourceName + ":" + theParamName;
		String msg = getFhirContext()
				.getLocalizer()
				.getMessage(
						ResourceLinkPredicateBuilder.class, "invalidTargetTypeForChain", theTypeValue, searchParamName);
		return new InvalidRequestException(Msg.code(2495) + msg);
	}

	@Nonnull
	private InvalidRequestException newInvalidResourceTypeException(String theResourceType) {
		String msg = getFhirContext()
				.getLocalizer()
				.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", theResourceType);
		throw new InvalidRequestException(Msg.code(1250) + msg);
	}

	@Nonnull
	public Condition createEverythingPredicate(
			String theResourceName, List<String> theSourceResourceNames, JpaPid... theTargetPids) {
		Condition condition;

		if (theTargetPids != null && theTargetPids.length >= 1) {
			// if resource ids are provided, we'll create the predicate
			// with ids in or equal to this value
			if (getSearchQueryBuilder().isIncludePartitionIdInJoins()) {
				Object left = ColumnTupleObject.from(getJoinColumnsForTarget());
				JpaPidValueTuples right = JpaPidValueTuples.from(getSearchQueryBuilder(), theTargetPids);
				condition = new InCondition(left, right);
			} else {
				condition = QueryParameterUtils.toEqualToOrInPredicate(
						myColumnTargetResourceId, generatePlaceholders(JpaPid.toLongList(theTargetPids)));
			}
		} else {
			// ... otherwise we look for resource types
			condition = BinaryCondition.equalTo(myColumnTargetResourceType, generatePlaceholder(theResourceName));
		}

		if (!theSourceResourceNames.isEmpty()) {
			// if source resources are provided, add on predicate for _type operation
			Condition typeCondition = QueryParameterUtils.toEqualToOrInPredicate(
					myColumnSrcType, generatePlaceholders(theSourceResourceNames));
			condition = QueryParameterUtils.toAndPredicate(List.of(condition, typeCondition));
		}

		return condition;
	}

	@Override
	public Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams) {
		SelectQuery subquery = new SelectQuery();
		subquery.addCustomColumns(1);
		subquery.addFromTable(getTable());

		String resourceType = theParams.getResourceTablePredicateBuilder().getResourceType();
		RuntimeSearchParam paramDefinition = mySearchParamRegistry.getRuntimeSearchParam(
				resourceType, theParams.getParamName(), ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
		List<String> pathList = paramDefinition.getPathsSplitForResourceType(resourceType);

		Condition subQueryCondition = ComboCondition.and(
				BinaryCondition.equalTo(
						getResourceIdColumn(),
						theParams.getResourceTablePredicateBuilder().getResourceIdColumn()),
				BinaryCondition.equalTo(getResourceTypeColumn(), generatePlaceholder(resourceType)),
				ComboCondition.or(pathList.stream()
						.map(path -> BinaryCondition.equalTo(getColumnSourcePath(), generatePlaceholder(path)))
						.toArray(BinaryCondition[]::new)));

		subquery.addCondition(subQueryCondition);

		Condition unaryCondition = UnaryCondition.exists(subquery);
		if (theParams.isMissing()) {
			unaryCondition = new NotCondition(unaryCondition);
		}

		return combineWithRequestPartitionIdPredicate(theParams.getRequestPartitionId(), unaryCondition);
	}

	@VisibleForTesting
	void setSearchParamRegistryForUnitTest(ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
	}

	@VisibleForTesting
	void setIdHelperServiceForUnitTest(IIdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}
}
