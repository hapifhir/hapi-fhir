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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeChildResourceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
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
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@Component
@Scope("prototype")
class PredicateBuilderReference extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderReference.class);

	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private final PredicateBuilder myPredicateBuilder;

	PredicateBuilderReference(SearchBuilder theSearchBuilder, PredicateBuilder thePredicateBuilder) {
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
											RequestDetails theRequest) {

		//Is this just to ensure the chain has been split correctly???
		assert theParamName.contains(".") == false;

		if ((operation != null) &&
			(operation != SearchFilterParser.CompareOperation.eq) &&
			(operation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException("Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing());
			return null;
		}

		Join<ResourceTable, ResourceLink> join = createJoin(SearchBuilderJoinEnum.REFERENCE, theParamName);

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

					return addPredicateReferenceWithChain(theResourceName, theParamName, theList, join, new ArrayList<>(), ref, theRequest);

				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}

		List<Predicate> codePredicates = new ArrayList<>();

		// Resources by ID
		List<ResourcePersistentId> targetPids = myIdHelperService.resolveResourcePersistentIds(targetIds, theRequest);
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
				pidPredicate = join.get("myTargetResourcePid").in(ResourcePersistentId.toLongList(targetPids));
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
			myQueryRoot.addPredicate(predicate);
			return predicate;
		} else {
			// Add a predicate that will never match
			Predicate pidPredicate = join.get("myTargetResourcePid").in(-1L);
			myQueryRoot.clearPredicates();
			myQueryRoot.addPredicate(pidPredicate);
			return pidPredicate;
		}
	}

	/**
	 * This is for handling queries like the following: /Observation?device.identifier=urn:system|foo in which we use a chain
	 * on the device.
	 */
	private Predicate addPredicateReferenceWithChain(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, Join<ResourceTable, ResourceLink> theJoin, List<Predicate> theCodePredicates, ReferenceParam theReferenceParam, RequestDetails theRequest) {
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
				RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceName);
				RuntimeSearchParam searchParamByName = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
				if (searchParamByName == null) {
					throw new InternalErrorException("Could not find parameter " + theParamName);
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
							throw new InvalidRequestException("Unable to perform search for unqualified chain '" + theParamName + "' as this SearchParameter does not declare any target types. Add a qualifier of the form '" + theParamName + ":[ResourceType]' to perform this search.");
						}
					}
				} else {
					throw new ConfigurationException("Property " + paramPath + " of type " + myResourceName + " is not a resource: " + def.getClass());
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

		// Handle chain on _type
		if (Constants.PARAM_TYPE.equals(theReferenceParam.getChain())) {
			String typeValue = theReferenceParam.getValue();

			Class<? extends IBaseResource> wantedType;
			try {
				wantedType = myContext.getResourceDefinition(typeValue).getImplementingClass();
			} catch (DataFormatException e) {
				throw newInvalidResourceTypeException(typeValue);
			}
			if (!resourceTypes.contains(wantedType)) {
				throw newInvalidTargetTypeForChainException(theResourceName, theParamName, typeValue);
			}

			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
			Predicate sourceTypeParameter = myCriteriaBuilder.equal(theJoin.get("mySourceResourceType"), myResourceName);
			Predicate targetTypeParameter = myCriteriaBuilder.equal(theJoin.get("myTargetResourceType"), typeValue);

			Predicate composite = myCriteriaBuilder.and(pathPredicate, sourceTypeParameter, targetTypeParameter);
			myQueryRoot.addPredicate(composite);
			return composite;
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
				param = mySearchParamRegistry.getSearchParamByName(typeDef, chain);
				if (param == null) {
					ourLog.debug("Type {} doesn't have search param {}", nextType.getSimpleName(), param);
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


			Subquery<Long> subQ = createLinkSubquery(foundChainMatch, chain, subResourceName, orValues, theRequest);

			Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
			Predicate pidPredicate = theJoin.get("myTargetResourcePid").in(subQ);
			Predicate andPredicate = myCriteriaBuilder.and(pathPredicate, pidPredicate);
			theCodePredicates.add(andPredicate);
			candidateTargetTypes.add(nextType);
		}

		if (!foundChainMatch) {
			throw new InvalidRequestException(myContext.getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + theReferenceParam.getChain()));
		}

		if (candidateTargetTypes.size() > 1) {
			warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, candidateTargetTypes);
		}

		Predicate predicate = myCriteriaBuilder.or(toArray(theCodePredicates));
		myQueryRoot.addPredicate(predicate);
		return predicate;
	}

	private void warnAboutPerformanceOnUnqualifiedResources(String theParamName, RequestDetails theRequest, List<Class<? extends IBaseResource>> theCandidateTargetTypes) {
		String message = new StringBuilder()
			.append("This search uses an unqualified resource(a parameter in a chain without a resource type). ")
			.append("This is less efficient than using a qualified type. ")
			.append("[" + theParamName + "] resolves to ["+ theCandidateTargetTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(",")) +"].")
			.append("If you know what you're looking for, try qualifying it like this: ")
			.append(theCandidateTargetTypes.stream().map(cls -> "[" +cls.getSimpleName() +":"+theParamName+"]").collect(Collectors.joining(" or ")))
			.toString();
		StorageProcessingMessage msg = new StorageProcessingMessage()
			.setMessage(message);
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(StorageProcessingMessage.class, msg);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
	}

	Predicate createResourceLinkPathPredicate(String theResourceName, String theParamName, From<?, ? extends ResourceLink> from) {
		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceName);
		RuntimeSearchParam param = mySearchParamRegistry.getSearchParamByName(resourceDef, theParamName);
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

		// one value
		if (path.size() == 1) {
			return myCriteriaBuilder.equal(from.get("mySourcePath").as(String.class), path.get(0));
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

	Subquery<Long> createLinkSubquery(boolean theFoundChainMatch, String theChain, String theSubResourceName, List<IQueryParameterType> theOrValues, RequestDetails theRequest) {
		Subquery<Long> subQ = myQueryRoot.subquery(Long.class);
		/*
		 * We're doing a chain call, so push the current query root
		 * and predicate list down and put new ones at the top of the
		 * stack and run a subquery
		 */
		myQueryRoot.push(subQ);
		subQ.select(myQueryRoot.get("myId").as(Long.class));

		List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
		andOrParams.add(theOrValues);

		// Create the subquery predicates
		myQueryRoot.addPredicate(myCriteriaBuilder.equal(myQueryRoot.get("myResourceType"), theSubResourceName));
		myQueryRoot.addPredicate(myCriteriaBuilder.isNull(myQueryRoot.get("myDeleted")));

		if (theFoundChainMatch) {
			searchForIdsWithAndOr(theSubResourceName, theChain, andOrParams, theRequest);
			subQ.where(myQueryRoot.getPredicateArray());
		}

		/*
		 * Pop the old query root and predicate list back
		 */
		myQueryRoot.pop();
		return subQ;
	}

	void searchForIdsWithAndOr(String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {

		if (theAndOrParams.isEmpty()) {
			return;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				myPredicateBuilder.addPredicateResourceId(theAndOrParams, theResourceName, theRequest);
				break;

			case IAnyResource.SP_RES_LANGUAGE:
				addPredicateLanguage(theAndOrParams,
					null);
				break;

			case Constants.PARAM_HAS:
				addPredicateHas(theResourceName, theAndOrParams, theRequest);
				break;

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				myPredicateBuilder.addPredicateTag(theAndOrParams, theParamName);
				break;

			case Constants.PARAM_SOURCE:
				addPredicateSource(theAndOrParams, theRequest);
				break;

			default:

				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
				if (nextParamDef != null) {
					switch (nextParamDef.getParamType()) {
						case DATE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateDate(theResourceName, theParamName, nextAnd, null);
							}
							break;
						case QUANTITY:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateQuantity(theResourceName, theParamName, nextAnd, null);
							}
							break;
						case REFERENCE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicate(theResourceName, theParamName, nextAnd, null, theRequest);
							}
							break;
						case STRING:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateString(theResourceName, theParamName, nextAnd, SearchFilterParser.CompareOperation.sw);
							}
							break;
						case TOKEN:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									myPredicateBuilder.addPredicateCoords(theResourceName, theParamName, nextAnd);
								} else {
									myPredicateBuilder.addPredicateToken(theResourceName, theParamName, nextAnd, null);
								}
							}
							break;
						case NUMBER:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateNumber(theResourceName, theParamName, nextAnd, null);
							}
							break;
						case COMPOSITE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateComposite(theResourceName, nextParamDef, nextAnd);
							}
							break;
						case URI:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								myPredicateBuilder.addPredicateUri(theResourceName, theParamName, nextAnd, SearchFilterParser.CompareOperation.eq);
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									myPredicateBuilder.addPredicateCoords(theResourceName, theParamName, nextAnd);
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
								throw new InvalidRequestException("Error parsing _filter syntax: " + theE.getMessage());
							}
							if (filter != null) {

								if (!myDaoConfig.isFilterParameterEnabled()) {
									throw new InvalidRequestException(Constants.PARAM_FILTER + " parameter is disabled on this server");
								}

								// TODO: we clear the predicates below because the filter builds up
								// its own collection of predicates. It'd probably be good at some
								// point to do something more fancy...
								ArrayList<Predicate> holdPredicates = new ArrayList<>(myQueryRoot.getPredicates());

								Predicate filterPredicate = processFilter(filter, theResourceName, theRequest);
								myQueryRoot.clearPredicates();
								myQueryRoot.addPredicates(holdPredicates);
								myQueryRoot.addPredicate(filterPredicate);
							}
						}


					} else {
						throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + theResourceName);
					}
				}
				break;
		}
	}

	private Predicate processFilter(SearchFilterParser.Filter theFilter,
											  String theResourceName, RequestDetails theRequest) {

		if (theFilter instanceof SearchFilterParser.FilterParameter) {
			return processFilterParameter((SearchFilterParser.FilterParameter) theFilter,
				theResourceName, theRequest);
		} else if (theFilter instanceof SearchFilterParser.FilterLogical) {
			// Left side
			Predicate xPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter1(),
				theResourceName, theRequest);

			// Right side
			Predicate yPredicate = processFilter(((SearchFilterParser.FilterLogical) theFilter).getFilter2(),
				theResourceName, theRequest);

			if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.and) {
				return myCriteriaBuilder.and(xPredicate, yPredicate);
			} else if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.or) {
				return myCriteriaBuilder.or(xPredicate, yPredicate);
			}
		} else if (theFilter instanceof SearchFilterParser.FilterParameterGroup) {
			return processFilter(((SearchFilterParser.FilterParameterGroup) theFilter).getContained(),
				theResourceName, theRequest);
		}
		return null;
	}

	private Predicate processFilterParameter(SearchFilterParser.FilterParameter theFilter,
														  String theResourceName, RequestDetails theRequest) {

		RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, theFilter.getParamPath().getName());

		if (searchParam == null) {
			throw new InvalidRequestException("Invalid search parameter specified, " + theFilter.getParamPath().getName() + ", for resource type " + theResourceName);
		} else if (searchParam.getName().equals(IAnyResource.SP_RES_ID)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null,
					null,
					null,
					theFilter.getValue());
				return myPredicateBuilder.addPredicateResourceId(Collections.singletonList(Collections.singletonList(param)), myResourceName, theFilter.getOperation(), theRequest);
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected token type for _id search");
			}
		} else if (searchParam.getName().equals(IAnyResource.SP_RES_LANGUAGE)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.STRING) {
				return addPredicateLanguage(Collections.singletonList(Collections.singletonList(new StringParam(theFilter.getValue()))),
					theFilter.getOperation());
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected string type for language search");
			}
		} else if (searchParam.getName().equals(Constants.PARAM_SOURCE)) {
			if (searchParam.getParamType() == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return addPredicateSource(Collections.singletonList(param), theFilter.getOperation(), theRequest);
			} else {
				throw new InvalidRequestException("Unexpected search parameter type encountered, expected token type for _id search");
			}
		} else {
			RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
			if (typeEnum == RestSearchParameterTypeEnum.URI) {
				return myPredicateBuilder.addPredicateUri(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(new UriParam(theFilter.getValue())), theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
				return myPredicateBuilder.addPredicateString(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(new StringParam(theFilter.getValue())), theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
				return myPredicateBuilder.addPredicateDate(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(new DateParam(theFilter.getValue())), theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
				return myPredicateBuilder.addPredicateNumber(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(new NumberParam(theFilter.getValue())), theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
				String paramName = theFilter.getParamPath().getName();
				SearchFilterParser.CompareOperation operation = theFilter.getOperation();
				String resourceType = null; // The value can either have (Patient/123) or not have (123) a resource type, either way it's not needed here
				String chain = (theFilter.getParamPath().getNext() != null) ? theFilter.getParamPath().getNext().toString() : null;
				String value = theFilter.getValue();
				ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
				return addPredicate(theResourceName, paramName, Collections.singletonList(referenceParam), operation, theRequest);
			} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
				return myPredicateBuilder.addPredicateQuantity(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(new QuantityParam(theFilter.getValue())), theFilter.getOperation());
			} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
				throw new InvalidRequestException("Composite search parameters not currently supported with _filter clauses");
			} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null,
					null,
					null,
					theFilter.getValue());
				return myPredicateBuilder.addPredicateToken(theResourceName, theFilter.getParamPath().getName(), Collections.singletonList(param), theFilter.getOperation());
			}
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
				List<RuntimeSearchParam> compositeOf = theParam.getCompositeOf();
				if (compositeOf.size() != 2) {
					throw new InternalErrorException("Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
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
			case URI:
			case HAS:
			default:
				throw new InternalErrorException("Don't know how to convert param type: " + theParam.getParamType());
		}
		return qp;
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(myContext, theParam.getName(), theQualifier, theValueAsQueryToken);
		return qp;
	}

	private Predicate addPredicateLanguage(List<List<IQueryParameterType>> theList,
														SearchFilterParser.CompareOperation operation) {
		for (List<? extends IQueryParameterType> nextList : theList) {

			Set<String> values = new HashSet<>();
			for (IQueryParameterType next : nextList) {
				if (next instanceof StringParam) {
					String nextValue = ((StringParam) next).getValue();
					if (isBlank(nextValue)) {
						continue;
					}
					values.add(nextValue);
				} else {
					throw new InternalErrorException("Language parameter must be of type " + StringParam.class.getCanonicalName() + " - Got " + next.getClass().getCanonicalName());
				}
			}

			if (values.isEmpty()) {
				continue;
			}

			Predicate predicate = null;
			if ((operation == null) ||
				(operation == SearchFilterParser.CompareOperation.eq)) {
				predicate = myQueryRoot.get("myLanguage").as(String.class).in(values);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				predicate = myQueryRoot.get("myLanguage").as(String.class).in(values).not();
			} else {
				throw new InvalidRequestException("Unsupported operator specified in language query, only \"eq\" and \"ne\" are supported");
			}
			myQueryRoot.addPredicate(predicate);
			if (operation != null) {
				return predicate;
			}
		}

		return null;
	}

	private void addPredicateSource(List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			addPredicateSource(nextAnd, SearchFilterParser.CompareOperation.eq, theRequest);
		}
	}

	private Predicate addPredicateSource(List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, "sourceParamDisabled");
			throw new InvalidRequestException(msg);
		}

		Join<ResourceTable, ResourceHistoryProvenanceEntity> join = myQueryRoot.join("myProvenance", JoinType.LEFT);

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
		myQueryRoot.addPredicate(retVal);
		return retVal;
	}

	private void addPredicateHas(String theResourceType, List<List<IQueryParameterType>> theHasParameters, RequestDetails theRequest) {

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
				throw new InvalidRequestException("Invalid resource type: " + targetResourceType);
			}

			assert parameterName != null;

			//Ensure that the name of the search param
			// (e.g. the `code` in Patient?_has:Observation:subject:code=sys|val)
			// exists on the target resource type.
			RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
			}

			//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
			//exists on the target resource.
			owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramReference);
			if (owningParameterDef == null) {
				throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + paramReference);
			}

			RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);

			IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>> parsedParam = (IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>>) ParameterUtil.parseQueryParams(myContext, paramDef, paramName, parameters);

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			for (IQueryParameterOr<IQueryParameterType> next : parsedParam.getValuesAsQueryTokens()) {
				orValues.addAll(next.getValuesAsQueryTokens());
			}
			//Handle internal chain inside the has.
			if (parameterName.contains(".")) {
				String chainedPartOfParameter = getChainedPart(parameterName);
				orValues.stream()
					.filter(qp -> qp instanceof ReferenceParam)
					.map(qp -> (ReferenceParam)qp)
					.forEach(rp -> rp.setChain(getChainedPart(chainedPartOfParameter)));
			}

			Subquery<Long> subQ = myPredicateBuilder.createLinkSubquery(paramName, targetResourceType, orValues, theRequest);
			Join<ResourceTable, ResourceLink> join = myQueryRoot.join("myResourceLinksAsTarget", JoinType.LEFT);

			Predicate pathPredicate = myPredicateBuilder.createResourceLinkPathPredicate(targetResourceType, paramReference, join);
			Predicate sourceTypePredicate = myCriteriaBuilder.equal(join.get("myTargetResourceType"), theResourceType);
			Predicate sourcePidPredicate = join.get("mySourceResourcePid").in(subQ);
			Predicate andPredicate = myCriteriaBuilder.and(pathPredicate, sourcePidPredicate, sourceTypePredicate);
			myQueryRoot.addPredicate(andPredicate);
		}
	}

	private String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

	private void addPredicateComposite(String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd) {
		// TODO: fail if missing is set for a composite query

		IQueryParameterType or = theNextAnd.get(0);
		if (!(or instanceof CompositeParam<?, ?>)) {
			throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
		}
		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;

		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		myQueryRoot.addPredicate(createCompositeParamPart(theResourceName, myQueryRoot.getRoot(), left, leftValue));

		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		myQueryRoot.addPredicate(createCompositeParamPart(theResourceName, myQueryRoot.getRoot(), right, rightValue));

	}

	private Predicate createCompositeParamPart(String theResourceName, Root<ResourceTable> theRoot, RuntimeSearchParam theParam, IQueryParameterType leftValue) {
		Predicate retVal = null;
		switch (theParam.getParamType()) {
			case STRING: {
				From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = theRoot.join("myParamsString", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateString(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, stringJoin);
				break;
			}
			case TOKEN: {
				From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = theRoot.join("myParamsToken", JoinType.INNER);
				List<IQueryParameterType> tokens = Collections.singletonList(leftValue);
				Collection<Predicate> tokenPredicates = myPredicateBuilder.createPredicateToken(tokens, theResourceName, theParam.getName(), myCriteriaBuilder, tokenJoin);
				retVal = myCriteriaBuilder.and(tokenPredicates.toArray(new Predicate[0]));
				break;
			}
			case DATE: {
				From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = theRoot.join("myParamsDate", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateDate(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin);
				break;
			}
			case QUANTITY: {
				From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = theRoot.join("myParamsQuantity", JoinType.INNER);
				retVal = myPredicateBuilder.createPredicateQuantity(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin);
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
			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
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
		throw new InvalidRequestException(msg);
	}

}
