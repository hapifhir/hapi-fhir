package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.BundleUtil.BundleEntryParts;
import ca.uhn.fhir.util.FhirTerser;

class RuleImplOp extends BaseRule /* implements IAuthRule */ {

	private AppliesTypeEnum myAppliesTo;
	private Set<?> myAppliesToTypes;
	private String myClassifierCompartmentName;
	private Collection<? extends IIdType> myClassifierCompartmentOwners;
	private ClassifierTypeEnum myClassifierType;
	private RuleOpEnum myOp;
	private TransactionAppliesToEnum myTransactionAppliesToOp;
	private List<IIdType> myAppliesToInstances;

	public RuleImplOp(String theRuleName) {
		super(theRuleName);
	}

	@Override
	public Verdict applyRule(RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theInputResource, IIdType theInputResourceId, IBaseResource theOutputResource,
			IRuleApplier theRuleApplier) {
		FhirContext ctx = theRequestDetails.getServer().getFhirContext();

		IBaseResource appliesToResource;
		IIdType appliesToResourceId = null;
		String appliesToResourceType = null;
		switch (myOp) {
		case READ:
			if (theOutputResource == null) {
				switch (theOperation) {
				case READ:
				case VREAD:
					appliesToResourceId = theInputResourceId;
					appliesToResourceType = theInputResourceId.getResourceType();
					break;
				case SEARCH_SYSTEM:
				case SEARCH_TYPE:
				case HISTORY_INSTANCE:
				case HISTORY_SYSTEM:
				case HISTORY_TYPE:
					return new Verdict(PolicyEnum.ALLOW, this);
				default:
					return null;
				}
			}
			appliesToResource = theOutputResource;
			if (theOutputResource != null) {
				appliesToResourceId = theOutputResource.getIdElement();
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
			case PATCH:
				appliesToResource = theInputResource;
				appliesToResourceId = theInputResourceId;
				break;
			default:
				return null;
			}
			break;
		case DELETE:
			if (theOperation == RestOperationTypeEnum.DELETE) {
				if (theInputResource == null) {
					return newVerdict();
				}
				appliesToResource = theInputResource;
			} else {
				return null;
			}
			break;
		case BATCH:
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
				for (BundleEntryParts nextPart : inputResources) {

					IBaseResource inputResource = nextPart.getResource();
					RestOperationTypeEnum operation = null;
					if (nextPart.getRequestType() == RequestTypeEnum.GET) {
						continue;
					}
					if (nextPart.getRequestType() == RequestTypeEnum.POST) {
						operation = RestOperationTypeEnum.CREATE;
					} else if (nextPart.getRequestType() == RequestTypeEnum.PUT) {
						operation = RestOperationTypeEnum.UPDATE;
					} else {
						throw new InvalidRequestException("Can not handle transaction with operation of type " + nextPart.getRequestType());
					}

					/*
					 * This is basically just being conservative - Be careful of transactions containing
					 * nested operations and nested transactions. We block the by default. At some point
					 * it would be nice to be more nuanced here.
					 */
					RuntimeResourceDefinition resourceDef = ctx.getResourceDefinition(nextPart.getResource());
					if ("Parameters".equals(resourceDef.getName()) || "Bundle".equals(resourceDef.getName())) {
						throw new InvalidRequestException("Can not handle transaction with nested resource of type " + resourceDef.getName());
					}

					Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(operation, theRequestDetails, inputResource, null, null);
					if (newVerdict == null) {
						continue;
					} else if (verdict == null) {
						verdict = newVerdict;
					} else if (verdict.getDecision() == PolicyEnum.ALLOW && newVerdict.getDecision() == PolicyEnum.DENY) {
						verdict = newVerdict;
					}
				}
				return verdict;
			} else if (theOutputResource != null) {
				List<BundleEntryParts> inputResources = BundleUtil.toListOfEntries(ctx, (IBaseBundle) theInputResource);
				Verdict verdict = null;
				for (BundleEntryParts nextPart : inputResources) {
					if (nextPart.getResource() == null) {
						continue;
					}
					Verdict newVerdict = theRuleApplier.applyRulesAndReturnDecision(RestOperationTypeEnum.READ, theRequestDetails, null, null, nextPart.getResource());
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
			return new Verdict(PolicyEnum.ALLOW, this);
		case DENY_ALL:
			return new Verdict(PolicyEnum.DENY, this);
		case METADATA:
			if (theOperation == RestOperationTypeEnum.METADATA) {
				return newVerdict();
			}
			return null;
		default:
			// Should not happen
			throw new IllegalStateException("Unable to apply security to event of type " + theOperation);
		}

		switch (myAppliesTo) {
		case INSTANCES:
			if (appliesToResourceId != null) {
				for (IIdType next : myAppliesToInstances) {
					if (isNotBlank(next.getResourceType())) {
						if (!next.getResourceType().equals(appliesToResourceId.getResourceType())) {
							continue;
						}
					}
					if (!next.getIdPart().equals(appliesToResourceId.getIdPart())) {
						continue;
					}
					return newVerdict();
				}
			}
			return null;
		case ALL_RESOURCES:
			if (appliesToResourceType != null) {
				return new Verdict(PolicyEnum.ALLOW, this);
			}
			break;
		case TYPES:
			if (appliesToResource != null) {
				if (myAppliesToTypes.contains(appliesToResource.getClass()) == false) {
					return null;
				}
			}
			if (appliesToResourceId != null) {
				Class<? extends IBaseResource> type = theRequestDetails.getServer().getFhirContext().getResourceDefinition(appliesToResourceId.getResourceType()).getImplementingClass();
				if (myAppliesToTypes.contains(type) == false) {
					return null;
				}
			}
			if (appliesToResourceType != null) {
				Class<? extends IBaseResource> type = theRequestDetails.getServer().getFhirContext().getResourceDefinition(appliesToResourceType).getImplementingClass();
				if (myAppliesToTypes.contains(type)) {
					return new Verdict(PolicyEnum.ALLOW, this);
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
			for (IIdType next : myClassifierCompartmentOwners) {
				if (appliesToResource != null) {
					if (t.isSourceInCompartmentForTarget(myClassifierCompartmentName, appliesToResource, next)) {
						foundMatch = true;
						break;
					}
				}
				if (appliesToResourceId != null && appliesToResourceId.hasResourceType() && appliesToResourceId.hasIdPart()) {
					if (appliesToResourceId.toUnqualifiedVersionless().getValue().equals(next.toUnqualifiedVersionless().getValue())) {
						foundMatch = true;
						break;
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

		return newVerdict();
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

	private boolean requestAppliesToTransaction(FhirContext theContext, RuleOpEnum theOp, IBaseResource theInputResource) {
		if (!"Bundle".equals(theContext.getResourceDefinition(theInputResource).getName())) {
			return false;
		}

		IBaseBundle request = (IBaseBundle) theInputResource;
		String bundleType = BundleUtil.getBundleType(theContext, request);
		switch (theOp) {
		case TRANSACTION:
			return "transaction".equals(bundleType);
		case BATCH:
			return "batch".equals(bundleType);
		default:
			return false;
		}
	}

	public TransactionAppliesToEnum getTransactionAppliesToOp() {
		return myTransactionAppliesToOp;
	}

	public void setAppliesTo(AppliesTypeEnum theAppliesTo) {
		myAppliesTo = theAppliesTo;
	}

	public void setAppliesToTypes(Set<?> theAppliesToTypes) {
		myAppliesToTypes = theAppliesToTypes;
	}

	public void setClassifierCompartmentName(String theClassifierCompartmentName) {
		myClassifierCompartmentName = theClassifierCompartmentName;
	}

	public void setClassifierCompartmentOwners(Collection<? extends IIdType> theInCompartmentOwners) {
		myClassifierCompartmentOwners = theInCompartmentOwners;
	}

	public void setClassifierType(ClassifierTypeEnum theClassifierType) {
		myClassifierType = theClassifierType;
	}

	public RuleImplOp setOp(RuleOpEnum theRuleOp) {
		myOp = theRuleOp;
		return this;
	}

	public void setTransactionAppliesToOp(TransactionAppliesToEnum theOp) {
		myTransactionAppliesToOp = theOp;
	}

	public void setAppliesToInstances(List<IIdType> theAppliesToInstances) {
		myAppliesToInstances = theAppliesToInstances;
	}

}
