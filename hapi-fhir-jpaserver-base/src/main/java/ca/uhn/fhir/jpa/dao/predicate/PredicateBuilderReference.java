package ca.uhn.fhir.jpa.dao.predicate;

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
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.fromOperation;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@Component
@Scope("prototype")
public
class PredicateBuilderReference extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderReference.class);
	private final PredicateBuilder myPredicateBuilder;
	@Autowired
	IIdHelperService myIdHelperService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	PartitionSettings myPartitionSettings;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public PredicateBuilderReference(LegacySearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
		super(theSearchBuilder);
		myPredicateBuilder = thePredicateBuilder;
	}

	/**
	 * Add reference predicate to the current search
	 */

	public Predicate addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation,
											RequestDetails theRequest,
											RequestPartitionId theRequestPartitionId) {

		// This just to ensure the chain has been split correctly
		assert theParamName.contains(".") == false;

		if ((operation != null) &&
			(operation != SearchFilterParser.CompareOperation.eq) &&
			(operation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException(Msg.code(1008) + "Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForReference(theResourceName, theParamName, theList.get(0).getMissing(), theRequestPartitionId);
			return null;
		}

		From<?, ResourceLink> join = myQueryStack.createJoin(SearchBuilderJoinEnum.REFERENCE, theParamName);

		List<IIdType> targetIds = new ArrayList<>();
		List<String> targetQualifiedUrls = new ArrayList<>();

		for (int orIdx = 0; orIdx < theList.size(); orIdx++) {
			IQueryParameterType nextOr = theList.get(orIdx);

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;

				if (isBlank(ref.getChain())) {

					/*
					 * Handle non-chained search, e.g. Patient?organization=Organization/123
					 */

					IIdType dt = new IdDt(ref.getBaseUrl(), ref.getResourceType(), ref.getIdPart(), null);

					if (dt.hasBaseUrl()) {
						if (myDaoConfig.getTreatBaseUrlsAsLocal().contains(dt.getBaseUrl())) {
							dt = dt.toUnqualified();
							targetIds.add(dt);
						} else {
							targetQualifiedUrls.add(dt.getValue());
						}
					} else {
						targetIds.add(dt);
					}

				} else {

					/*
					 * Handle chained search, e.g. Patient?organization.name=Kwik-e-mart
					 */

					return addPredicateReferenceWithChain(theResourceName, theParamName, theList, join, new ArrayList<>(), ref, theRequest, theRequestPartitionId);

				}

			} else {
				throw new IllegalArgumentException(Msg.code(1009) + "Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}

		List<Predicate> codePredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, join, codePredicates);

		for (IIdType next : targetIds) {
			if (!next.hasResourceType()) {
				warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, null);
			}
		}

		// Resources by ID
		List<ResourcePersistentId> targetPids = myIdHelperService.resolveResourcePersistentIdsWithCache(theRequestPartitionId, targetIds);
		if (!targetPids.isEmpty()) {
			ourLog.debug("Searching for resource link with target PIDs: {}", targetPids);
			Predicate pathPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			} else {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join).not();
			}
			Predicate pidPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				if (targetPids.size() == 1) {
					pidPredicate = myCriteriaBuilder.equal(join.get("myTargetResourcePid").as(Long.class), targetPids.get(0).getIdAsLong());
				} else {
					pidPredicate = join.get("myTargetResourcePid").in(ResourcePersistentId.toLongList(targetPids));
				}
			} else {
				pidPredicate = join.get("myTargetResourcePid").in(ResourcePersistentId.toLongList(targetPids)).not();
			}
			codePredicates.add(myCriteriaBuilder.and(pathPredicate, pidPredicate));
		}

		// Resources by fully qualified URL
		if (!targetQualifiedUrls.isEmpty()) {
			ourLog.debug("Searching for resource link with target URLs: {}", targetQualifiedUrls);
			Predicate pathPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join);
			} else {
				pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, join).not();
			}
			Predicate pidPredicate;
			if ((operation == null) || (operation == SearchFilterParser.CompareOperation.eq)) {
				pidPredicate = join.get("myTargetResourceUrl").in(targetQualifiedUrls);
			} else {
				pidPredicate = join.get("myTargetResourceUrl").in(targetQualifiedUrls).not();
			}
			codePredicates.add(myCriteriaBuilder.and(pathPredicate, pidPredicate));
		}

		if (codePredicates.size() > 0) {
			Predicate predicate = myCriteriaBuilder.or(toArray(codePredicates));
			myQueryStack.addPredicateWithImplicitTypeSelection(predicate);
			return predicate;
		} else {
			return myQueryStack.addNeverMatchingPredicate();
		}
	}

	/**
	 * This is for handling queries like the following: /Observation?device.identifier=urn:system|foo in which we use a chain
	 * on the device.
	 */
	private Predicate addPredicateReferenceWithChain(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, From<?, ResourceLink> theJoin, List<Predicate> theCodePredicates, ReferenceParam theReferenceParam, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		/*
		 * Which resource types can the given chained parameter actually link to? This might be a list
		 * where the chain is unqualified, as in: Observation?subject.identifier=(...)
		 * since subject can link to several possible target types.
		 *
		 * If the user has qualified the chain, as in: Observation?subject:Patient.identifier=(...)
		 * this is just a simple 1-entry list.
		 */
		final List<Class<? extends IBaseResource>> resourceTypes = determineCandidateResourceTypesForChain(theResourceName, theParamName, theReferenceParam);

		/*
		 * Handle chain on _type
		 */
		if (Constants.PARAM_TYPE.equals(theReferenceParam.getChain())) {
			return createChainPredicateOnType(theResourceName, theParamName, theJoin, theReferenceParam, resourceTypes);
		}

		boolean foundChainMatch = false;
		List<Class<? extends IBaseResource>> candidateTargetTypes = new ArrayList<>();
		for (Class<? extends IBaseResource> nextType : resourceTypes) {
			String chain = theReferenceParam.getChain();

			String remainingChain = null;
			int chainDotIndex = chain.indexOf('.');
			if (chainDotIndex != -1) {
				remainingChain = chain.substring(chainDotIndex + 1);
				chain = chain.substring(0, chainDotIndex);
			}

			RuntimeResourceDefinition typeDef = myContext.getResourceDefinition(nextType);
			String subResourceName = typeDef.getName();

			IDao dao = myDaoRegistry.getResourceDao(nextType);
			if (dao == null) {
				ourLog.debug("Don't have a DAO for type {}", nextType.getSimpleName());
				continue;
			}

			int qualifierIndex = chain.indexOf(':');
			String qualifier = null;
			if (qualifierIndex != -1) {
				qualifier = chain.substring(qualifierIndex);
				chain = chain.substring(0, qualifierIndex);
			}

			boolean isMeta = ResourceMetaParams.RESOURCE_META_PARAMS.containsKey(chain);
			RuntimeSearchParam param = null;
			if (!isMeta) {
				param = mySearchParamRegistry.getActiveSearchParam(subResourceName, chain);
				if (param == null) {
					ourLog.debug("Type {} doesn't have search param {}", subResourceName, param);
					continue;
				}
			}

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			for (IQueryParameterType next : theList) {
				String nextValue = next.getValueAsQueryToken(myContext);
				IQueryParameterType chainValue = mapReferenceChainToRawParamType(remainingChain, param, theParamName, qualifier, nextType, chain, isMeta, nextValue);
				if (chainValue == null) {
					continue;
				}
				foundChainMatch = true;
				orValues.add(chainValue);
			}

			// If this is false, we throw an exception below so no sense doing any further processing
			if (foundChainMatch) {
				Subquery<Long> subQ = createLinkSubquery(chain, subResourceName, orValues, theRequest, theRequestPartitionId);

				Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
				Predicate pidPredicate = theJoin.get("myTargetResourcePid").in(subQ);
				Predicate andPredicate = myCriteriaBuilder.and(pathPredicate, pidPredicate);
				theCodePredicates.add(andPredicate);
				candidateTargetTypes.add(nextType);
			}
		}

		if (!foundChainMatch) {
			throw new InvalidRequestException(Msg.code(1010) + myContext.getLocalizer().getMessage(BaseStorageDao.class, "invalidParameterChain", theParamName + '.' + theReferenceParam.getChain()));
		}

		if (candidateTargetTypes.size() > 1) {
			warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, candidateTargetTypes);
		}

		Predicate predicate = myCriteriaBuilder.or(toArray(theCodePredicates));
		myQueryStack.addPredicateWithImplicitTypeSelection(predicate);
		return predicate;
	}

	private Predicate createChainPredicateOnType(String theResourceName, String theParamName, From<?, ResourceLink> theJoin, ReferenceParam theReferenceParam, List<Class<? extends IBaseResource>> theResourceTypes) {
		String typeValue = theReferenceParam.getValue();

		Class<? extends IBaseResource> wantedType;
		try {
			wantedType = myContext.getResourceDefinition(typeValue).getImplementingClass();
		} catch (DataFormatException e) {
			throw newInvalidResourceTypeException(typeValue);
		}
		if (!theResourceTypes.contains(wantedType)) {
			throw newInvalidTargetTypeForChainException(theResourceName, theParamName, typeValue);
		}

		Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
		Predicate sourceTypeParameter = myCriteriaBuilder.equal(theJoin.get("mySourceResourceType"), myResourceName);
		Predicate targetTypeParameter = myCriteriaBuilder.equal(theJoin.get("myTargetResourceType"), typeValue);

		Predicate composite = myCriteriaBuilder.and(pathPredicate, sourceTypeParameter, targetTypeParameter);
		myQueryStack.addPredicate(composite);
		return composite;
	}

	@Nonnull
	private List<Class<? extends IBaseResource>> determineCandidateResourceTypesForChain(String theResourceName, String theParamName, ReferenceParam theReferenceParam) {
		final List<Class<? extends IBaseResource>> resourceTypes;
		if (!theReferenceParam.hasResourceType()) {

			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
			resourceTypes = new ArrayList<>();

			if (param.hasTargets()) {
				Set<String> targetTypes = param.getTargets();
				for (String next : targetTypes) {
					resourceTypes.add(myContext.getResourceDefinition(next).getImplementingClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				RuntimeSearchParam searchParamByName = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
				if (searchParamByName == null) {
					throw new InternalErrorException(Msg.code(1011) + "Could not find parameter " + theParamName);
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

				BaseRuntimeChildDefinition def = myContext.newTerser().getDefinition(myResourceType, paramPath);
				if (def instanceof RuntimeChildChoiceDefinition) {
					RuntimeChildChoiceDefinition choiceDef = (RuntimeChildChoiceDefinition) def;
					resourceTypes.addAll(choiceDef.getResourceTypes());
				} else if (def instanceof RuntimeChildResourceDefinition) {
					RuntimeChildResourceDefinition resDef = (RuntimeChildResourceDefinition) def;
					resourceTypes.addAll(resDef.getResourceTypes());
					if (resourceTypes.size() == 1) {
						if (resourceTypes.get(0).isInterface()) {
							throw new InvalidRequestException(Msg.code(1012) + "Unable to perform search for unqualified chain '" + theParamName + "' as this SearchParameter does not declare any target types. Add a qualifier of the form '" + theParamName + ":[ResourceType]' to perform this search.");
						}
					}
				} else {
					throw new ConfigurationException(Msg.code(1013) + "Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				for (BaseRuntimeElementDefinition<?> next : myContext.getElementDefinitions()) {
					if (next instanceof RuntimeResourceDefinition) {
						RuntimeResourceDefinition nextResDef = (RuntimeResourceDefinition) next;
						resourceTypes.add(nextResDef.getImplementingClass());
					}
				}
			}

		} else {

			try {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theReferenceParam.getResourceType());
				resourceTypes = new ArrayList<>(1);
				resourceTypes.add(resDef.getImplementingClass());
			} catch (DataFormatException e) {
				throw newInvalidResourceTypeException(theReferenceParam.getResourceType());
			}

		}
		return resourceTypes;
	}

	private void warnAboutPerformanceOnUnqualifiedResources(String theParamName, RequestDetails theRequest, @Nullable List<Class<? extends IBaseResource>> theCandidateTargetTypes) {
		StringBuilder builder = new StringBuilder();
		builder.append("This search uses an unqualified resource(a parameter in a chain without a resource type). ");
		builder.append("This is less efficient than using a qualified type. ");
		if (theCandidateTargetTypes != null) {
			builder.append("[" + theParamName + "] resolves to [" + theCandidateTargetTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(",")) + "].");
			builder.append("If you know what you're looking for, try qualifying it using the form ");
			builder.append(theCandidateTargetTypes.stream().map(cls -> "[" + cls.getSimpleName() + ":" + theParamName + "]").collect(Collectors.joining(" or ")));
		} else {
			builder.append("If you know what you're looking for, try qualifying it using the form: '");
			builder.append(theParamName).append(":[resourceType]");
			builder.append("'");
		}
		String message = builder
			.toString();
		StorageProcessingMessage msg = new StorageProcessingMessage()
			.setMessage(message);
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(StorageProcessingMessage.class, msg);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
	}

	Predicate createResourceLinkPathPredicate(String theResourceName, String theParamName, From<?, ? extends ResourceLink> from) {
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
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

		// multiple values
		return from.get("mySourcePath").in(path);
	}

	private IQueryParameterType mapReferenceChainToRawParamType(String remainingChain, RuntimeSearchParam param, String theParamName, String qualifier, Class<? extends IBaseResource> nextType, String chain, boolean isMeta, String resourceId) {
		IQueryParameterType chainValue;
		if (remainingChain != null) {
			if (param == null || param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", nextType.getSimpleName(), chain, remainingChain);
				return null;
			}

			chainValue = new ReferenceParam();
			chainValue.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
			((ReferenceParam) chainValue).setChain(remainingChain);
		} else if (isMeta) {
			IQueryParameterType type = myMatchUrlService.newInstanceType(chain);
			type.setValueAsQueryToken(myContext, theParamName, qualifier, resourceId);
			chainValue = type;
		} else {
			chainValue = toParameterType(param, qualifier, resourceId);
		}

		return chainValue;
	}

	Subquery<Long> createLinkSubquery(String theChain, String theSubResourceName, List<IQueryParameterType> theOrValues, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		/*
		 * We're doing a chain call, so push the current query root
		 * and predicate list down and put new ones at the top of the
		 * stack and run a subquery
		 */
		RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theSubResourceName, theChain);
		if (nextParamDef != null && !theChain.startsWith("_")) {
			myQueryStack.pushIndexTableSubQuery();
		} else {
			myQueryStack.pushResourceTableSubQuery(theSubResourceName);
		}

		List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
		andOrParams.add(theOrValues);

		searchForIdsWithAndOr(theSubResourceName, theChain, andOrParams, theRequest, theRequestPartitionId);

		/*
		 * Pop the old query root and predicate list back
		 */
		return (Subquery<Long>) myQueryStack.pop();

	}

	void searchForIdsWithAndOr(String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theAndOrParams.isEmpty()) {
			return;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				myPredicateBuilder.addPredicateResourceId(theAndOrParams, theResourceName, theRequestPartitionId);
				break;

			case Constants.PARAM_HAS:
				addPredicateHas(theResourceName, theAndOrParams, theRequest, theRequestPartitionId);
				break;

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				myPredicateBuilder.addPredicateTag(theAndOrParams, theParamName, theRequestPartitionId);
				break;

			case Constants.PARAM_SOURCE:
				addPredicateSource(theAndOrParams, theRequest);
				break;

			default:

				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
				if (nextParamDef != null) {

					if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.isIncludePartitionInSearchHashes()) {
						if (theRequestPartitionId.isAllPartitions()) {
							throw new PreconditionFailedException(Msg.code(1014) + "This server is not configured to support search against all partitions");
						}
					}

					switch (nextParamDef.getParamType()) {
						case DATE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								// FT: 2021-01-18 use operation 'gt', 'ge', 'le' or 'lt' 
								// to create the predicateDate instead of generic one with operation = null 
								SearchFilterParser.CompareOperation operation = null;
								if (nextAnd.size() > 0) {
									DateParam param = (DateParam) nextAnd.get(0);
									operation = ca.uhn.fhir.jpa.search.builder.QueryStack.toOperation(param.getPrefix());
								}
								myPredicateBuilder.addPredicateDate(theResourceName, nextParamDef, nextAnd, operation, theRequestPartitionId);
							}
							break;
						case QUANTITY:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateQuantity(theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId);
							}
							break;
						case REFERENCE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicate(theResourceName, theParamName, nextAnd, null, theRequest, theRequestPartitionId);
							}
							break;
						case STRING:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateString(theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.sw, theRequestPartitionId);
							}
							break;
						case TOKEN:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									myPredicateBuilder.addPredicateCoords(theResourceName, nextParamDef, nextAnd, theRequestPartitionId);
								} else {
									myPredicateBuilder.addPredicateToken(theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId);
								}
							}
							break;
						case NUMBER:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateNumber(theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId);
							}
							break;
						case COMPOSITE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateComposite(theResourceName, nextParamDef, nextAnd, theRequestPartitionId);
							}
							break;
						case URI:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateUri(theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.eq, theRequestPartitionId);
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									myPredicateBuilder.addPredicateCoords(theResourceName, nextParamDef, nextAnd, theRequestPartitionId);
								}
							}
							break;
					}
				} else {
					if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
						// These are handled later
					} else if (Constants.PARAM_FILTER.equals(theParamName)) {
						// Parse the predicates enumerated in the _filter separated by AND or OR...
						if (theAndOrParams.get(0).get(0) instanceof StringParam) {
							String filterString = ((StringParam) theAndOrParams.get(0).get(0)).getValue();
							SearchFilterParser.Filter filter;
							try {
								filter = SearchFilterParser.parse(filterString);
							} catch (SearchFilterParser.FilterSyntaxException theE) {
								throw new InvalidRequestException(Msg.code(1015) + "Error parsing _filter syntax: " + theE.getMessage());
							}
							if (filter != null) {

								if (!myDaoConfig.isFilterParameterEnabled()) {
									throw new InvalidRequestException(Msg.code(1016) + Constants.PARAM_FILTER + " parameter is disabled on this server");
								}

								// TODO: we clear the predicates below because the filter builds up
								// its own collection of predicates. It'd probably be good at some
								// point to do something more fancy...
								ArrayList<Predicate> holdPredicates = new ArrayList<>(myQueryStack.getPredicates());

								Predicate filterPredicate = processFilter(filter, theResourceName, theRequest, theRequestPartitionId);
								myQueryStack.clearPredicates();
								myQueryStack.addPredicates(holdPredicates);
								myQueryStack.addPredicate(filterPredicate);

								// Because filters can have an OR at the root, we never know for sure that we haven't done an optimized
								// search that doesn't check the resource type. This could be improved in the future, but for now it's
								// safest to just clear this flag. The test "testRetrieveDifferentTypeEq" will fail if we don't clear
								// this here.
								myQueryStack.clearHasImplicitTypeSelection();
							}
						}

					} else {
						Collection<String> validNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
						String msg = myContext.getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidSearchParameter", theParamName, theResourceName, validNames);
						throw new InvalidRequestException(Msg.code(1017) + msg);
					}
				}
				break;
		}
	}

	private Predicate processFilter(SearchFilterParser.Filter theFilter, String theResourceName, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theFilter instanceof SearchFilterParser.FilterParameter) {
			return processFilterParameter((SearchFilterParser.FilterParameter) theFilter, theResourceName, theRequest, theRequestPartitionId);
		} else if (theFilter instanceof SearchFilterParser.FilterLogical) {
			// Left side
			Predicate xPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter1(), theResourceName, theRequest, theRequestPartitionId);

			// Right side
			Predicate yPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter2(), theResourceName, theRequest, theRequestPartitionId);

			if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.and) {
				return myCriteriaBuilder.and(xPredicate, yPredicate);
			} else if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.or) {
				return myCriteriaBuilder.or(xPredicate, yPredicate);
			}
		} else if (theFilter instanceof SearchFilterParser.FilterParameterGroup) {
			return processFilter(((SearchFilterParser.FilterParameterGroup) theFilter).getContained(), theResourceName, theRequest, theRequestPartitionId);
		}
		return null;
	}

	private Predicate processFilterParameter(SearchFilterParser.FilterParameter theFilter,
														  String theResourceName, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theFilter.getParamPath().getName().equals(Constants.PARAM_SOURCE)) {
			TokenParam param = new TokenParam();
			param.setValueAsQueryToken(null, null, null, theFilter.getValue());
			return addPredicateSource(Collections.singletonList(param), theFilter.getOperation(), theRequest);
		} else if (theFilter.getParamPath().getName().equals(IAnyResource.SP_RES_ID)) {
			TokenParam param = new TokenParam();
			param.setValueAsQueryToken(null,
				null,
				null,
				theFilter.getValue());
			return myPredicateBuilder.addPredicateResourceId(Collections.singletonList(Collections.singletonList(param)), myResourceName, theFilter.getOperation(), theRequestPartitionId);
		}

		RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, theFilter.getParamPath().getName());

		if (searchParam == null) {
			throw new InvalidRequestException(Msg.code(1018) + "Invalid search parameter specified, " + theFilter.getParamPath().getName() + ", for resource type " + theResourceName);
		}

		RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
		if (typeEnum == RestSearchParameterTypeEnum.URI) {
			return myPredicateBuilder.addPredicateUri(theResourceName, searchParam, Collections.singletonList(new UriParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
			return myPredicateBuilder.addPredicateString(theResourceName, searchParam, Collections.singletonList(new StringParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
			return myPredicateBuilder.addPredicateDate(theResourceName, searchParam, Collections.singletonList(new DateParam(fromOperation(theFilter.getOperation()), theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
			return myPredicateBuilder.addPredicateNumber(theResourceName, searchParam, Collections.singletonList(new NumberParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
			String paramName = theFilter.getParamPath().getName();
			SearchFilterParser.CompareOperation operation = theFilter.getOperation();
			String resourceType = null; // The value can either have (Patient/123) or not have (123) a resource type, either way it's not needed here
			String chain = (theFilter.getParamPath().getNext() != null) ? theFilter.getParamPath().getNext().toString() : null;
			String value = theFilter.getValue();
			ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
			return addPredicate(theResourceName, paramName, Collections.singletonList(referenceParam), operation, theRequest, theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
			return myPredicateBuilder.addPredicateQuantity(theResourceName, searchParam, Collections.singletonList(new QuantityParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
		} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
			throw new InvalidRequestException(Msg.code(1019) + "Composite search parameters not currently supported with _filter clauses");
		} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
			TokenParam param = new TokenParam();
			param.setValueAsQueryToken(null,
				null,
				null,
				theFilter.getValue());
			return myPredicateBuilder.addPredicateToken(theResourceName, searchParam, Collections.singletonList(param), theFilter.getOperation(), theRequestPartitionId);
		}

		return null;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam) {
		IQueryParameterType qp;
		switch (theParam.getParamType()) {
			case DATE:
				qp = new DateParam();
				break;
			case NUMBER:
				qp = new NumberParam();
				break;
			case QUANTITY:
				qp = new QuantityParam();
				break;
			case STRING:
				qp = new StringParam();
				break;
			case TOKEN:
				qp = new TokenParam();
				break;
			case COMPOSITE:
				List<RuntimeSearchParam> compositeOf = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParam);
				if (compositeOf.size() != 2) {
					throw new InternalErrorException(Msg.code(1020) + "Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
				}
				IQueryParameterType leftParam = toParameterType(compositeOf.get(0));
				IQueryParameterType rightParam = toParameterType(compositeOf.get(1));
				qp = new CompositeParam<>(leftParam, rightParam);
				break;
			case REFERENCE:
				qp = new ReferenceParam();
				break;
			case SPECIAL:
				if ("Location.position".equals(theParam.getPath())) {
					qp = new SpecialParam();
					break;
				}
				throw new InternalErrorException(Msg.code(1021) + "Don't know how to convert param type: " + theParam.getParamType());
			case URI:
			case HAS:
			default:
				throw new InternalErrorException(Msg.code(1022) + "Don't know how to convert param type: " + theParam.getParamType());
		}
		return qp;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(myContext, theParam.getName(), theQualifier, theValueAsQueryToken);
		return qp;
	}

	private void addPredicateSource(List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			addPredicateSource(nextAnd, SearchFilterParser.CompareOperation.eq, theRequest);
		}
	}

	private Predicate addPredicateSource(List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		// Required for now
		assert theOperation == SearchFilterParser.CompareOperation.eq;

		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
			String msg = myContext.getLocalizer().getMessage(LegacySearchBuilder.class, "sourceParamDisabled");
			throw new InvalidRequestException(Msg.code(1023) + msg);
		}

		From<?, ResourceHistoryProvenanceEntity> join = myQueryStack.createJoin(SearchBuilderJoinEnum.PROVENANCE, Constants.PARAM_SOURCE);

		List<Predicate> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextParameter : theList) {
			SourceParam sourceParameter = new SourceParam(nextParameter.getValueAsQueryToken(myContext));
			String sourceUri = sourceParameter.getSourceUri();
			String requestId = sourceParameter.getRequestId();
			Predicate sourceUriPredicate = myCriteriaBuilder.equal(join.get("mySourceUri"), sourceUri);
			Predicate requestIdPredicate = myCriteriaBuilder.equal(join.get("myRequestId"), requestId);
			if (isNotBlank(sourceUri) && isNotBlank(requestId)) {
				codePredicates.add(myCriteriaBuilder.and(sourceUriPredicate, requestIdPredicate));
			} else if (isNotBlank(sourceUri)) {
				codePredicates.add(sourceUriPredicate);
			} else if (isNotBlank(requestId)) {
				codePredicates.add(requestIdPredicate);
			}
		}

		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryStack.addPredicate(retVal);
		return retVal;
	}

	private void addPredicateHas(String theResourceType, List<List<IQueryParameterType>> theHasParameters, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		for (List<? extends IQueryParameterType> nextOrList : theHasParameters) {

			String targetResourceType = null;
			String paramReference = null;
			String parameterName = null;

			String paramName = null;
			List<QualifiedParamList> parameters = new ArrayList<>();
			for (IQueryParameterType nextParam : nextOrList) {
				HasParam next = (HasParam) nextParam;
				targetResourceType = next.getTargetResourceType();
				paramReference = next.getReferenceFieldName();
				parameterName = next.getParameterName();
				paramName = parameterName.replaceAll("\\..*", "");
				parameters.add(QualifiedParamList.singleton(null, next.getValueAsQueryToken(myContext)));
			}

			if (paramName == null) {
				continue;
			}

			RuntimeResourceDefinition targetResourceDefinition;
			try {
				targetResourceDefinition = myContext.getResourceDefinition(targetResourceType);
			} catch (DataFormatException e) {
				throw new InvalidRequestException(Msg.code(1024) + "Invalid resource type: " + targetResourceType);
			}

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			if (paramName.startsWith("_has:")) {

				ourLog.trace("Handing double _has query: {}", paramName);

				String qualifier = paramName.substring(4);
				paramName = Constants.PARAM_HAS;
				for (IQueryParameterType next : nextOrList) {
					HasParam nextHasParam = new HasParam();
					nextHasParam.setValueAsQueryToken(myContext, Constants.PARAM_HAS, qualifier, next.getValueAsQueryToken(myContext));
					orValues.add(nextHasParam);
				}

			} else {

				//Ensure that the name of the search param
				// (e.g. the `code` in Patient?_has:Observation:subject:code=sys|val)
				// exists on the target resource type.
				RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getActiveSearchParam(targetResourceType, paramName);
				if (owningParameterDef == null) {
					throw new InvalidRequestException(Msg.code(1025) + "Unknown parameter name: " + targetResourceType + ':' + parameterName);
				}

				//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
				//exists on the target resource.
				RuntimeSearchParam joiningParameterDef = mySearchParamRegistry.getActiveSearchParam(targetResourceType, paramReference);
				if (joiningParameterDef == null) {
					throw new InvalidRequestException(Msg.code(1026) + "Unknown parameter name: " + targetResourceType + ':' + paramReference);
				}

				IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>> parsedParam = (IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>>) JpaParamUtil.parseQueryParams(mySearchParamRegistry, myContext, owningParameterDef, paramName, parameters);

				for (IQueryParameterOr<IQueryParameterType> next : parsedParam.getValuesAsQueryTokens()) {
					orValues.addAll(next.getValuesAsQueryTokens());
				}

			}

			//Handle internal chain inside the has.
			if (parameterName.contains(".")) {
				String chainedPartOfParameter = getChainedPart(parameterName);
				orValues.stream()
					.filter(qp -> qp instanceof ReferenceParam)
					.map(qp -> (ReferenceParam) qp)
					.forEach(rp -> rp.setChain(getChainedPart(chainedPartOfParameter)));
			}

			Subquery<Long> subQ = myPredicateBuilder.createLinkSubquery(paramName, targetResourceType, orValues, theRequest, theRequestPartitionId);
			Join<?, ResourceLink> join = (Join) myQueryStack.createJoin(SearchBuilderJoinEnum.HAS, "_has");

			Predicate pathPredicate = myPredicateBuilder.createResourceLinkPathPredicate(targetResourceType, paramReference, join);
			Predicate sourceTypePredicate = myCriteriaBuilder.equal(join.get("myTargetResourceType"), theResourceType);
			Predicate sourcePidPredicate = join.get("mySourceResourcePid").in(subQ);
			Predicate andPredicate = myCriteriaBuilder.and(pathPredicate, sourcePidPredicate, sourceTypePredicate);
			myQueryStack.addPredicate(andPredicate);
		}
	}

	private String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

	private void addPredicateComposite(String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {
		// TODO: fail if missing is set for a composite query

		IQueryParameterType or = theNextAnd.get(0);
		if (!(or instanceof CompositeParam<?, ?>)) {
			throw new InvalidRequestException(Msg.code(1027) + "Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
		}
		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;

		List<RuntimeSearchParam> componentParams = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParamDef);
		RuntimeSearchParam left = componentParams.get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		myQueryStack.addPredicate(createCompositeParamPart(theResourceName, myQueryStack.getRootForComposite(), left, leftValue, theRequestPartitionId));

		RuntimeSearchParam right = componentParams.get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		myQueryStack.addPredicate(createCompositeParamPart(theResourceName, myQueryStack.getRootForComposite(), right, rightValue, theRequestPartitionId));

	}

	private Predicate createCompositeParamPart(String theResourceName, Root<?> theRoot, RuntimeSearchParam theParam, IQueryParameterType leftValue, RequestPartitionId theRequestPartitionId) {
		Predicate retVal = null;
		switch (theParam.getParamType()) {
			case STRING: {
				From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = theRoot.join("myParamsString", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateString(leftValue, theResourceName, theParam, myCriteriaBuilder, stringJoin, theRequestPartitionId);
				break;
			}
			case TOKEN: {
				From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = theRoot.join("myParamsToken", JoinType.INNER);
				List<IQueryParameterType> tokens = Collections.singletonList(leftValue);
				Collection<Predicate> tokenPredicates = myPredicateBuilder.createPredicateToken(tokens, theResourceName, theParam, myCriteriaBuilder, tokenJoin, theRequestPartitionId);
				retVal = myCriteriaBuilder.and(tokenPredicates.toArray(new Predicate[0]));
				break;
			}
			case DATE: {
				From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = theRoot.join("myParamsDate", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateDate(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin, theRequestPartitionId);
				break;
			}
			case QUANTITY: {
				From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = theRoot.join("myParamsQuantity", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateQuantity(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin, theRequestPartitionId);
				break;
			}
			case COMPOSITE:
			case HAS:
			case NUMBER:
			case REFERENCE:
			case URI:
			case SPECIAL:
				break;
		}

		if (retVal == null) {
			throw new InvalidRequestException(Msg.code(1028) + "Don't know how to handle composite parameter with type of " + theParam.getParamType());
		}

		return retVal;
	}

	@Nonnull
	private InvalidRequestException newInvalidTargetTypeForChainException(String theResourceName, String theParamName, String theTypeValue) {
		String searchParamName = theResourceName + ":" + theParamName;
		String msg = myContext.getLocalizer().getMessage(PredicateBuilderReference.class, "invalidTargetTypeForChain", theTypeValue, searchParamName);
		return new InvalidRequestException(msg);
	}

	@Nonnull
	private InvalidRequestException newInvalidResourceTypeException(String theResourceType) {
		String msg = myContext.getLocalizer().getMessageSanitized(PredicateBuilderReference.class, "invalidResourceType", theResourceType);
		throw new InvalidRequestException(Msg.code(1029) + msg);
	}

}
