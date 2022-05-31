package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_ID;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
class RuleImplOp extends BaseRule /* implements IAuthRule */ {

	private AppliesTypeEnum myAppliesTo;
	private Set<String> myAppliesToTypes;
	private String myClassifierCompartmentName;
	private Collection<? extends IIdType> myClassifierCompartmentOwners;
	private ClassifierTypeEnum myClassifierType;
	private RuleOpEnum myOp;
	private TransactionAppliesToEnum myTransactionAppliesToOp;
	private Collection<IIdType> myAppliesToInstances;
	private boolean myAppliesToDeleteCascade;
	private boolean myAppliesToDeleteExpunge;
	private AdditionalCompartmentSearchParameters myAdditionalCompartmentSearchParamMap;

	/**
	 * Constructor
	 */
	RuleImplOp(String theRuleName) {
		super(theRuleName);
	}

	@VisibleForTesting
	Collection<IIdType> getAppliesToInstances() {
		return myAppliesToInstances;
	}

	void setAppliesToInstances(Collection<IIdType> theAppliesToInstances) {
		myAppliesToInstances = theAppliesToInstances;
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource,
									 IRuleApplier theRuleApplier, Set<AuthorizationFlagsEnum> theFlags, Pointcut thePointcut) {

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		RuleTarget target = new RuleTarget();

		switch (myOp) {
			case READ:
				if (theOutputResource == null) {

					switch (theOperation) {
						case READ:
						case VREAD:
							target.resourceIds = Collections.singleton(theInputResourceId);
							target.resourceType = theInputResourceId.getResourceType();
							break;
						case SEARCH_SYSTEM:
						case HISTORY_SYSTEM:
							if (theFlags.contains(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							}
							break;
						case SEARCH_TYPE:
							if (theFlags.contains(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							}
							target.resourceType = theRequestDetails.getResourceName();
							target.setSearchParams(theRequestDetails);

							/*
							 * If this is a search with an "_id" parameter, we can treat this
							 * as a read for the given resource ID(s)
							 */
							if (theRequestDetails.getParameters().containsKey(SP_RES_ID)) {
								setTargetFromResourceId(theRequestDetails, ctx, target);
							}
							break;
						case HISTORY_TYPE:
							if (theFlags.contains(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							}
							target.resourceType = theRequestDetails.getResourceName();
							break;
						case HISTORY_INSTANCE:
							if (theFlags.contains(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							}
							target.resourceIds = Collections.singleton(theInputResourceId);
							break;
						case GET_PAGE:
							return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);

						// None of the following are checked on the way in
						case ADD_TAGS:
						case DELETE_TAGS:
						case GET_TAGS:
						case GRAPHQL_REQUEST:
						case EXTENDED_OPERATION_SERVER:
						case EXTENDED_OPERATION_TYPE:
						case EXTENDED_OPERATION_INSTANCE:
						case CREATE:
						case DELETE:
						case TRANSACTION:
						case UPDATE:
						case VALIDATE:
						case METADATA:
						case META_ADD:
						case META:
						case META_DELETE:
						case PATCH:
						default:
							return null;
					}
				}
				target.resource = theOutputResource;
				if (theOutputResource != null) {
					target.resourceIds = Collections.singleton(theOutputResource.getIdElement());
				}
				break;
			case WRITE:
				if (theInputResource == null && theInputResourceId == null) {
					return null;
				}
				switch (theOperation) {
					case CREATE:
					case UPDATE:
					case ADD_TAGS:
					case DELETE_TAGS:
					case META_ADD:
					case META_DELETE:
						target.resource = theInputResource;
						if (theInputResourceId != null) {
							target.resourceIds = Collections.singletonList(theInputResourceId);
						}
						break;
					case PATCH:
						target.resource = null;
						if (theInputResourceId != null) {
							target.resourceIds = Collections.singletonList(theInputResourceId);
						} else {
							return null;
						}
						break;
					default:
						return null;
				}
				break;
			case CREATE:
				if (theInputResource == null && theInputResourceId == null) {
					return null;
				}
				if (theOperation == RestOperationTypeEnum.CREATE) {
					target.resource = theInputResource;
					if (theInputResourceId != null) {
						target.resourceIds = Collections.singletonList(theInputResourceId);
					}
				} else {
					return null;
				}
				break;
			case DELETE:
				if (theOperation == RestOperationTypeEnum.DELETE) {
					if (thePointcut == Pointcut.STORAGE_PRE_DELETE_EXPUNGE && myAppliesToDeleteExpunge) {
						return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
					}
					if (myAppliesToDeleteCascade != (thePointcut == Pointcut.STORAGE_CASCADE_DELETE)) {
						return null;
					}
					if (theInputResourceId == null) {
						return null;
					}
					if (theInputResourceId.hasIdPart() == false) {
						// This is a conditional DELETE, so we'll authorize it using STORAGE events instead
						// so just let it through for now..
						return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
					}
					if (theInputResource == null && myClassifierCompartmentOwners != null && myClassifierCompartmentOwners.size() > 0) {
						return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
					}

					target.resource = theInputResource;
					target.resourceIds = Collections.singleton(theInputResourceId);
				} else {
					return null;
				}
				break;
			case GRAPHQL:
				return applyRuleToGraphQl(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, thePointcut);
			case TRANSACTION:
				return applyRuleToTransaction(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theRuleApplier, thePointcut, ctx);
			case ALL:
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			case METADATA:
				if (theOperation == RestOperationTypeEnum.METADATA) {
					return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
				}
				return null;
			default:
				// Should not happen
				throw new IllegalStateException(Msg.code(335) + "Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
			case INSTANCES:
				return applyRuleToInstances(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, target);
			case ALL_RESOURCES:
				if (target.resourceType != null) {
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
					}
				}
				break;
			case TYPES:
				if (target.resource != null) {
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						String type = theRequestDetails.getFhirContext().getResourceType(target.resource);
						if (myAppliesToTypes.contains(type) == false) {
							return null;
						}
					}
				}
				if (target.resourceIds != null) {
					for (IIdType nextRequestAppliesToResourceId : target.resourceIds) {
						if (nextRequestAppliesToResourceId.hasResourceType()) {
							String nextRequestAppliesToResourceIdType = nextRequestAppliesToResourceId.getResourceType();
							if (myAppliesToTypes.contains(nextRequestAppliesToResourceIdType) == false) {
								return null;
							}
						}
					}
				}
				if (target.resourceType != null) {
					if (!myAppliesToTypes.contains(target.resourceType)) {
						return null;
					}
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
					} else if (myClassifierType == ClassifierTypeEnum.IN_COMPARTMENT) {
						// ok we'll check below
					}
				}
				break;
			default:
				throw new IllegalStateException(Msg.code(336) + "Unable to apply security to event of applies to type " + myAppliesTo);
		}

		return applyRuleLogic(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theFlags, ctx, target, theRuleApplier);
	}

	/**
	 * Apply any special processing logic specific to this rule.
	 * This is intended to be overridden.
	 *
	 * TODO: At this point {@link RuleImplOp} handles "any ID" and "in compartment" logic - It would be nice to split these into separate classes.
	 */
	protected Verdict applyRuleLogic(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Set<AuthorizationFlagsEnum> theFlags, FhirContext theFhirContext, RuleTarget theRuleTarget, IRuleApplier theRuleApplier) {
		switch (myClassifierType) {
			case ANY_ID:
				break;
			case IN_COMPARTMENT:
				return applyRuleToCompartment(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource, theFlags, theFhirContext, theRuleTarget);
			default:
				throw new IllegalStateException(Msg.code(337) + "Unable to apply security to event of applies to type " + myAppliesTo);
		}

		return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
	}

	@Nullable
	private Verdict applyRuleToGraphQl(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Pointcut thePointcut) {
		if (theOperation == RestOperationTypeEnum.GRAPHQL_REQUEST) {

			// Make sure that the requestor actually has sufficient access to see the given resource
			if (isResourceAccess(thePointcut)) {
				return null;
			}

			return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
		} else {
			return null;
		}
	}

	@Nullable
	private Verdict applyRuleToCompartment(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, Set<AuthorizationFlagsEnum> theFlags, FhirContext ctx, RuleTarget target) {
		FhirTerser t = ctx.newTerser();
		boolean foundMatch = false;

		if (target.resourceIds != null && target.resourceIds.size() > 0) {
			boolean haveOwnersForAll = target.resourceIds
				.stream()
				.allMatch(n -> myClassifierCompartmentOwners.contains(n.toUnqualifiedVersionless()));
			if (haveOwnersForAll) {
				foundMatch = true;
			}
		}

		for (IIdType next : myClassifierCompartmentOwners) {
			if (target.resource != null) {

				Set<String> additionalSearchParamNames = null;
				if (myAdditionalCompartmentSearchParamMap != null) {
					additionalSearchParamNames = myAdditionalCompartmentSearchParamMap.getSearchParamNamesForResourceType(ctx.getResourceType(target.resource));
				}
				if (t.isSourceInCompartmentForTarget(myClassifierCompartmentName, target.resource, next, additionalSearchParamNames)) {
					foundMatch = true;
					break;
				}
			}

			/*
			 * If the client has permission to read compartment
			 * Patient/ABC, then a search for Patient?_id=Patient/ABC
			 * should be permitted. This is kind of a one-off case, but
			 * it makes sense.
			 */
			if (next.getResourceType().equals(target.resourceType)) {
				Verdict verdict = checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(target.getSearchParams(), next, SP_RES_ID, theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
				if (verdict != null) {
					return verdict;
				}
			}

			/*
			 * If we're trying to read a resource that could potentially be
			 * in the given compartment, we'll let the request through and
			 * catch any issues on the response.
			 *
			 * This is less than perfect, but it's the best we can do-
			 * If the user is allowed to see compartment "Patient/123" and
			 * the client is requesting to read a CarePlan, there is nothing
			 * in the request URL that indicates whether or not the CarePlan
			 * might be in the given compartment.
			 */
			if (isNotBlank(target.resourceType)) {
				RuntimeResourceDefinition sourceDef = theRequestDetails.getFhirContext().getResourceDefinition(target.resourceType);
				String compartmentOwnerResourceType = next.getResourceType();
				if (!StringUtils.equals(target.resourceType, compartmentOwnerResourceType)) {

					List<RuntimeSearchParam> params = sourceDef.getSearchParamsForCompartmentName(compartmentOwnerResourceType);

					Set<String> additionalParamNames = myAdditionalCompartmentSearchParamMap.getSearchParamNamesForResourceType(sourceDef.getName());
					List<RuntimeSearchParam> additionalParams = additionalParamNames.stream().map(sourceDef::getSearchParam).filter(Objects::nonNull).collect(Collectors.toList());
					if (params == null || params.isEmpty()) {
						params = additionalParams;
					} else {
						List<RuntimeSearchParam> existingParams = params;
						params = new ArrayList<>(existingParams.size() + additionalParams.size());
						params.addAll(existingParams);
						params.addAll(additionalParams);
					}

					if (!params.isEmpty()) {

						/*
						 * If this is a search, we can at least check whether
						 * the client has requested a search parameter that
						 * would match the given compartment. In this case, this
						 * is a very effective mechanism.
						 */
						if (target.getSearchParams() != null && !theFlags.contains(AuthorizationFlagsEnum.DO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
							for (RuntimeSearchParam nextRuntimeSearchParam : params) {
								String name = nextRuntimeSearchParam.getName();
								Verdict verdict = checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(target.getSearchParams(), next, name, theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
								if (verdict != null) {
									return verdict;
								}
							}
						} else if (getMode() == PolicyEnum.ALLOW) {
							return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
						}
					}
				}
			}
		}
		if (!foundMatch) {
			return null;
		}
		return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
	}

	@Nullable
	private Verdict applyRuleToInstances(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, RuleTarget target) {
		if (target.resourceIds != null && target.resourceIds.size() > 0) {
			int haveMatches = 0;
			for (IIdType requestAppliesToResource : target.resourceIds) {

				for (IIdType next : myAppliesToInstances) {
					if (isNotBlank(next.getResourceType())) {
						if (!next.getResourceType().equals(requestAppliesToResource.getResourceType())) {
							continue;
						}
					}
					if (!next.getIdPart().equals(requestAppliesToResource.getIdPart())) {
						continue;
					}
					haveMatches++;
					break;
				}

			}

			if (haveMatches == target.resourceIds.size()) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}
		}

		return null;
	}

	@Nullable
	private Verdict applyRuleToTransaction(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource, IRuleApplier theRuleApplier, Pointcut thePointcut, FhirContext ctx) {
		if (!(theOperation == RestOperationTypeEnum.TRANSACTION)) {
			return null;
		}
		if (theInputResource != null && requestAppliesToTransaction(ctx, myOp, theInputResource)) {
			if (getMode() == PolicyEnum.DENY) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}
			List<BundleEntryParts> inputResources = BundleUtil.toListOfEntries(ctx, (IBaseBundle) theInputResource);
			Verdict verdict = null;

			boolean allComponentsAreGets = true;
			for (BundleEntryParts nextPart : inputResources) {

				IBaseResource inputResource = nextPart.getResource();
				IIdType inputResourceId = null;
				if (isNotBlank(nextPart.getUrl())) {

					UrlUtil.UrlParts parts = UrlUtil.parseUrl(nextPart.getUrl());

					inputResourceId = theRequestDetails.getFhirContext().getVersion().newIdType();
					inputResourceId.setParts(null, parts.getResourceType(), parts.getResourceId(), null);
				}

				RestOperationTypeEnum operation;
				if (nextPart.getRequestType() == RequestTypeEnum.GET) {
					continue;
				} else {
					allComponentsAreGets = false;
				}
				if (nextPart.getRequestType() == RequestTypeEnum.POST) {
					operation = RestOperationTypeEnum.CREATE;
				} else if (nextPart.getRequestType() == RequestTypeEnum.PUT) {
					operation = RestOperationTypeEnum.UPDATE;
				} else if (nextPart.getRequestType() == RequestTypeEnum.DELETE) {
					operation = RestOperationTypeEnum.DELETE;
				} else if (nextPart.getRequestType() == RequestTypeEnum.PATCH) {
					operation = RestOperationTypeEnum.PATCH;
				} else if (nextPart.getRequestType() == null && theRequestDetails.getServer().getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU3 && BundleUtil.isDstu3TransactionPatch(theRequestDetails.getFhirContext(), nextPart.getResource())) {
					// This is a workaround for the fact that there is no PATCH verb in DSTU3's bundle entry verb type ValueSet.
					// See BundleUtil#isDstu3TransactionPatch
					operation = RestOperationTypeEnum.PATCH;
				} else {

					throw new InvalidRequestException(Msg.code(338) + "Can not handle transaction with operation of type " + nextPart.getRequestType());
				}

				/*
				 * This is basically just being conservative - Be careful of transactions containing
				 * nested operations and nested transactions. We block them by default. At some point
				 * it would be nice to be more nuanced here.
				 */
				if (nextPart.getResource() != null) {
					RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(nextPart.getResource());
					if ("Parameters".equals(resourceDef.getName()) || "Bundle".equals(resourceDef.getName())) {
						throw new InvalidRequestException(Msg.code(339) + "Can not handle transaction with nested resource of type " + resourceDef.getName());
					}
				}

				String previousFixedConditionalUrl = theRequestDetails.getFixedConditionalUrl();
				theRequestDetails.setFixedConditionalUrl(nextPart.getConditionalUrl());

				Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(operation, theRequestDetails, inputResource, inputResourceId, null, thePointcut);

				theRequestDetails.setFixedConditionalUrl(previousFixedConditionalUrl);

				if (newVerdict == null) {
					continue;
				} else if (verdict == null) {
					verdict = newVerdict;
				} else if (verdict.getDecision() == PolicyEnum.ALLOW && newVerdict.getDecision() == PolicyEnum.DENY) {
					verdict = newVerdict;
				}
			}

			/*
			 * If we're handling a transaction with all gets and nothing else, we'll
			 * be applying security on the way out
			 */
			if (allComponentsAreGets) {
				return newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
			}

			return verdict;
		} else if (theOutputResource != null) {

			List<IBaseResource> outputResources = AuthorizationInterceptor.toListOfResourcesAndExcludeContainer(theOutputResource, theRequestDetails.getFhirContext());

			Verdict verdict = null;
			for (IBaseResource nextResource : outputResources) {
				if (nextResource == null) {
					continue;
				}
				Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(RestOperationTypeEnum.READ, theRequestDetails, null, null, nextResource, thePointcut);
				if (newVerdict == null) {
					continue;
				} else if (verdict == null) {
					verdict = newVerdict;
				} else if (verdict.getDecision() == PolicyEnum.ALLOW && newVerdict.getDecision() == PolicyEnum.DENY) {
					verdict = newVerdict;
				}
			}
			return verdict;
		} else {
			return null;
		}
	}

	private void setTargetFromResourceId(RequestDetails theRequestDetails, FhirContext ctx, RuleTarget target) {
		String[] idValues = theRequestDetails.getParameters().get(SP_RES_ID);
		target.resourceIds = new ArrayList<>();

		for (String nextIdValue : idValues) {
			QualifiedParamList orParamList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, nextIdValue);
			for (String next : orParamList) {
				IIdType nextId = ctx.getVersion().newIdType().setValue(next);
				if (nextId.hasIdPart()) {
					if (!nextId.hasResourceType()) {
						nextId = nextId.withResourceType(target.resourceType);
					}
					if (nextId.getResourceType().equals(target.resourceType)) {
						target.resourceIds.add(nextId);
					}
				}
			}
		}
		if (target.resourceIds.isEmpty()) {
			target.resourceIds = null;
		}
	}

	private Verdict checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(Map<String, String[]> theSearchParams, IIdType theCompartmentOwner, String theSearchParamName, RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource) {
		Verdict verdict = null;
		if (theSearchParams != null) {
			String[] values = theSearchParams.get(theSearchParamName);
			if (values != null) {
				for (String nextParameterValue : values) {
					QualifiedParamList orParamList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, nextParameterValue);
					for (String next : orParamList) {
						if (next.equals(theCompartmentOwner.getValue())) {
							verdict = newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							break;
						}
						if (next.equals(theCompartmentOwner.getIdPart())) {
							verdict = newVerdict(theOperation, theRequestDetails, theInputResource, theInputResourceId, theOutputResource);
							break;
						}
					}
				}
			}
		}
		return verdict;
	}

	public void setTransactionAppliesToOp(TransactionAppliesToEnum theOp) {
		myTransactionAppliesToOp = theOp;
	}

	private boolean requestAppliesToTransaction(FhirContext theContext, RuleOpEnum theOp, IBaseResource theInputResource) {
		if (!"Bundle".equals(theContext.getResourceType(theInputResource))) {
			return false;
		}

		IBaseBundle request = (IBaseBundle) theInputResource;
		String bundleType = defaultString(BundleUtil.getBundleType(theContext, request));

		//noinspection EnumSwitchStatementWhichMissesCases
		if (theOp == RuleOpEnum.TRANSACTION) {
			if ("transaction".equals(bundleType) || "batch".equals(bundleType)) {
				return true;
			} else {
				String msg = theContext.getLocalizer().getMessage(RuleImplOp.class, "invalidRequestBundleTypeForTransaction", '"' + bundleType + '"');
				throw new UnprocessableEntityException(Msg.code(340) + msg);
			}
		}
		return false;
	}

	public void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	void setAppliesToTypes(Set<String> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	void setClassifierCompartmentName(String theClassifierCompartmentName) {
		myClassifierCompartmentName = theClassifierCompartmentName;
	}

	void setClassifierCompartmentOwners(Collection<? extends IIdType> theInCompartmentOwners) {
		myClassifierCompartmentOwners = theInCompartmentOwners;
	}

	void setClassifierType(ClassifierTypeEnum theClassifierType) {
		myClassifierType = theClassifierType;
	}

	public RuleImplOp setOp(RuleOpEnum theRuleOp) {
		myOp = theRuleOp;
		return this;
	}


	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("op", myOp);
		builder.append("transactionAppliesToOp", myTransactionAppliesToOp);
		builder.append("appliesTo", myAppliesTo);
		builder.append("appliesToTypes", myAppliesToTypes);
		builder.append("classifierCompartmentName", myClassifierCompartmentName);
		builder.append("classifierCompartmentOwners", myClassifierCompartmentOwners);
		builder.append("classifierType", myClassifierType);
		return builder.toString();
	}

	void setAppliesToDeleteCascade(boolean theAppliesToDeleteCascade) {
		myAppliesToDeleteCascade = theAppliesToDeleteCascade;
	}

	void setAppliesToDeleteExpunge(boolean theAppliesToDeleteExpunge) {
		myAppliesToDeleteExpunge = theAppliesToDeleteExpunge;
	}

	public void addClassifierCompartmentOwner(IIdType theOwner) {
		List<IIdType> newList = new ArrayList<>(myClassifierCompartmentOwners);
		newList.add(theOwner);
		myClassifierCompartmentOwners = newList;
	}

	public boolean matches(RuleOpEnum theRuleOp, AppliesTypeEnum theAppliesTo, Collection<IIdType> theAppliesToInstances, Set<String> theAppliesToTypes, ClassifierTypeEnum theClassifierType, String theCompartmentName) {
		if (theRuleOp != myOp ||
			theAppliesTo != myAppliesTo ||
			theClassifierType != myClassifierType) {
			return false;
		}

		switch (theAppliesTo) {
			case TYPES:
				return theAppliesToTypes.equals(myAppliesToTypes) && theCompartmentName.equals(myClassifierCompartmentName);
			case INSTANCES:
				return theAppliesToInstances.equals(myAppliesToInstances) && theCompartmentName.equals(myClassifierCompartmentName);
			case ALL_RESOURCES:
				return theCompartmentName.equals(myClassifierCompartmentName);
			default:
				// no more cases
				return false;
		}
	}

	public void setAdditionalSearchParamsForCompartmentTypes(AdditionalCompartmentSearchParameters theAdditionalParameters) {
		myAdditionalCompartmentSearchParamMap = theAdditionalParameters;
	}

}
