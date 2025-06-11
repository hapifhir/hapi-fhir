/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class RuleBuilder implements IAuthRuleBuilder {

	private static final ConcurrentHashMap<Class<? extends IBaseResource>, String> ourTypeToName =
			new ConcurrentHashMap<>();
	protected final List<IAuthRule> myRules = new ArrayList<>();
	private IAuthRuleBuilderRule myAllow;
	private IAuthRuleBuilderRule myDeny;

	protected void addRule(IAuthRule theRule) {
		Validate.notNull(theRule, "theRule must not be null");
		myRules.add(theRule);
	}

	protected boolean containsRule(IAuthRule theRule) {
		Validate.notNull(theRule, "theRule must not be null");
		return myRules.contains(theRule);
	}

	protected Optional<IAuthRule> findRule(Predicate<IAuthRule> thePredicate) {
		Validate.notNull(thePredicate, "thePredicate must not be null");
		return myRules.stream().filter(thePredicate).findFirst();
	}

	protected List<IAuthRule> getRules() {
		return myRules;
	}

	@Override
	public IAuthRuleBuilderRule allow() {
		if (myAllow == null) {
			myAllow = allow(null);
		}
		return myAllow;
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
		rule.setOp(RuleOpEnum.ALL);
		rule.setMode(PolicyEnum.ALLOW);
		addRule(rule);
		return new RuleBuilderFinished(rule);
	}

	@Override
	public List<IAuthRule> build() {
		return getRules();
	}

	@Override
	public IAuthRuleBuilderRule deny() {
		if (myDeny == null) {
			myDeny = deny(null);
		}
		return myDeny;
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
		rule.setOp(RuleOpEnum.ALL);
		rule.setMode(PolicyEnum.DENY);
		addRule(rule);
		return new RuleBuilderFinished(rule);
	}

	protected class RuleBuilderFinished
			implements IAuthRuleFinished,
					IAuthRuleBuilderRuleOpClassifierFinished,
					IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId {

		protected final BaseRule myOpRule;
		private List<IAuthRuleTester> myTesters;

		protected RuleBuilderFinished(BaseRule theRule) {
			assert theRule != null;
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
			return getRules();
		}

		/**
		 * Subclasses may override
		 */
		protected void doBuildRule() {
			// nothing
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId forTenantIds(String... theTenantIds) {
			return forTenantIds(Arrays.asList(defaultIfNull(theTenantIds, Constants.EMPTY_STRING_ARRAY)));
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId forTenantIds(
				final Collection<String> theTenantIds) {
			withTester(new TenantCheckingTester(theTenantIds, true));
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
			return notForTenantIds(Arrays.asList(defaultIfNull(theTenantIds, Constants.EMPTY_STRING_ARRAY)));
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinishedWithTenantId notForTenantIds(
				final Collection<String> theTenantIds) {
			withTester(new TenantCheckingTester(theTenantIds, false));
			return this;
		}

		@Override
		public IAuthRuleFinished withTester(IAuthRuleTester theTester) {
			if (theTester != null) {
				if (myTesters == null) {
					myTesters = new ArrayList<>();
				}
				myTesters.add(theTester);
				myOpRule.addTester(theTester);
			}

			return this;
		}

		@Override
		public IAuthRuleFinished withFilterTester(String theQueryParameters) {
			return withTester(new FhirQueryRuleTester(theQueryParameters));
		}

		protected class TenantCheckingTester implements IAuthRuleTester {
			protected final Collection<String> myTenantIds;
			protected final boolean myOutcome;

			protected TenantCheckingTester(Collection<String> theTenantIds, boolean theOutcome) {
				myTenantIds = theTenantIds;
				myOutcome = theOutcome;
			}

			@Override
			public boolean matches(
					RestOperationTypeEnum theOperation,
					RequestDetails theRequestDetails,
					IIdType theInputResourceId,
					IBaseResource theInputResource) {
				if (!myTenantIds.contains(theRequestDetails.getTenantId())) {
					return !myOutcome;
				}

				return matchesResource(theInputResource);
			}

			@Override
			public boolean matchesOutput(
					RestOperationTypeEnum theOperation,
					RequestDetails theRequestDetails,
					IBaseResource theOutputResource) {
				if (!myTenantIds.contains(theRequestDetails.getTenantId())) {
					return !myOutcome;
				}

				return matchesResource(theOutputResource);
			}

			protected boolean matchesResource(IBaseResource theResource) {
				if (theResource != null) {
					RequestPartitionId partitionId =
							(RequestPartitionId) theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
					if (partitionId != null) {
						if (partitionId.hasDefaultPartitionId()
								&& myTenantIds.contains(ProviderConstants.DEFAULT_PARTITION_NAME)) {
							return myOutcome;
						}

						String partitionNameOrNull = partitionId.getFirstPartitionNameOrNull();
						if (partitionNameOrNull == null || !myTenantIds.contains(partitionNameOrNull)) {
							return !myOutcome;
						}
					}
				}

				return myOutcome;
			}
		}
	}

	protected class RuleBuilderRule implements IAuthRuleBuilderRule {

		protected final PolicyEnum myRuleMode;
		protected final String myRuleName;
		private RuleBuilderRuleOp myReadRuleBuilder;
		private RuleBuilderRuleOp myWriteRuleBuilder;
		private RuleBuilderBulkExport ruleBuilderBulkExport;

		protected RuleBuilderRule(PolicyEnum theRuleMode, String theRuleName) {
			myRuleMode = theRuleMode;
			myRuleName = theRuleName;
		}

		@Override
		public IAuthRuleBuilderRuleConditional createConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.CREATE);
		}

		@Override
		public IAuthRuleBuilderRuleOpDelete delete() {
			return new RuleBuilderRuleOp(RuleOpEnum.DELETE);
		}

		@Override
		public IAuthRuleBuilderRuleConditional deleteConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.DELETE);
		}

		@Override
		public IAuthRuleBuilderRuleOpClassifierFinished metadata() {
			RuleImplOp rule = new RuleImplOp(myRuleName);
			rule.setOp(RuleOpEnum.METADATA);
			rule.setMode(myRuleMode);
			addRule(rule);
			return new RuleBuilderFinished(rule);
		}

		@Override
		public IAuthRuleBuilderOperation operation() {
			return new RuleBuilderRuleOperation();
		}

		@Override
		public IAuthRuleBuilderPatch patch() {
			return new PatchBuilder();
		}

		@Override
		public IAuthRuleBuilderRuleOp read() {
			if (myReadRuleBuilder == null) {
				myReadRuleBuilder = createReadRuleBuilder();
			}
			return myReadRuleBuilder;
		}

		protected RuleBuilderRuleOp createReadRuleBuilder() {
			return new RuleBuilderRuleOp(RuleOpEnum.READ);
		}

		@Override
		public IAuthRuleBuilderRuleTransaction transaction() {
			return new RuleBuilderRuleTransaction();
		}

		@Override
		public IAuthRuleBuilderRuleConditional updateConditional() {
			return new RuleBuilderRuleConditional(RestOperationTypeEnum.UPDATE);
		}

		@Override
		public IAuthRuleBuilderRuleOp write() {
			if (myWriteRuleBuilder == null) {
				myWriteRuleBuilder = createMyWriteRuleBuilder();
			}
			return myWriteRuleBuilder;
		}

		protected RuleBuilderRuleOp createMyWriteRuleBuilder() {
			return new RuleBuilderRuleOp(RuleOpEnum.WRITE);
		}

		@Override
		public IAuthRuleBuilderRuleOp create() {
			if (myWriteRuleBuilder == null) {
				myWriteRuleBuilder = createMyWriteRuleBuilder();
			}
			return myWriteRuleBuilder;
		}

		@Override
		public IAuthRuleBuilderGraphQL graphQL() {
			return new RuleBuilderGraphQL();
		}

		@Override
		public IAuthRuleBuilderRuleBulkExport bulkExport() {
			if (ruleBuilderBulkExport == null) {
				ruleBuilderBulkExport = createRuleBuilderBulkExport();
			}
			return ruleBuilderBulkExport;
		}

		protected RuleBuilderBulkExport createRuleBuilderBulkExport() {
			return new RuleBuilderBulkExport();
		}

		@Override
		public IAuthRuleBuilderUpdateHistoryRewrite updateHistoryRewrite() {
			return new UpdateHistoryRewriteBuilder();
		}

		protected class RuleBuilderRuleConditional implements IAuthRuleBuilderRuleConditional {

			private AppliesTypeEnum myAppliesTo;
			private Set<String> myAppliesToTypes;
			protected final RestOperationTypeEnum myOperationType;

			protected RuleBuilderRuleConditional(RestOperationTypeEnum theOperationType) {
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

				String typeName = toTypeName(theType);
				return resourcesOfType(typeName);
			}

			@Override
			public IAuthRuleBuilderRuleConditionalClassifier resourcesOfType(String theType) {
				myAppliesTo = AppliesTypeEnum.TYPES;
				myAppliesToTypes = Collections.singleton(theType);
				return new RuleBuilderRuleConditionalClassifier();
			}

			protected class RuleBuilderRuleConditionalClassifier extends RuleBuilderFinished
					implements IAuthRuleBuilderRuleConditionalClassifier {

				protected RuleBuilderRuleConditionalClassifier() {
					super(new RuleImplConditional(myRuleName));
				}

				@Override
				protected void doBuildRule() {
					RuleImplConditional rule = (RuleImplConditional) myOpRule;
					rule.setMode(myRuleMode);
					rule.setOperationType(myOperationType);
					rule.setAppliesTo(myAppliesTo);
					rule.setAppliesToTypes(myAppliesToTypes);
					rule.addTesters(getTesters());
					addRule(rule);
				}
			}
		}

		protected class RuleBuilderRuleOp implements IAuthRuleBuilderRuleOp, IAuthRuleBuilderRuleOpDelete {

			protected final RuleOpEnum myRuleOp;
			private RuleBuilderRuleOpClassifier myInstancesBuilder;
			private boolean myOnCascade;
			private boolean myOnExpunge;

			protected RuleBuilderRuleOp(RuleOpEnum theRuleOp) {
				myRuleOp = theRuleOp;
			}

			@Override
			public IAuthRuleBuilderRuleOpClassifier allResources() {
				return new RuleBuilderRuleOpClassifier(AppliesTypeEnum.ALL_RESOURCES, null);
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

				List<IIdType> instances = Lists.newArrayList(theId);
				return instances(instances);
			}

			@Override
			public IAuthRuleFinished instances(Collection<IIdType> theInstances) {
				Validate.notNull(theInstances, "theInstances must not be null");
				Validate.notEmpty(theInstances, "theInstances must not be empty");

				if (myInstancesBuilder == null) {
					RuleBuilderRuleOpClassifier instancesBuilder = new RuleBuilderRuleOpClassifier(theInstances);
					myInstancesBuilder = instancesBuilder;
					return instancesBuilder.finished();
				} else {
					return myInstancesBuilder.addInstances(theInstances);
				}
			}

			@Override
			public IAuthRuleBuilderRuleOpClassifier resourcesOfType(Class<? extends IBaseResource> theType) {
				Validate.notNull(theType, "theType must not be null");
				String resourceName = toTypeName(theType);
				return resourcesOfType(resourceName);
			}

			@Override
			public IAuthRuleBuilderRuleOpClassifier resourcesOfType(String theType) {
				Validate.notNull(theType, "theType must not be null");
				return new RuleBuilderRuleOpClassifier(AppliesTypeEnum.TYPES, Collections.singleton(theType));
			}

			@Override
			public IAuthRuleBuilderRuleOp onCascade() {
				myOnCascade = true;
				return this;
			}

			@Override
			public IAuthRuleBuilderRuleOp onExpunge() {
				myOnExpunge = true;
				return this;
			}

			protected class RuleBuilderRuleOpClassifier implements IAuthRuleBuilderRuleOpClassifier {

				protected final AppliesTypeEnum myAppliesTo;
				protected final Set<String> myAppliesToTypes;
				private ClassifierTypeEnum myClassifierType;
				private String myInCompartmentName;
				private Collection<? extends IIdType> myInCompartmentOwners;
				private Collection<IIdType> myAppliesToInstances;
				private RuleImplOp myRule;
				private AdditionalCompartmentSearchParameters myAdditionalSearchParamsForCompartmentTypes =
						new AdditionalCompartmentSearchParameters();

				protected RuleBuilderRuleOpClassifier(AppliesTypeEnum theAppliesTo, Set<String> theAppliesToTypes) {
					myAppliesTo = theAppliesTo;
					myAppliesToTypes = theAppliesToTypes;
				}

				protected RuleBuilderRuleOpClassifier(Collection<IIdType> theAppliesToInstances) {
					this(AppliesTypeEnum.INSTANCES, null);

					myAppliesToInstances = theAppliesToInstances;
				}

				protected IAuthRuleBuilderRuleOpClassifierFinished finished() {
					return finished(new RuleImplOp(myRuleName));
				}

				protected IAuthRuleBuilderRuleOpClassifierFinished finished(RuleImplOp theRule) {
					Validate.isTrue(myRule == null, "Can not call finished() twice");
					myRule = theRule;
					theRule.setMode(myRuleMode);
					theRule.setOp(myRuleOp);
					theRule.setAppliesTo(myAppliesTo);
					theRule.setAppliesToTypes(myAppliesToTypes);
					theRule.setAppliesToInstances(myAppliesToInstances);
					theRule.setClassifierType(myClassifierType);
					theRule.setClassifierCompartmentName(myInCompartmentName);
					theRule.setClassifierCompartmentOwners(myInCompartmentOwners);
					theRule.setAppliesToDeleteCascade(myOnCascade);
					theRule.setAppliesToDeleteExpunge(myOnExpunge);
					theRule.setAdditionalSearchParamsForCompartmentTypes(myAdditionalSearchParamsForCompartmentTypes);
					addRule(theRule);

					return new RuleBuilderFinished(theRule);
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartment(
						String theCompartmentName, Collection<? extends IIdType> theOwners) {
					return inCompartmentWithAdditionalSearchParams(
							theCompartmentName, theOwners, new AdditionalCompartmentSearchParameters());
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartmentWithAdditionalSearchParams(
						String theCompartmentName,
						Collection<? extends IIdType> theOwners,
						AdditionalCompartmentSearchParameters theAdditionalTypeSearchParams) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theOwners, "theOwners must not be null");
					Validate.noNullElements(theOwners, "theOwners must not contain any null elements");
					for (IIdType next : theOwners) {
						validateOwner(next);
					}
					myInCompartmentName = theCompartmentName;
					myInCompartmentOwners = theOwners;
					myAdditionalSearchParamsForCompartmentTypes = theAdditionalTypeSearchParams;
					myClassifierType = ClassifierTypeEnum.IN_COMPARTMENT;
					return finished();
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartment(
						String theCompartmentName, IIdType theOwner) {
					return inCompartmentWithAdditionalSearchParams(
							theCompartmentName, theOwner, new AdditionalCompartmentSearchParameters());
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartmentWithAdditionalSearchParams(
						String theCompartmentName,
						IIdType theOwner,
						AdditionalCompartmentSearchParameters theAdditionalTypeSearchParamNames) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theOwner, "theOwner must not be null");
					validateOwner(theOwner);
					myClassifierType = ClassifierTypeEnum.IN_COMPARTMENT;
					myInCompartmentName = theCompartmentName;
					myAdditionalSearchParamsForCompartmentTypes = theAdditionalTypeSearchParamNames;
					Optional<RuleImplOp> oRule = findMatchingRule();
					if (oRule.isPresent()) {
						RuleImplOp rule = oRule.get();
						rule.setAdditionalSearchParamsForCompartmentTypes(myAdditionalSearchParamsForCompartmentTypes);
						rule.addClassifierCompartmentOwner(theOwner);
						return new RuleBuilderFinished(rule);
					}
					myInCompartmentOwners = Collections.singletonList(theOwner);
					return finished();
				}

				private Optional<RuleImplOp> findMatchingRule() {
					return findRule(rule -> rule instanceof RuleImplOp
									&& ((RuleImplOp) rule)
											.matches(
													myRuleOp,
													myAppliesTo,
													myAppliesToInstances,
													myAppliesToTypes,
													myClassifierType,
													myInCompartmentName))
							.map(RuleImplOp.class::cast);
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

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished withCodeInValueSet(
						@Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl) {
					SearchParameterAndValueSetRuleImpl rule = new SearchParameterAndValueSetRuleImpl(myRuleName);
					rule.setSearchParameterName(theSearchParameterName);
					rule.setValueSetUrl(theValueSetUrl);
					rule.setWantCode(true);
					return finished(rule);
				}

				@Override
				public IAuthRuleFinished withCodeNotInValueSet(
						@Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl) {
					SearchParameterAndValueSetRuleImpl rule = new SearchParameterAndValueSetRuleImpl(myRuleName);
					rule.setSearchParameterName(theSearchParameterName);
					rule.setValueSetUrl(theValueSetUrl);
					rule.setWantCode(false);
					return finished(rule);
				}

				@Override
				public IAuthRuleFinished inCompartmentWithFilter(
						String theCompartmentName, IIdType theIdElement, String theFilter) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theIdElement, "theOwner must not be null");
					validateOwner(theIdElement);

					// inlined from inCompartmentWithAdditionalSearchParams()
					myClassifierType = ClassifierTypeEnum.IN_COMPARTMENT;
					myInCompartmentName = theCompartmentName;
					myAdditionalSearchParamsForCompartmentTypes = new AdditionalCompartmentSearchParameters();
					// todo JR/MB this is a quick and dirty fix at the last minute before the release.
					//  We should revisit approach so that findMatchingRule() takes the filters into account
					//  and only merges the rules if the filters are compatible
					//					Optional<RuleImplOp> oRule = findMatchingRule();
					//					if (oRule.isPresent()) {
					//						RuleImplOp rule = oRule.get();
					//
					//	rule.setAdditionalSearchParamsForCompartmentTypes(myAdditionalSearchParamsForCompartmentTypes);
					//						rule.addClassifierCompartmentOwner(theIdElement);
					//						return new RuleBuilderFinished(rule);
					//					}
					myInCompartmentOwners = Collections.singletonList(theIdElement);

					return finished().withTester(new FhirQueryRuleTester(theFilter));
				}

				@Override
				public IAuthRuleFinished withFilter(String theFilter) {
					myClassifierType = ClassifierTypeEnum.ANY_ID;

					return finished().withTester(new FhirQueryRuleTester(theFilter));
				}

				protected RuleBuilderFinished addInstances(Collection<IIdType> theInstances) {
					myAppliesToInstances.addAll(theInstances);
					return new RuleBuilderFinished(myRule);
				}
			}
		}

		protected class RuleBuilderRuleOperation implements IAuthRuleBuilderOperation {

			@Override
			public IAuthRuleBuilderOperationNamed named(String theOperationName) {
				Validate.notBlank(theOperationName, "theOperationName must not be null or empty");
				return new RuleBuilderRuleOperationNamed(theOperationName);
			}

			@Override
			public IAuthRuleBuilderOperationNamed withAnyName() {
				return new RuleBuilderRuleOperationNamed(null);
			}

			protected class RuleBuilderRuleOperationNamed implements IAuthRuleBuilderOperationNamed {

				protected final String myOperationName;

				protected RuleBuilderRuleOperationNamed(String theOperationName) {
					if (theOperationName != null && !theOperationName.startsWith("$")) {
						myOperationName = '$' + theOperationName;
					} else {
						myOperationName = theOperationName;
					}
				}

				protected OperationRule createRule() {
					OperationRule rule = new OperationRule(myRuleName);
					rule.setOperationName(myOperationName);
					rule.setMode(myRuleMode);
					return rule;
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onAnyInstance() {
					OperationRule rule = createRule();
					rule.appliesToAnyInstance();
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped atAnyLevel() {
					OperationRule rule = createRule();
					rule.appliesAtAnyLevel(true);
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onAnyType() {
					OperationRule rule = createRule();
					rule.appliesToAnyType();
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onInstance(IIdType theInstanceId) {
					Validate.notNull(theInstanceId, "theInstanceId must not be null");
					Validate.notBlank(theInstanceId.getResourceType(), "theInstanceId does not have a resource type");
					Validate.notBlank(theInstanceId.getIdPart(), "theInstanceId does not have an ID part");

					OperationRule rule = createRule();
					ArrayList<IIdType> ids = new ArrayList<>();
					ids.add(theInstanceId);
					rule.appliesToInstances(ids);
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onInstances(Collection<IIdType> theInstanceIds) {
					Validate.notNull(theInstanceIds, "theInstanceIds must not be null");
					theInstanceIds.forEach(instanceId -> Validate.notBlank(
							instanceId.getResourceType(),
							"at least one of theInstanceIds does not have a resource type"));
					theInstanceIds.forEach(instanceId -> Validate.notBlank(
							instanceId.getIdPart(), "at least one of theInstanceIds does not have an ID part"));

					final OperationRule rule = createRule();
					rule.appliesToInstances(new ArrayList<>(theInstanceIds));
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onInstancesOfType(
						Class<? extends IBaseResource> theType) {
					validateType(theType);

					OperationRule rule = createRule();
					rule.appliesToInstancesOfType(toTypeSet(theType));
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onServer() {
					OperationRule rule = createRule();
					rule.appliesToServer();
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				@Override
				public IAuthRuleBuilderOperationNamedAndScoped onType(Class<? extends IBaseResource> theType) {
					validateType(theType);

					OperationRule rule = createRule();
					rule.appliesToTypes(toTypeSet(theType));
					return new RuleBuilderOperationNamedAndScoped(rule);
				}

				private HashSet<Class<? extends IBaseResource>> toTypeSet(Class<? extends IBaseResource> theType) {
					HashSet<Class<? extends IBaseResource>> appliesToTypes = new HashSet<>();
					appliesToTypes.add(theType);
					return appliesToTypes;
				}

				private void validateType(Class<? extends IBaseResource> theType) {
					Validate.notNull(theType, "theType must not be null");
				}

				protected class RuleBuilderOperationNamedAndScoped implements IAuthRuleBuilderOperationNamedAndScoped {

					protected final OperationRule myRule;

					protected RuleBuilderOperationNamedAndScoped(OperationRule theRule) {
						myRule = theRule;
					}

					@Override
					public IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponses() {
						myRule.allowAllResponses();
						addRule(myRule);
						return new RuleBuilderFinished(myRule);
					}

					@Override
					public IAuthRuleBuilderRuleOpClassifierFinished andAllowAllResponsesWithAllResourcesAccess() {
						myRule.allowAllResponses();
						myRule.allowAllResourcesAccess();
						addRule(myRule);
						return new RuleBuilderFinished(myRule);
					}

					@Override
					public IAuthRuleBuilderRuleOpClassifierFinished andRequireExplicitResponseAuthorization() {
						addRule(myRule);
						return new RuleBuilderFinished(myRule);
					}
				}
			}
		}

		protected class RuleBuilderRuleTransaction implements IAuthRuleBuilderRuleTransaction {

			@Override
			public IAuthRuleBuilderRuleTransactionOp withAnyOperation() {
				return new RuleBuilderRuleTransactionOp();
			}

			protected class RuleBuilderRuleTransactionOp implements IAuthRuleBuilderRuleTransactionOp {

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished andApplyNormalRules() {
					// Allow transaction
					RuleImplOp rule = new RuleImplOp(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOp(RuleOpEnum.TRANSACTION);
					rule.setTransactionAppliesToOp(TransactionAppliesToEnum.ANY_OPERATION);
					addRule(rule);
					return new RuleBuilderFinished(rule);
				}
			}
		}

		protected class PatchBuilder implements IAuthRuleBuilderPatch {

			protected PatchBuilder() {}

			@Override
			public IAuthRuleFinished allRequests() {
				BaseRule rule =
						new RuleImplPatch(myRuleName).setAllRequests(true).setMode(myRuleMode);
				addRule(rule);
				return new RuleBuilderFinished(rule);
			}
		}

		protected class UpdateHistoryRewriteBuilder implements IAuthRuleBuilderUpdateHistoryRewrite {

			protected UpdateHistoryRewriteBuilder() {}

			@Override
			public IAuthRuleFinished allRequests() {
				BaseRule rule = new RuleImplUpdateHistoryRewrite(myRuleName)
						.setAllRequests(true)
						.setMode(myRuleMode);
				addRule(rule);
				return new RuleBuilderFinished(rule);
			}
		}

		protected class RuleBuilderGraphQL implements IAuthRuleBuilderGraphQL {
			@Override
			public IAuthRuleFinished any() {
				RuleImplOp rule = new RuleImplOp(myRuleName);
				rule.setOp(RuleOpEnum.GRAPHQL);
				rule.setMode(myRuleMode);
				addRule(rule);
				return new RuleBuilderFinished(rule);
			}
		}

		protected class RuleBuilderBulkExport implements IAuthRuleBuilderRuleBulkExport {
			private RuleBulkExportImpl myRuleBulkExport;

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget groupExportOnGroup(@Nonnull String theFocusResourceId) {
				RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
				rule.setAppliesToGroupExportOnGroup(theFocusResourceId);
				rule.setMode(myRuleMode);
				addRule(rule);

				return new RuleBuilderBulkExportWithTarget(rule);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnAllPatients() {
				if (myRuleBulkExport == null) {
					RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
					rule.setMode(myRuleMode);
					myRuleBulkExport = rule;
				}
				myRuleBulkExport.setAppliesToPatientExportAllPatients();

				// prevent duplicate rules being added
				if (!containsRule(myRuleBulkExport)) {
					addRule(myRuleBulkExport);
				}

				return new RuleBuilderBulkExportWithTarget(myRuleBulkExport);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatient(@Nonnull String theFocusResourceId) {
				if (myRuleBulkExport == null) {
					RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
					rule.setAppliesToPatientExport(theFocusResourceId);
					rule.setMode(myRuleMode);
					myRuleBulkExport = rule;
				} else {
					myRuleBulkExport.setAppliesToPatientExport(theFocusResourceId);
				}

				// prevent duplicate rules being added
				if (!containsRule(myRuleBulkExport)) {
					addRule(myRuleBulkExport);
				}

				return new RuleBuilderBulkExportWithTarget(myRuleBulkExport);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnPatientStrings(
					@Nonnull Collection<String> theFocusResourceIds) {
				if (myRuleBulkExport == null) {
					RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
					rule.setAppliesToPatientExport(theFocusResourceIds);
					rule.setMode(myRuleMode);
					myRuleBulkExport = rule;
				} else {
					myRuleBulkExport.setAppliesToPatientExport(theFocusResourceIds);
				}

				// prevent duplicate rules being added
				if (!containsRule(myRuleBulkExport)) {
					addRule(myRuleBulkExport);
				}

				return new RuleBuilderBulkExportWithTarget(myRuleBulkExport);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget patientExportOnGroup(@Nonnull String theFocusResourceId) {
				RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
				rule.setAppliesToPatientExportOnGroup(theFocusResourceId);
				rule.setMode(myRuleMode);
				addRule(rule);

				return new RuleBuilderBulkExportWithTarget(rule);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget systemExport() {
				RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
				rule.setAppliesToSystem();
				rule.setMode(myRuleMode);
				addRule(rule);

				return new RuleBuilderBulkExportWithTarget(rule);
			}

			@Override
			public IAuthRuleBuilderRuleBulkExportWithTarget any() {
				RuleBulkExportImpl rule = new RuleBulkExportImpl(myRuleName);
				rule.setAppliesToAny();
				rule.setMode(myRuleMode);
				addRule(rule);

				return new RuleBuilderBulkExportWithTarget(rule);
			}

			protected class RuleBuilderBulkExportWithTarget extends RuleBuilderFinished
					implements IAuthRuleBuilderRuleBulkExportWithTarget {
				protected final RuleBulkExportImpl myRule;

				protected RuleBuilderBulkExportWithTarget(RuleBulkExportImpl theRule) {
					super(theRule);
					myRule = theRule;
				}

				@Override
				public IAuthRuleBuilderRuleBulkExportWithTarget withResourceTypes(Collection<String> theResourceTypes) {
					myRule.setResourceTypes(theResourceTypes);
					return this;
				}
			}
		}
	}

	protected static String toTypeName(Class<? extends IBaseResource> theType) {
		String retVal = ourTypeToName.get(theType);
		if (retVal == null) {
			ResourceDef resourceDef = theType.getAnnotation(ResourceDef.class);
			retVal = resourceDef.name();
			Validate.notBlank(retVal, "Could not determine resource type of class %s", theType);
			ourTypeToName.put(theType, retVal);
		}
		return retVal;
	}
}
