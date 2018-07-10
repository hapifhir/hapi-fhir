package ca.uhn.fhir.rest.server.interceptor.auth;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class RuleBuilder implements IAuthRuleBuilder {

	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private ArrayList<IAuthRule> myRules;

	public RuleBuilder() {
		myRules = new ArrayList<>();
	}

	@Override
	public IAuthRuleBuilderRule allow() {
		return allow(null);
	}

	@Override
	public IAuthRuleBuilderRule allow(String theRuleName) {
		return new RuleBuilderRule(PolicyEnum.ALLOW, theRuleName);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll() {
		return allowAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll(String theRuleName) {
		RuleImplOp rule = new RuleImplOp(theRuleName);
		myRules.add(rule.setOp(RuleOpEnum.ALLOW_ALL));
		return new RuleBuilderFinished(rule);
	}

	@Override
	public List<IAuthRule> build() {
		return myRules;
	}

	@Override
	public IAuthRuleBuilderRule deny() {
		return deny(null);
	}

	@Override
	public IAuthRuleBuilderRule deny(String theRuleName) {
		return new RuleBuilderRule(PolicyEnum.DENY, theRuleName);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll() {
		return denyAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll(String theRuleName) {
		RuleImplOp rule = new RuleImplOp(theRuleName);
		myRules.add(rule.setOp(RuleOpEnum.DENY_ALL));
		return new RuleBuilderFinished(rule);
	}

	public interface ITenantApplicabilityChecker {
		boolean applies(RequestDetails theRequest);
	}

	private class RuleBuilderFinished implements IAuthRuleFinished, IAuthRuleBuilderRuleOpClassifierFinished, IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId {

		private final BaseRule myOpRule;
		ITenantApplicabilityChecker myTenantApplicabilityChecker;
		private List<IAuthRuleTester> myTesters;

		RuleBuilderFinished(BaseRule theRule) {
			myOpRule = theRule;
		}

		@Override
		public IAuthRuleBuilder andThen() {
			doBuildRule();
			return RuleBuilder.this;
		}

		@Override
		public List<IAuthRule> build() {
			doBuildRule();
			return myRules;
		}

		/**
		 * Subclasses may override
		 */
		protected void doBuildRule() {
			// nothing
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId forTenantIds(String... theTenantIds) {
			return forTenantIds(Arrays.asList(defaultIfNull(theTenantIds, EMPTY_STRING_ARRAY)));
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId forTenantIds(final Collection<String> theTenantIds) {
			setTenantApplicabilityChecker(theRequest -> theTenantIds.contains(theRequest.getTenantId()));
			return this;
		}

		List<IAuthRuleTester> getTesters() {
			if (myTesters == null) {
				return Collections.emptyList();
			}
			return myTesters;
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId notForTenantIds(String... theTenantIds) {
			return notForTenantIds(Arrays.asList(defaultIfNull(theTenantIds, EMPTY_STRING_ARRAY)));
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId notForTenantIds(final Collection<String> theTenantIds) {
			setTenantApplicabilityChecker(theRequest -> !theTenantIds.contains(theRequest.getTenantId()));
			return this;
		}

		private void setTenantApplicabilityChecker(ITenantApplicabilityChecker theTenantApplicabilityChecker) {
			myTenantApplicabilityChecker = theTenantApplicabilityChecker;
				myOpRule.setTenantApplicabilityChecker(myTenantApplicabilityChecker);
		}

		@Override
		public IAuthRuleFinished withTester(IAuthRuleTester theTester) {
			if (myTesters == null) {
				myTesters = new ArrayList<>();
			}
			myTesters.add(theTester);
			myOpRule.addTester(theTester);

			return this;
		}
	}

	private class RuleBuilderRule implements IAuthRuleBuilderRule {

		private PolicyEnum myRuleMode;
		private String myRuleName;
		private RuleOpEnum myRuleOp;

		RuleBuilderRule(PolicyEnum theRuleMode, String theRuleName) {
			myRuleMode = theRuleMode;
			myRuleName = theRuleName;
		}

		@Override
		public IAuthRuleBuilderRuleConditional createConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.CREATE);
		}

		@Override
		public IAuthRuleBuilderRuleOp delete() {
			myRuleOp = RuleOpEnum.DELETE;
			return new RuleBuilderRuleOp();
		}

		@Override
		public IAuthRuleBuilderRuleConditional deleteConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.DELETE);
		}

		@Override
		public RuleBuilderFinished metadata() {
			RuleImplOp rule = new RuleImplOp(myRuleName);
			rule.setOp(RuleOpEnum.METADATA);
			rule.setMode(myRuleMode);
			myRules.add(rule);
			return new RuleBuilderFinished(rule);
		}

		@Override
		public IAuthRuleBuilderOperation operation() {
			return new RuleBuilderRuleOperation();
		}

		@Override
		public IAuthRuleBuilderPatch patch() {
			myRuleOp = RuleOpEnum.PATCH;
			return new PatchBuilder();
		}

		@Override
		public IAuthRuleBuilderRuleOp read() {
			myRuleOp = RuleOpEnum.READ;
			return new RuleBuilderRuleOp();
		}

		@Override
		public IAuthRuleBuilderRuleTransaction transaction() {
			myRuleOp = RuleOpEnum.TRANSACTION;
			return new RuleBuilderRuleTransaction();
		}

		@Override
		public IAuthRuleBuilderRuleConditional updateConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.UPDATE);
		}

		@Override
		public IAuthRuleBuilderRuleOp write() {
			myRuleOp = RuleOpEnum.WRITE;
			return new RuleBuilderRuleOp();
		}

		private class RuleBuilderRuleConditional implements IAuthRuleBuilderRuleConditional {

			private AppliesTypeEnum myAppliesTo;

			private Set<?> myAppliesToTypes;
			private RestOperationTypeEnum myOperationType;

			RuleBuilderRuleConditional(RestOperationTypeEnum theOperationType) {
				myOperationType = theOperationType;
			}

			@Override
			public IAuthRuleBuilderRuleConditionalClassifier allResources() {
				myAppliesTo = AppliesTypeEnum.ALL_RESOURCES;
				return new RuleBuilderRuleConditionalClassifier();
			}

			@Override
			public IAuthRuleBuilderRuleConditionalClassifier resourcesOfType(Class<? extends IBaseResource> theType) {
				Validate.notNull(theType, "theType must not be null");
				myAppliesTo = AppliesTypeEnum.TYPES;
				myAppliesToTypes = Collections.singleton(theType);
				return new RuleBuilderRuleConditionalClassifier();
			}

			public class RuleBuilderRuleConditionalClassifier extends RuleBuilderFinished implements IAuthRuleBuilderRuleConditionalClassifier {

				RuleBuilderRuleConditionalClassifier() {
					super(null);
				}

				@Override
				protected void doBuildRule() {
					RuleImplConditional rule = new RuleImplConditional(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOperationType(myOperationType);
					rule.setAppliesTo(myAppliesTo);
					rule.setAppliesToTypes(myAppliesToTypes);
					rule.setTenantApplicabilityChecker(myTenantApplicabilityChecker);
					rule.addTesters(getTesters());
					myRules.add(rule);

				}
			}

		}

		private class RuleBuilderRuleOp implements IAuthRuleBuilderRuleOp {

			private AppliesTypeEnum myAppliesTo;
			private Set<?> myAppliesToTypes;

			@Override
			public IAuthRuleBuilderRuleOpClassifier allResources() {
				myAppliesTo = AppliesTypeEnum.ALL_RESOURCES;
				return new RuleBuilderRuleOpClassifier();
			}

			@Override
			public IAuthRuleFinished instance(String theId) {
				Validate.notBlank(theId, "theId must not be null or empty");
				return instance(new IdDt(theId));
			}

			@Override
			public IAuthRuleFinished instance(IIdType theId) {
				Validate.notNull(theId, "theId must not be null");
				Validate.notBlank(theId.getValue(), "theId.getValue() must not be null or empty");
				Validate.notBlank(theId.getIdPart(), "theId must contain an ID part");

				return new RuleBuilderRuleOpClassifier(Collections.singletonList(theId)).finished();
			}

			@Override
			public IAuthRuleBuilderRuleOpClassifier resourcesOfType(Class<? extends IBaseResource> theType) {
				Validate.notNull(theType, "theType must not be null");
				myAppliesTo = AppliesTypeEnum.TYPES;
				myAppliesToTypes = Collections.singleton(theType);
				return new RuleBuilderRuleOpClassifier();
			}

			private class RuleBuilderRuleOpClassifier implements IAuthRuleBuilderRuleOpClassifier {

				private ClassifierTypeEnum myClassifierType;
				private String myInCompartmentName;
				private Collection<? extends IIdType> myInCompartmentOwners;
				private List<IIdType> myAppliesToInstances;

				/**
				 * Constructor
				 */
				RuleBuilderRuleOpClassifier() {
					super();
				}

				/**
				 * Constructor
				 */
				RuleBuilderRuleOpClassifier(List<IIdType> theAppliesToInstances) {
					myAppliesToInstances = theAppliesToInstances;
					myAppliesTo = AppliesTypeEnum.INSTANCES;
				}

				private IAuthRuleBuilderRuleOpClassifierFinished finished() {

					RuleImplOp rule = new RuleImplOp(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOp(myRuleOp);
					rule.setAppliesTo(myAppliesTo);
					rule.setAppliesToTypes(myAppliesToTypes);
					rule.setAppliesToInstances(myAppliesToInstances);
					rule.setClassifierType(myClassifierType);
					rule.setClassifierCompartmentName(myInCompartmentName);
					rule.setClassifierCompartmentOwners(myInCompartmentOwners);
					myRules.add(rule);

					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartment(String theCompartmentName, Collection<? extends IIdType> theOwners) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theOwners, "theOwners must not be null");
					Validate.noNullElements(theOwners, "theOwners must not contain any null elements");
					for (IIdType next : theOwners) {
						validateOwner(next);
					}
					myInCompartmentName = theCompartmentName;
					myInCompartmentOwners = theOwners;
					myClassifierType = ClassifierTypeEnum.IN_COMPARTMENT;
					return finished();
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartment(String theCompartmentName, IIdType theOwner) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theOwner, "theOwner must not be null");
					validateOwner(theOwner);
					myInCompartmentName = theCompartmentName;
					myInCompartmentOwners = Collections.singletonList(theOwner);
					myClassifierType = ClassifierTypeEnum.IN_COMPARTMENT;
					return finished();
				}

				private void validateOwner(IIdType theOwner) {
					Validate.notBlank(theOwner.getIdPart(), "owner.getIdPart() must not be null or empty");
					Validate.notBlank(theOwner.getIdPart(), "owner.getResourceType() must not be null or empty");
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished withAnyId() {
					myClassifierType = ClassifierTypeEnum.ANY_ID;
					return finished();
				}

			}

		}

		private class RuleBuilderRuleOperation implements IAuthRuleBuilderOperation {

			@Override
			public IAuthRuleBuilderOperationNamed named(String theOperationName) {
				Validate.notBlank(theOperationName, "theOperationName must not be null or empty");
				return new RuleBuilderRuleOperationNamed(theOperationName);
			}

			@Override
			public IAuthRuleBuilderOperationNamed withAnyName() {
				return new RuleBuilderRuleOperationNamed(null);
			}

			private class RuleBuilderRuleOperationNamed implements IAuthRuleBuilderOperationNamed {

				private String myOperationName;

				RuleBuilderRuleOperationNamed(String theOperationName) {
					if (theOperationName != null && !theOperationName.startsWith("$")) {
						myOperationName = '$' + theOperationName;
					} else {
						myOperationName = theOperationName;
					}
				}

				private OperationRule createRule() {
					OperationRule rule = new OperationRule(myRuleName);
					rule.setOperationName(myOperationName);
					rule.setMode(myRuleMode);
					return rule;
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onAnyInstance() {
					OperationRule rule = createRule();
					rule.appliesToAnyInstance();
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished atAnyLevel() {
					OperationRule rule = createRule();
					rule.appliesAtAnyLevel(true);
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onAnyType() {
					OperationRule rule = createRule();
					rule.appliesToAnyType();
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onInstance(IIdType theInstanceId) {
					Validate.notNull(theInstanceId, "theInstanceId must not be null");
					Validate.notBlank(theInstanceId.getResourceType(), "theInstanceId does not have a resource type");
					Validate.notBlank(theInstanceId.getIdPart(), "theInstanceId does not have an ID part");

					OperationRule rule = createRule();
					ArrayList<IIdType> ids = new ArrayList<>();
					ids.add(theInstanceId);
					rule.appliesToInstances(ids);
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onInstancesOfType(Class<? extends IBaseResource> theType) {
					validateType(theType);

					OperationRule rule = createRule();
					rule.appliesToInstancesOfType(toTypeSet(theType));
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onServer() {
					OperationRule rule = createRule();
					rule.appliesToServer();
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished onType(Class<? extends IBaseResource> theType) {
					validateType(theType);

					OperationRule rule = createRule();
					rule.appliesToTypes(toTypeSet(theType));
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

				private HashSet<Class<? extends IBaseResource>> toTypeSet(Class<? extends IBaseResource> theType) {
					HashSet<Class<? extends IBaseResource>> appliesToTypes = new HashSet<>();
					appliesToTypes.add(theType);
					return appliesToTypes;
				}

				private void validateType(Class<? extends IBaseResource> theType) {
					Validate.notNull(theType, "theType must not be null");
				}

			}

		}

		private class RuleBuilderRuleTransaction implements IAuthRuleBuilderRuleTransaction {

			@Override
			public IAuthRuleBuilderRuleTransactionOp withAnyOperation() {
				return new RuleBuilderRuleTransactionOp();
			}

			private class RuleBuilderRuleTransactionOp implements IAuthRuleBuilderRuleTransactionOp {

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished andApplyNormalRules() {
					RuleImplOp rule = new RuleImplOp(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOp(myRuleOp);
					rule.setTransactionAppliesToOp(TransactionAppliesToEnum.ANY_OPERATION);
					myRules.add(rule);
					return new RuleBuilderFinished(rule);
				}

			}

		}

		private class PatchBuilder implements IAuthRuleBuilderPatch {

			@Override
			public IAuthRuleFinished allRequests() {
				BaseRule rule = new RuleImplPatch(myRuleName)
					.setAllRequests(true)
					.setMode(myRuleMode);
				myRules.add(rule);
				return new RuleBuilderFinished(rule);
			}
		}
	}

}
