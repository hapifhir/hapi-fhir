package ca.uhn.fhir.rest.server.interceptor.auth;

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
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

		if (isOtherTenant(theRequestDetails)) {
			return null;
		}

		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		IBaseResource appliesToResource;
		Collection<IIdType> appliesToResourceId = null;
		String appliesToResourceType = null;
		Map<String, String[]> appliesToSearchParams = null;
		switch (myOp) {
			case READ:
				if (theOutputResource == null) {
					if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
						return null;
					}

					switch (theOperation) {
						case READ:
						case VREAD:
							appliesToResourceId = Collections.singleton(theInputResourceId);
							appliesToResourceType = theInputResourceId.getResourceType();
							break;
						case SEARCH_SYSTEM:
						case HISTORY_SYSTEM:
							if (theFlags.contains(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return new Verdict(PolicyEnum.ALLOW, this);
							}
							break;
						case SEARCH_TYPE:
							if (theFlags.contains(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return new Verdict(PolicyEnum.ALLOW, this);
							}
							appliesToResourceType = theRequestDetails.getResourceName();
							appliesToSearchParams = theRequestDetails.getParameters();

							/*
							 * If this is a search with an "_id" parameter, we can treat this
							 * as a read for the given resource ID(s)
							 */
							if (theRequestDetails.getParameters().containsKey("_id")) {
								String[] idValues = theRequestDetails.getParameters().get("_id");
								appliesToResourceId = new ArrayList<>();

								for (String nextIdValue : idValues) {
									QualifiedParamList orParamList = QualifiedParamList.splitQueryStringByCommasIgnoreEscape(null, nextIdValue);
									for (String next : orParamList) {
										IIdType nextId = ctx.getVersion().newIdType().setValue(next);
										if (nextId.hasIdPart()) {
											if (!nextId.hasResourceType()) {
												nextId = nextId.withResourceType(appliesToResourceType);
											}
											if (nextId.getResourceType().equals(appliesToResourceType)) {
												appliesToResourceId.add(nextId);
											}
										}
									}
								}
								if (appliesToResourceId.isEmpty()) {
									appliesToResourceId = null;
								}
							}
							break;
						case HISTORY_TYPE:
							if (theFlags.contains(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return new Verdict(PolicyEnum.ALLOW, this);
							}
							appliesToResourceType = theRequestDetails.getResourceName();
							break;
						case HISTORY_INSTANCE:
							if (theFlags.contains(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
								return new Verdict(PolicyEnum.ALLOW, this);
							}
							appliesToResourceId = Collections.singleton(theInputResourceId);
							break;
						case GET_PAGE:
							return new Verdict(PolicyEnum.ALLOW, this);

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
				appliesToResource = theOutputResource;
				if (theOutputResource != null) {
					appliesToResourceId = Collections.singleton(theOutputResource.getIdElement());
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
						appliesToResource = theInputResource;
						if (theInputResourceId != null) {
							appliesToResourceId = Collections.singletonList(theInputResourceId);
						}
						break;
					case PATCH:
						appliesToResource = null;
						if (theInputResourceId != null) {
							appliesToResourceId = Collections.singletonList(theInputResourceId);
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
					appliesToResource = theInputResource;
					if (theInputResourceId != null) {
						appliesToResourceId = Collections.singletonList(theInputResourceId);
					}
				} else {
					return null;
				}
				break;
			case DELETE:
				if (theOperation == RestOperationTypeEnum.DELETE) {
					if (myAppliesToDeleteCascade != (thePointcut == Pointcut.STORAGE_CASCADE_DELETE)) {
						return null;
					}
					if (theInputResourceId == null) {
						return null;
					}
					if (theInputResourceId.hasIdPart() == false) {
						// This is a conditional DELETE, so we'll authorize it using STORAGE events instead
						// so just let it through for now..
						return newVerdict();
					}
					if (theInputResource== null && myClassifierCompartmentOwners != null && myClassifierCompartmentOwners.size() > 0) {
						return newVerdict();
					}

					appliesToResource = theInputResource;
					appliesToResourceId = Collections.singleton(theInputResourceId);
				} else {
					return null;
				}
				break;
			case GRAPHQL:
				if (theOperation == RestOperationTypeEnum.GRAPHQL_REQUEST) {
					return newVerdict();
				} else {
					return null;
				}
			case TRANSACTION:
				if (!(theOperation == RestOperationTypeEnum.TRANSACTION)) {
					return null;
				}
				if (theInputResource != null && requestAppliesToTransaction(ctx, myOp, theInputResource)) {
					if (getMode() == PolicyEnum.DENY) {
						return new Verdict(PolicyEnum.DENY, this);
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
						} else if (nextPart.getRequestType() == null && theRequestDetails.getServer().getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU3 && BundleUtil.isDstu3TransactionPatch(nextPart.getResource())) {
							// This is a workaround for the fact that there is no PATCH verb in DSTU3's bundle entry verb type ValueSet.
							// See BundleUtil#isDstu3TransactionPatch
							operation = RestOperationTypeEnum.PATCH;
						} else {

							throw new InvalidRequestException("Can not handle transaction with operation of type " + nextPart.getRequestType());
						}

						/*
						 * This is basically just being conservative - Be careful of transactions containing
						 * nested operations and nested transactions. We block them by default. At some point
						 * it would be nice to be more nuanced here.
						 */
						if (nextPart.getResource() != null) {
							RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(nextPart.getResource());
							if ("Parameters".equals(resourceDef.getName()) || "Bundle".equals(resourceDef.getName())) {
								throw new InvalidRequestException("Can not handle transaction with nested resource of type " + resourceDef.getName());
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
						return newVerdict();
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
			case ALLOW_ALL:
				if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
					return null;
				}
				return new Verdict(PolicyEnum.ALLOW, this);
			case DENY_ALL:
				if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
					return null;
				}
				return new Verdict(PolicyEnum.DENY, this);
			case METADATA:
				if (theOperation == RestOperationTypeEnum.METADATA) {
					if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
						return null;
					}
					return newVerdict();
				}
				return null;
			default:
				// Should not happen
				throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
			case INSTANCES:
				if (appliesToResourceId != null && appliesToResourceId.size() > 0) {
					int haveMatches = 0;
					for (IIdType requestAppliesToResource : appliesToResourceId) {

						for (IIdType next : myAppliesToInstances) {
							if (isNotBlank(next.getResourceType())) {
								if (!next.getResourceType().equals(requestAppliesToResource.getResourceType())) {
									continue;
								}
							}
							if (!next.getIdPart().equals(requestAppliesToResource.getIdPart())) {
								continue;
							}
							if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
								return null;
							}
							haveMatches++;
							break;
						}

					}

					if (haveMatches == appliesToResourceId.size()) {
						return newVerdict();
					}
				}

				return null;
			case ALL_RESOURCES:
				if (appliesToResourceType != null) {
					if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
						return null;
					}
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						return new Verdict(PolicyEnum.ALLOW, this);
					}
				}
				break;
			case TYPES:
				if (appliesToResource != null) {
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						String type = theRequestDetails.getFhirContext().getResourceDefinition(appliesToResource).getName();
						if (myAppliesToTypes.contains(type) == false) {
							return null;
						}
					}
				}
				if (appliesToResourceId != null) {
					for (IIdType nextRequestAppliesToResourceId : appliesToResourceId) {
						if (nextRequestAppliesToResourceId.hasResourceType()) {
							String nextRequestAppliesToResourceIdType = nextRequestAppliesToResourceId.getResourceType();
							if (myAppliesToTypes.contains(nextRequestAppliesToResourceIdType) == false) {
								return null;
							}
						}
					}
				}
				if (appliesToResourceType != null) {
					if (!myAppliesToTypes.contains(appliesToResourceType)) {
						return null;
					}
					if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
						return null;
					}
					if (myClassifierType == ClassifierTypeEnum.ANY_ID) {
						return newVerdict();
					} else if (myClassifierType == ClassifierTypeEnum.IN_COMPARTMENT) {
						// ok we'll check below
					}
				}
				break;
			default:
				throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		switch (myClassifierType) {
			case ANY_ID:
				break;
			case IN_COMPARTMENT:
				FhirTerser t = ctx.newTerser();
				boolean foundMatch = false;

				if (appliesToResourceId != null && appliesToResourceId.size() > 0) {
					boolean haveOwnersForAll = appliesToResourceId
						.stream()
						.allMatch(n -> myClassifierCompartmentOwners.contains(n.toUnqualifiedVersionless()));
					if (haveOwnersForAll) {
						foundMatch = true;
					}
				}

				for (IIdType next : myClassifierCompartmentOwners) {
					if (appliesToResource != null) {
						if (t.isSourceInCompartmentForTarget(myClassifierCompartmentName, appliesToResource, next)) {
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
					if (next.getResourceType().equals(appliesToResourceType)) {
						Verdict verdict = checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(appliesToSearchParams, next, IAnyResource.SP_RES_ID);
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
					if (isNotBlank(appliesToResourceType)) {
						RuntimeResourceDefinition sourceDef = theRequestDetails.getFhirContext().getResourceDefinition(appliesToResourceType);
						String compartmentOwnerResourceType = next.getResourceType();
						if (!StringUtils.equals(appliesToResourceType, compartmentOwnerResourceType)) {
							List<RuntimeSearchParam> params = sourceDef.getSearchParamsForCompartmentName(compartmentOwnerResourceType);
							if (!params.isEmpty()) {

								/*
								 * If this is a search, we can at least check whether
								 * the client has requested a search parameter that
								 * would match the given compartment. In this case, this
								 * is a very effective mechanism.
								 */
								if (appliesToSearchParams != null && !theFlags.contains(AuthorizationFlagsEnum.NO_NOT_PROACTIVELY_BLOCK_COMPARTMENT_READ_ACCESS)) {
									for (RuntimeSearchParam nextRuntimeSearchParam : params) {
										String name = nextRuntimeSearchParam.getName();
										Verdict verdict = checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(appliesToSearchParams, next, name);
										if (verdict != null) {
											return verdict;
										}
									}
								} else {
									return new Verdict(PolicyEnum.ALLOW, this);
								}
								break;
							}
						}
					}
				}
				if (!foundMatch) {
					return null;
				}
				break;
			default:
				throw new IllegalStateException("Unable to apply security to event of applies to type " + myAppliesTo);
		}

		if (!applyTesters(theOperation, theRequestDetails, theInputResourceId, theInputResource, theOutputResource)) {
			return null;
		}

		return newVerdict();
	}

	private Verdict checkForSearchParameterMatchingCompartmentAndReturnSuccessfulVerdictOrNull(Map<String, String[]> theSearchParams, IIdType theCompartmentOwner, String theSearchParamName) {
		Verdict verdict = null;
		if (theSearchParams != null) {
			String[] values = theSearchParams.get(theSearchParamName);
			if (values != null) {
				for (String nextParameterValue : values) {
					if (nextParameterValue.equals(theCompartmentOwner.getValue())) {
						verdict = new Verdict(PolicyEnum.ALLOW, this);
						break;
					}
					if (nextParameterValue.equals(theCompartmentOwner.getIdPart())) {
						verdict = new Verdict(PolicyEnum.ALLOW, this);
						break;
					}
				}
			}
		}
		return verdict;
	}

	public TransactionAppliesToEnum getTransactionAppliesToOp() {
		return myTransactionAppliesToOp;
	}

	public void setTransactionAppliesToOp(TransactionAppliesToEnum theOp) {
		myTransactionAppliesToOp = theOp;
	}

	private boolean requestAppliesToTransaction(FhirContext theContext, RuleOpEnum theOp, IBaseResource theInputResource) {
		if (!"Bundle".equals(theContext.getResourceDefinition(theInputResource).getName())) {
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
				throw new UnprocessableEntityException(msg);
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
		builder.append("appliesToTenant", getTenantApplicabilityChecker());
		builder.append("classifierCompartmentName", myClassifierCompartmentName);
		builder.append("classifierCompartmentOwners", myClassifierCompartmentOwners);
		builder.append("classifierType", myClassifierType);
		return builder.toString();
	}

	void setAppliesToDeleteCascade(boolean theAppliesToDeleteCascade) {
		myAppliesToDeleteCascade = theAppliesToDeleteCascade;
	}

}
