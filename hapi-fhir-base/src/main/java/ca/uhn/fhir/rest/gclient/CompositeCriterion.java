package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class CompositeCriterion<A extends IParam, B extends IParam> implements ICompositeWithLeft<B>, ICriterion<B>, ICriterionInternal {

	private ICriterion<B> myRight;
	private String myName;
	private ICriterion<A> myLeft;

	public CompositeCriterion(String theName, ICriterion<A> theLeft) {
		myName = theName;
		myLeft = theLeft;
	}

	@Override
	public ICriterion<B> withRight(ICriterion<B> theRight) {
		myRight = theRight;
		return this;
	}

	@Override
	public String getParameterValue() {
		ICriterionInternal left = (ICriterionInternal) myLeft;
		ICriterionInternal right = (ICriterionInternal) myRight;
		return defaultString(left.getParameterValue()) + '$' + defaultString(right.getParameterValue());
	}

	@Override
	public String getParameterName() {
		return myName;
	}

}
