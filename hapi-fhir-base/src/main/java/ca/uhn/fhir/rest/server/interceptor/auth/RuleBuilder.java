package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.ArrayList;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class RuleBuilder implements IAuthRuleBuilder {

	private ArrayList<IAuthRule> myRules;

	public RuleBuilder() {
		myRules = new ArrayList<IAuthRule>();
	}
	
	@Override
	public IAuthRuleBuilderRule allow() {
		return allow(null);
	}

	@Override
	public IAuthRuleBuilderRule allow(String theRuleName) {
		return new RuleBuilderRule(RuleVerdictEnum.ALLOW, theRuleName);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll() {
		return allowAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll(String theRuleName) {
		myRules.add(new Rule(theRuleName).setOp(RuleOpEnum.ALLOW_ALL));
		return new RuleBuilderFinished();
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
		return new RuleBuilderRule(RuleVerdictEnum.DENY, theRuleName);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll() {
		return denyAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll(String theRuleName) {
		myRules.add(new Rule(theRuleName).setOp(RuleOpEnum.DENY_ALL));
		return new RuleBuilderFinished();
	}

	private final class RuleBuilderFinished implements IAuthRuleBuilderRuleOpClassifierFinished {

		@Override
		public IAuthRuleBuilder andThen() {
			return RuleBuilder.this;
		}

		@Override
		public List<IAuthRule> build() {
			return myRules;
		}
	}

	private class RuleBuilderRule implements IAuthRuleBuilderRule {

		private RuleOpEnum myRuleOp;
		private RuleVerdictEnum myRuleMode;
		private String myRuleName;

		public RuleBuilderRule(RuleVerdictEnum theRuleMode, String theRuleName) {
			myRuleMode = theRuleMode;
			myRuleName = theRuleName;
		}

		@Override
		public RuleBuilderFinished metadata() {
			Rule rule = new Rule(myRuleName);
			rule.setOp(RuleOpEnum.METADATA);
			rule.setMode(myRuleMode);
			myRules.add(rule);
			return new RuleBuilderFinished();
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
		public IAuthRuleBuilderRuleOp write() {
			myRuleOp = RuleOpEnum.WRITE;
			return new RuleBuilderRuleOp();
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

				private IAuthRuleBuilderRuleOpClassifierFinished finished() {
					
					Rule rule = new Rule(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOp(myRuleOp);
					rule.setAppliesTo(myAppliesTo);
					rule.setAppliesToTypes(myAppliesToTypes);
					rule.setClassifierType(myClassifierType);
					rule.setClassifierCompartmentName(myInCompartmentName);
					rule.setClassifierCompartmentOwners(myInCompartmentOwners);
					myRules.add(rule);
					
					return new RuleBuilderFinished();
				}

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished inCompartment(String theCompartmentName, Collection<? extends IIdType> theOwners) {
					Validate.notBlank(theCompartmentName, "theCompartmentName must not be null");
					Validate.notNull(theOwners, "theOwners must not be null");
					Validate.noNullElements(theOwners, "theOwners must not contain any null elements");
					for (IIdType next : theOwners) {
						validateOwner(next);
					}
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

		private class RuleBuilderRuleTransaction implements IAuthRuleBuilderRuleTransaction {

			@Override
			public IAuthRuleBuilderRuleTransactionOp withAnyOperation() {
				return new RuleBuilderRuleTransactionOp();
			}
			private class RuleBuilderRuleTransactionOp implements IAuthRuleBuilderRuleTransactionOp {

				@Override
				public IAuthRuleBuilderRuleOpClassifierFinished andApplyNormalRules() {
					Rule rule = new Rule(myRuleName);
					rule.setMode(myRuleMode);
					rule.setOp(myRuleOp);
					rule.setTransactionAppliesToOp(TransactionAppliesToEnum.ANY_OPERATION);
					myRules.add(rule);
					return new RuleBuilderFinished();
				}
				
			}
			
		}

	}

}
