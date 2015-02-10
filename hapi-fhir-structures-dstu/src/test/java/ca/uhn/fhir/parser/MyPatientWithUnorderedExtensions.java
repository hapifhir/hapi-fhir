package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name = "Patient")
public class MyPatientWithUnorderedExtensions extends Patient {

	@Extension(url = "urn:ex1", definedLocally = false, isModifier = false)
	@Child(name = "extAtt")
	private BooleanDt myExtAtt1;

	@Extension(url = "urn:ex2", definedLocally = false, isModifier = false)
	@Child(name = "moreExt")
	private StringDt myExtAtt2;

	@Extension(url = "urn:ex3", definedLocally = false, isModifier = false)
	@Child(name = "modExt")
	private DateDt myExtAtt3;

	public BooleanDt getExtAtt1() {
		if (myExtAtt1 == null) {
			myExtAtt1 = new BooleanDt();
		}
		return myExtAtt1;
	}

	public StringDt getExtAtt2() {
		if (myExtAtt2 == null) {
			myExtAtt2 = new StringDt();
		}
		return myExtAtt2;
	}

	public DateDt getExtAtt3() {
		if (myExtAtt3 == null) {
			myExtAtt3 = new DateDt();
		}
		return myExtAtt3;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && ElementUtil.isEmpty(myExtAtt1, myExtAtt3, myExtAtt2);
	}

	public void setExtAtt1(BooleanDt theExtAtt1) {
		myExtAtt1 = theExtAtt1;
	}

	public void setExtAtt2(StringDt theExtAtt2) {
		myExtAtt2 = theExtAtt2;
	}

	public void setExtAtt3(DateDt theExtAtt3) {
		myExtAtt3 = theExtAtt3;
	}

}
