package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;

abstract class BaseClientParam implements IParam {

	@Override
	public ICriterion<?> isMissing(boolean theMissing) {
		return new MissingCriterion(theMissing ? Constants.PARAMQUALIFIER_MISSING_TRUE : Constants.PARAMQUALIFIER_MISSING_FALSE);
	}

	private class MissingCriterion implements ICriterion<IParam>, ICriterionInternal
	{
		private String myParameterValue;


		public MissingCriterion(String theParameterValue) {
			myParameterValue = theParameterValue;
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			return myParameterValue;
		}

		@Override
		public String getParameterName() {
			return BaseClientParam.this.getParamName() + Constants.PARAMQUALIFIER_MISSING;
		}
		
	}
	
}
