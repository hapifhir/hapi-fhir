package ca.uhn.fhir.rest.server.interceptor.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

class RuleBuilder implements IAuthRuleBuilder {

	private RuleModeEnum myRuleMode;

	private String myRuleName;

	private List<IAuthRule> myRules;

	public RuleBuilder(List<IAuthRule> theRules) {
		myRules = theRules;
	}

	@Override
	public IAuthRuleBuilderRule allow() {
		return allow(null);
	}

	@Override
	public IAuthRuleBuilderRule allow(String theRuleName) {
		myRuleMode = RuleModeEnum.ALLOW;
		myRuleName = theRuleName;
		return new RuleBuilderRule();
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll() {
		return allowAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished allowAll(String theRuleName) {
		myRules.add(new Rule(theRuleName).setOp(RuleOpEnum.ALLOW_ALL));
		return new RuleBuilderFinished(myRules);
	}

	@Override
	public IAuthRuleBuilderRule deny() {
		return deny(null);
	}

	@Override
	public IAuthRuleBuilderRule deny(String theRuleName) {
		myRuleMode = RuleModeEnum.DENY;
		myRuleName = theRuleName;
		return new RuleBuilderRule();
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll() {
		return denyAll(null);
	}

	@Override
	public IAuthRuleBuilderRuleOpClassifierFinished denyAll(String theRuleName) {
		myRules.add(new Rule(theRuleName).setOp(RuleOpEnum.DENY_ALL));
		return new RuleBuilderFinished(myRules);
	}

	private static final class RuleBuilderFinished implements IAuthRuleBuilderRuleOpClassifierFinished {
		private List<IAuthRule> myRules;

		public RuleBuilderFinished(List<IAuthRule> theRules) {
			myRules = theRules;
		}

		@Override
		public IAuthRuleBuilder andThen() {
			return new RuleBuilder(myRules);
		}
	}

	class RuleBuilderRule implements IAuthRuleBuilderRule {

		private RuleOpEnum myRuleOp;

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
		
		public class RuleBuilderRuleOp implements IAuthRuleBuilderRuleOp {

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

			public class RuleBuilderRuleOpClassifier implements IAuthRuleBuilderRuleOpClassifier {

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
					
					return new RuleBuilderFinished(myRules);
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
		
		public class RuleBuilderRuleTransaction implements IAuthRuleBuilderRuleTransaction {

			@Override
			public IAuthRuleBuilderRuleOpClassifierFinished withAnyOperation() {
				Rule rule = new Rule(myRuleName);
				rule.setOp(myRuleOp);
				rule.setTransactionAppliesToOp(TransactionAppliesToEnum.ANY_OPERATION);
				myRules.add(rule);
				return new RuleBuilderFinished(myRules);
			}
		}

	}

}
