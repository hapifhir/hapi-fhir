package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

public interface IFhirResourceDaoValueSet<T extends IBaseResource> extends IFhirResourceDao<T> {

	ValueSet expand(IIdType theId, StringDt theFilter);
	
	ValidateCodeResult validateCode(UriDt theValueSetIdentifier, IIdType theId, CodeDt theCode, UriDt theSystem, StringDt theDisplay, CodingDt theCoding, CodeableConceptDt theCodeableConcept);

	public class ValidateCodeResult {
		private String myDisplay;
		private String myMessage;
		private boolean myResult;

		public ValidateCodeResult(boolean theResult, String theMessage, String theDisplay) {
			super();
			myResult = theResult;
			myMessage = theMessage;
			myDisplay = theDisplay;
		}

		public String getDisplay() {
			return myDisplay;
		}

		public String getMessage() {
			return myMessage;
		}

		public boolean isResult() {
			return myResult;
		}
	}

}
