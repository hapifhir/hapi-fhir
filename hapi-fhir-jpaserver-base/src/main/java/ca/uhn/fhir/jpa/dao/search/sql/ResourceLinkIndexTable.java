package ca.uhn.fhir.jpa.dao.search.sql;

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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderReference;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceMetaParams;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.collect.Lists;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ResourceLinkIndexTable extends BaseIndexTable {

	private final DbColumn myColumnSrcType;
	private final DbColumn myColumnSrcPath;
	private final DbColumn myColumnTargetResourceId;
	private final DbColumn myColumnTargetResourceUrl;
	private final DbColumn myColumnSrcResourceId;
	private final DbColumn myColumnTargetResourceType;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private MatchUrlService myMatchUrlService;

	/**
	 * Constructor
	 */
	public ResourceLinkIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_LINK"));
		myColumnSrcResourceId = getTable().addColumn("SRC_RESOURCE_ID");
		myColumnSrcType = getTable().addColumn("SOURCE_RESOURCE_TYPE");
		myColumnSrcPath = getTable().addColumn("SRC_PATH");
		myColumnTargetResourceId = getTable().addColumn("TARGET_RESOURCE_ID");
		myColumnTargetResourceUrl = getTable().addColumn("TARGET_RESOURCE_URL");
		myColumnTargetResourceType = getTable().addColumn("TARGET_RESOURCE_TYPE");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnSrcResourceId;
	}

	public Condition addPredicate(RequestDetails theRequest, String theParamName, List<? extends IQueryParameterType> theReferenceOrParamList, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

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

					return addPredicateReferenceWithChain(getResourceType(), theParamName, theReferenceOrParamList, ref, theRequest);

				}

			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}

		}


		if (getRequestPartitionId() != null) {
			addPartitionIdPredicate(getRequestPartitionId().getPartitionId());
		}

		for (IIdType next : targetIds) {
			if (!next.hasResourceType()) {
				warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, null);
			}
		}

		List<String> pathsToMatch = createResourceLinkPathPredicate(getResourceType(), theParamName);
		boolean inverse;
		if ((theOperation == null) || (theOperation == SearchFilterParser.CompareOperation.eq)) {
			inverse = false;
		} else {
			inverse = true;
		}

		List<ResourcePersistentId> targetPids = myIdHelperService.resolveResourcePersistentIdsWithCache(theRequestPartitionId, targetIds);
		List<Long> targetPidList = ResourcePersistentId.toLongList(targetPids);

		if (targetPidList.isEmpty() && targetQualifiedUrls.isEmpty()) {
			setMatchNothing();
			return null;
		} else {
			return addPredicateReference(inverse, pathsToMatch, targetPidList, targetQualifiedUrls);
		}

	}


	private Condition addPredicateReference(boolean theInverse, List<String> thePathsToMatch, List<Long> theTargetPidList, List<String> theTargetQualifiedUrls) {

		Condition targetPidCondition = null;
		if (!theTargetPidList.isEmpty()) {
			targetPidCondition = new InCondition(myColumnTargetResourceId, generatePlaceholders(theTargetPidList));
		}

		Condition targetUrlsCondition = null;
		if (!theTargetQualifiedUrls.isEmpty()) {
			targetUrlsCondition = new InCondition(myColumnTargetResourceUrl, generatePlaceholders(theTargetQualifiedUrls));
		}

		Condition joinedCondition;
		if (targetPidCondition != null && targetUrlsCondition != null) {
			joinedCondition = ComboCondition.or(targetPidCondition, targetUrlsCondition);
		} else if (targetPidCondition != null) {
			joinedCondition = targetPidCondition;
		} else {
			joinedCondition = targetUrlsCondition;
		}

		InCondition pathPredicate = new InCondition(myColumnSrcPath, generatePlaceholders(thePathsToMatch));
		joinedCondition = ComboCondition.and(pathPredicate, joinedCondition);

		Condition condition;
		if (theInverse) {
			condition = new NotCondition(joinedCondition);
		} else {
			condition = joinedCondition;
		}

		addCondition(condition);
		return condition;
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
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
	}


	/**
	 * This is for handling queries like the following: /Observation?device.identifier=urn:system|foo in which we use a chain
	 * on the device.
	 */
	private Condition addPredicateReferenceWithChain(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, ReferenceParam theReferenceParam, RequestDetails theRequest) {

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
			InCondition condition = new InCondition(myColumnTargetResourceType, generatePlaceholders(resourceTypes));
			addCondition(condition);
			return condition;
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

			RuntimeResourceDefinition typeDef = getFhirContext().getResourceDefinition(nextType);
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
				String nextValue = next.getValueAsQueryToken(getFhirContext());
				IQueryParameterType chainValue = mapReferenceChainToRawParamType(remainingChain, param, theParamName, qualifier, nextType, chain, isMeta, nextValue);
				if (chainValue == null) {
					continue;
				}
				foundChainMatch = true;
				orValues.add(chainValue);
			}

			if (!foundChainMatch) {
				throw new InvalidRequestException(getFhirContext().getLocalizer().getMessage(BaseHapiFhirResourceDao.class, "invalidParameterChain", theParamName + '.' + theReferenceParam.getChain()));
			}

			if (candidateTargetTypes.size() > 1) {
				warnAboutPerformanceOnUnqualifiedResources(theParamName, theRequest, candidateTargetTypes);
			}

			RuntimeSearchParam chainParamDef = mySearchParamRegistry.getActiveSearchParam(subResourceName, chain);



			throw new UnsupportedOperationException();

			// If this is false, we throw an exception below so no sense doing any further processing
//			if (foundChainMatch) {
				// FIXME KHS this is the part we need to change
//				RuntimeSearchParam chainParamDef = mySearchParamRegistry.getActiveSearchParam(subResourceName, chain);



				// FIXME KHS rather than all this exclusionary logic, refactor the predicate stuff out of the join code so we can reuse it in createPredicate() below
//				if (canOptimizeToCrossJoin(resourceTypes, orValues, chainParamDef)) {
					// FIXME hardcode token for now
//					Join<ResourceLink, ResourceTable> linkTargetJoin = theLinkJoin.join("myParamsToken", JoinType.LEFT);

					// JA RESTORE
//					RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(subResourceName, chain);
//					Predicate valuesPredicate = myPredicateBuilder.addLinkPredicate(theResourceName, paramDef, orValues, null, theLinkJoin, theRequestPartitionId);
//					Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theLinkJoin);
//					theCodePredicates.add(pathPredicate);
//					candidateTargetTypes.add(nextType);
//					andPredicate = myCriteriaBuilder.and(pathPredicate, valuesPredicate);
//


//					From<?, ?> from = myQueryStack.addFromOrReturnNull(chainParamDef);
//					if (from != null) {
//						// Optimize search with a cross join
//						Predicate valuePredicate = myPredicateBuilder.createPredicate(orValues, subResourceName, chainParamDef, myCriteriaBuilder, from, theRequestPartitionId);
//						Predicate pidPredicate = myCriteriaBuilder.equal(theLinkJoin.get("myTargetResourcePid"), from.get("myResourcePid"));
//						Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theLinkJoin);
//						andPredicate = myCriteriaBuilder.and(pidPredicate, pathPredicate, valuePredicate);
//					}
//				}

//				if (andPredicate == null) {
					// JA RESTORE
//					Subquery<Long> subQ = createLinkSubquery(chain, subResourceName, orValues, theRequest, theRequestPartitionId);
//
//					Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theLinkJoin);
//					Predicate pidPredicate = theLinkJoin.get("myTargetResourcePid").in(subQ);
//					andPredicate = myCriteriaBuilder.and(pathPredicate, pidPredicate);
//					theCodePredicates.add(andPredicate);
//					candidateTargetTypes.add(nextType);
//				}
//			}
		}



		return null;
//		Predicate predicate = myCriteriaBuilder.or(toArray(theCodePredicates));
//		myQueryStack.addPredicateWithImplicitTypeSelection(predicate);
//		return predicate;
	}


//
//	private boolean canOptimizeToCrossJoin(List<Class<? extends IBaseResource>> theResourceTypes, ArrayList<IQueryParameterType> theOrValues, RuntimeSearchParam theChainParamDef) {
//		if (theChainParamDef == null) {
//			return false;
//		}
//		if (theResourceTypes.size() > 1) {
//			return false;
//		}
//		if (theOrValues.size() != 1) {
//			return false;
//		}
//		IQueryParameterType queryParam = theOrValues.get(0);
//		if (queryParam instanceof TokenParam) {
//			TokenParam tokenParam = (TokenParam) queryParam;
//			if (tokenParam.isText()) {
//				return false;
//			}
//		}
//		if (queryParam.getMissing() != null) {
//			return false;
//		}
//		return true;
//	}
//
//	private Predicate createChainPredicateOnType(String theResourceName, String theParamName, From<?, ResourceLink> theJoin, ReferenceParam theReferenceParam, List<Class<? extends IBaseResource>> theResourceTypes) {
//		String typeValue = theReferenceParam.getValue();
//
//		Class<? extends IBaseResource> wantedType;
//		try {
//			wantedType = myContext.getResourceDefinition(typeValue).getImplementingClass();
//		} catch (DataFormatException e) {
//			throw newInvalidResourceTypeException(typeValue);
//		}
//		if (!theResourceTypes.contains(wantedType)) {
//			throw newInvalidTargetTypeForChainException(theResourceName, theParamName, typeValue);
//		}
//
//		// JA RESTORE
////		Predicate pathPredicate = createResourceLinkPathPredicate(theResourceName, theParamName, theJoin);
////		Predicate sourceTypeParameter = myCriteriaBuilder.equal(theJoin.get("mySourceResourceType"), myResourceName);
////		Predicate targetTypeParameter = myCriteriaBuilder.equal(theJoin.get("myTargetResourceType"), typeValue);
////
////		Predicate composite = myCriteriaBuilder.and(pathPredicate, sourceTypeParameter, targetTypeParameter);
////		myQueryStack.addPredicate(composite);
//		return null;
//	}
//


	@Nonnull
	private List<Class<? extends IBaseResource>> determineCandidateResourceTypesForChain(String theResourceName, String theParamName, ReferenceParam theReferenceParam) {
		final List<Class<? extends IBaseResource>> resourceTypes;
		if (!theReferenceParam.hasResourceType()) {

			RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
			resourceTypes = new ArrayList<>();

			if (param.hasTargets()) {
				Set<String> targetTypes = param.getTargets();
				for (String next : targetTypes) {
					resourceTypes.add(getFhirContext().getResourceDefinition(next).getImplementingClass());
				}
			}

			if (resourceTypes.isEmpty()) {
				RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(theResourceName);
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

				Class<? extends IBaseResource> resourceType = getFhirContext().getResourceDefinition(getResourceType()).getImplementingClass();
				BaseRuntimeChildDefinition def = getFhirContext().newTerser().getDefinition(resourceType, paramPath);
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
					throw new ConfigurationException("Property " + paramPath + " of type " + getResourceType() + " is not a resource: " + def.getClass());
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
				RuntimeResourceDefinition resDef = getFhirContext().getResourceDefinition(theReferenceParam.getResourceType());
				resourceTypes = new ArrayList<>(1);
				resourceTypes.add(resDef.getImplementingClass());
			} catch (DataFormatException e) {
				throw newInvalidResourceTypeException(theReferenceParam.getResourceType());
			}

		}
		return resourceTypes;
	}


	private List<String> createResourceLinkPathPredicate(String theResourceName, String theParamName) {
		RuntimeResourceDefinition resourceDef = getFhirContext().getResourceDefinition(theResourceName);
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

		return path;
	}



	private IQueryParameterType mapReferenceChainToRawParamType(String remainingChain, RuntimeSearchParam param, String theParamName, String qualifier, Class<? extends IBaseResource> nextType, String chain, boolean isMeta, String resourceId) {
		IQueryParameterType chainValue;
		if (remainingChain != null) {
			if (param == null || param.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
				ourLog.debug("Type {} parameter {} is not a reference, can not chain {}", nextType.getSimpleName(), chain, remainingChain);
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
			chainValue = toParameterType(param, qualifier, resourceId);
		}

		return chainValue;
	}


	//	Subquery<Long> createLinkSubquery(String theChain, String theSubResourceName, List<IQueryParameterType> theOrValues, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
//
//		/*
//		 * We're doing a chain call, so push the current query root
//		 * and predicate list down and put new ones at the top of the
//		 * stack and run a subquery
//		 */
//		RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theSubResourceName, theChain);
//		if (nextParamDef != null && !theChain.startsWith("_")) {
//			myQueryStack.pushIndexTableSubQuery();
//		} else {
//			myQueryStack.pushResourceTableSubQuery(theSubResourceName);
//		}
//
//		List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
//		andOrParams.add(theOrValues);
//
//		searchForIdsWithAndOr(theSubResourceName, theChain, andOrParams, theRequest, theRequestPartitionId);
//
//		/*
//		 * Pop the old query root and predicate list back
//		 */
//		return (Subquery<Long>) myQueryStack.pop();
//
//	}
//
//


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
				throw new InternalErrorException("Don't know how to convert param type: " + theParam.getParamType());
			case URI:
			case HAS:
			default:
				throw new InternalErrorException("Don't know how to convert param type: " + theParam.getParamType());
		}
		return qp;
	}


	private IQueryParameterType toParameterType(RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = toParameterType(theParam);

		qp.setValueAsQueryToken(getFhirContext(), theParam.getName(), theQualifier, theValueAsQueryToken);
		return qp;
	}

//	private Predicate addPredicateLanguage(List<List<IQueryParameterType>> theList,
//														SearchFilterParser.CompareOperation operation) {
//		for (List<? extends IQueryParameterType> nextList : theList) {
//
//			Set<String> values = new HashSet<>();
//			for (IQueryParameterType next : nextList) {
//				if (next instanceof StringParam) {
//					String nextValue = ((StringParam) next).getValue();
//					if (isBlank(nextValue)) {
//						continue;
//					}
//					values.add(nextValue);
//				} else {
//					throw new InternalErrorException("Language parameter must be of type " + StringParam.class.getCanonicalName() + " - Got " + next.getClass().getCanonicalName());
//				}
//			}
//
//			if (values.isEmpty()) {
//				continue;
//			}
//
//			Predicate predicate;
//			if ((operation == null) ||
//				(operation == SearchFilterParser.CompareOperation.eq)) {
//				predicate = myQueryStack.get("myLanguage").as(String.class).in(values);
//			} else if (operation == SearchFilterParser.CompareOperation.ne) {
//				predicate = myQueryStack.get("myLanguage").as(String.class).in(values).not();
//			} else {
//				throw new InvalidRequestException("Unsupported operator specified in language query, only \"eq\" and \"ne\" are supported");
//			}
//			myQueryStack.addPredicate(predicate);
//			if (operation != null) {
//				return predicate;
//			}
//		}
//
//		return null;
//	}
//
//	private void addPredicateSource(List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
//		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
//			addPredicateSource(nextAnd, SearchFilterParser.CompareOperation.eq, theRequest);
//		}
//	}
//
//	private Predicate addPredicateSource(List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
//		// Required for now
//		assert theOperation == SearchFilterParser.CompareOperation.eq;
//
//		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
//			String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, "sourceParamDisabled");
//			throw new InvalidRequestException(msg);
//		}
//
//		From<?, ResourceHistoryProvenanceEntity> join = myQueryStack.createJoin(SearchBuilderJoinEnum.PROVENANCE, Constants.PARAM_SOURCE);
//
//		List<Predicate> codePredicates = new ArrayList<>();
//
//		for (IQueryParameterType nextParameter : theList) {
//			SourceParam sourceParameter = new SourceParam(nextParameter.getValueAsQueryToken(myContext));
//			String sourceUri = sourceParameter.getSourceUri();
//			String requestId = sourceParameter.getRequestId();
//			Predicate sourceUriPredicate = myCriteriaBuilder.equal(join.get("mySourceUri"), sourceUri);
//			Predicate requestIdPredicate = myCriteriaBuilder.equal(join.get("myRequestId"), requestId);
//			if (isNotBlank(sourceUri) && isNotBlank(requestId)) {
//				codePredicates.add(myCriteriaBuilder.and(sourceUriPredicate, requestIdPredicate));
//			} else if (isNotBlank(sourceUri)) {
//				codePredicates.add(sourceUriPredicate);
//			} else if (isNotBlank(requestId)) {
//				codePredicates.add(requestIdPredicate);
//			}
//		}
//
//		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));
//		myQueryStack.addPredicate(retVal);
//		return retVal;
//	}
//
//	private void addPredicateHas(String theResourceType, List<List<IQueryParameterType>> theHasParameters, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
//
//		for (List<? extends IQueryParameterType> nextOrList : theHasParameters) {
//
//			String targetResourceType = null;
//			String paramReference = null;
//			String parameterName = null;
//
//			String paramName = null;
//			List<QualifiedParamList> parameters = new ArrayList<>();
//			for (IQueryParameterType nextParam : nextOrList) {
//				HasParam next = (HasParam) nextParam;
//				targetResourceType = next.getTargetResourceType();
//				paramReference = next.getReferenceFieldName();
//				parameterName = next.getParameterName();
//				paramName = parameterName.replaceAll("\\..*", "");
//				parameters.add(QualifiedParamList.singleton(null, next.getValueAsQueryToken(myContext)));
//			}
//
//			if (paramName == null) {
//				continue;
//			}
//
//			RuntimeResourceDefinition targetResourceDefinition;
//			try {
//				targetResourceDefinition = myContext.getResourceDefinition(targetResourceType);
//			} catch (DataFormatException e) {
//				throw new InvalidRequestException("Invalid resource type: " + targetResourceType);
//			}
//
//			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();
//
//			if (paramName.startsWith("_has:")) {
//
//				ourLog.trace("Handing double _has query: {}", paramName);
//
//				String qualifier = paramName.substring(4);
//				paramName = Constants.PARAM_HAS;
//				for (IQueryParameterType next : nextOrList) {
//					HasParam nextHasParam = new HasParam();
//					nextHasParam.setValueAsQueryToken(myContext, Constants.PARAM_HAS, qualifier, next.getValueAsQueryToken(myContext));
//					orValues.add(nextHasParam);
//				}
//
//			} else {
//
//				//Ensure that the name of the search param
//				// (e.g. the `code` in Patient?_has:Observation:subject:code=sys|val)
//				// exists on the target resource type.
//				RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
//				if (owningParameterDef == null) {
//					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
//				}
//
//				//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
//				//exists on the target resource.
//				owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramReference);
//				if (owningParameterDef == null) {
//					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + paramReference);
//				}
//
//				RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
//				IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>> parsedParam = (IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>>) ParameterUtil.parseQueryParams(myContext, paramDef, paramName, parameters);
//
//				for (IQueryParameterOr<IQueryParameterType> next : parsedParam.getValuesAsQueryTokens()) {
//					orValues.addAll(next.getValuesAsQueryTokens());
//				}
//
//			}
//
//			//Handle internal chain inside the has.
//			if (parameterName.contains(".")) {
//				String chainedPartOfParameter = getChainedPart(parameterName);
//				orValues.stream()
//					.filter(qp -> qp instanceof ReferenceParam)
//					.map(qp -> (ReferenceParam) qp)
//					.forEach(rp -> rp.setChain(getChainedPart(chainedPartOfParameter)));
//			}
//
//			Subquery<Long> subQ = myPredicateBuilder.createLinkSubquery(paramName, targetResourceType, orValues, theRequest, theRequestPartitionId);
//			Join<?, ResourceLink> join = (Join) myQueryStack.createJoin(SearchBuilderJoinEnum.HAS, "_has");
//
//			Predicate pathPredicate = myPredicateBuilder.createResourceLinkPathPredicate(targetResourceType, paramReference, join);
//			Predicate sourceTypePredicate = myCriteriaBuilder.equal(join.get("myTargetResourceType"), theResourceType);
//			Predicate sourcePidPredicate = join.get("mySourceResourcePid").in(subQ);
//			Predicate andPredicate = myCriteriaBuilder.and(pathPredicate, sourcePidPredicate, sourceTypePredicate);
//			myQueryStack.addPredicate(andPredicate);
//		}
//	}
//
//	private String getChainedPart(String parameter) {
//		return parameter.substring(parameter.indexOf(".") + 1);
//	}
//
//	private void addPredicateComposite(String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {
//		// TODO: fail if missing is set for a composite query
//
//		IQueryParameterType or = theNextAnd.get(0);
//		if (!(or instanceof CompositeParam<?, ?>)) {
//			throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
//		}
//		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;
//
//		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
//		IQueryParameterType leftValue = cp.getLeftValue();
//		myQueryStack.addPredicate(createCompositeParamPart(theResourceName, myQueryStack.getRootForComposite(), left, leftValue, theRequestPartitionId));
//
//		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
//		IQueryParameterType rightValue = cp.getRightValue();
//		myQueryStack.addPredicate(createCompositeParamPart(theResourceName, myQueryStack.getRootForComposite(), right, rightValue, theRequestPartitionId));
//
//	}
//
//	private Predicate createCompositeParamPart(String theResourceName, Root<?> theRoot, RuntimeSearchParam theParam, IQueryParameterType leftValue, RequestPartitionId theRequestPartitionId) {
//		Predicate retVal = null;
//
//		// FIXME: implement composite
////		switch (theParam.getParamType()) {
////			case STRING: {
////				From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> stringJoin = theRoot.join("myParamsString", JoinType.INNER);
////				retVal = myPredicateBuilder.createPredicateString(leftValue, theResourceName, theParam, myCriteriaBuilder, stringJoin, theRequestPartitionId);
////				break;
////			}
////			case TOKEN: {
////				From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> tokenJoin = theRoot.join("myParamsToken", JoinType.INNER);
////				List<IQueryParameterType> tokens = Collections.singletonList(leftValue);
////				Collection<Predicate> tokenPredicates = myPredicateBuilder.createPredicateToken(tokens, theResourceName, theParam, myCriteriaBuilder, tokenJoin, theRequestPartitionId);
////				retVal = myCriteriaBuilder.and(tokenPredicates.toArray(new Predicate[0]));
////				break;
////			}
////			case DATE: {
////				From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> dateJoin = theRoot.join("myParamsDate", JoinType.INNER);
////				retVal = myPredicateBuilder.createPredicateDate(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin, theRequestPartitionId);
////				break;
////			}
////			case QUANTITY: {
////				From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> dateJoin = theRoot.join("myParamsQuantity", JoinType.INNER);
////				retVal = myPredicateBuilder.createPredicateQuantity(leftValue, theResourceName, theParam.getName(), myCriteriaBuilder, dateJoin, theRequestPartitionId);
////				break;
////			}
////			case COMPOSITE:
////			case HAS:
////			case NUMBER:
////			case REFERENCE:
////			case URI:
////			case SPECIAL:
////				break;
////		}
//
//		if (retVal == null) {
//			throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
//		}
//
//		return retVal;
//	}
//
//	@Nonnull
//	private InvalidRequestException newInvalidTargetTypeForChainException(String theResourceName, String theParamName, String theTypeValue) {
//		String searchParamName = theResourceName + ":" + theParamName;
//		String msg = myContext.getLocalizer().getMessage(PredicateBuilderReference2.class, "invalidTargetTypeForChain", theTypeValue, searchParamName);
//		return new InvalidRequestException(msg);
//	}


	@Nonnull
	private InvalidRequestException newInvalidResourceTypeException(String theResourceType) {
		String msg = getFhirContext().getLocalizer().getMessageSanitized(PredicateBuilderReference.class, "invalidResourceType", theResourceType);
		throw new InvalidRequestException(msg);
	}

private static final Logger ourLog = LoggerFactory.getLogger(ResourceLinkIndexTable.class);
}
