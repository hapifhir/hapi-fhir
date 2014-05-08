package ca.uhn.fhir.rest.gclient;


public class ReferenceParam implements IParam {

	private String myName;

	public ReferenceParam(String theName) {
		myName = theName;
	}

	@Override
	public String getParamName() {
		return myName;
	}
	
	public ICriterion hasChainedProperty(ICriterion theICriterion) {
		return new ReferenceChainCriterion(getParamName(), theICriterion);
	}

	/**
	 * Match the referenced resource if the resource has the given ID (this can be
	 * the logical ID or the absolute URL of the resource)
	 */
	public ICriterion hasId(String theId) {
		return new StringCriterion(getParamName(), theId);
	}

	private static class ReferenceChainCriterion implements ICriterion, ICriterionInternal {

		private ICriterionInternal myWrappedCriterion;
		private String myParamName;

		public ReferenceChainCriterion(String theParamName, ICriterion theWrappedCriterion) {
			myParamName = theParamName;
			myWrappedCriterion = (ICriterionInternal) theWrappedCriterion;
		}

		@Override
		public String getParameterValue() {
			return myWrappedCriterion.getParameterValue();
		}

		@Override
		public String getParameterName() {
			return myParamName + "." + myWrappedCriterion.getParameterName();
		}

	}

}
