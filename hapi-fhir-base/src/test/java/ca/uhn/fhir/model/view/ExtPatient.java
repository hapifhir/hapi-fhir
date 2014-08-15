package ca.uhn.fhir.model.view;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IntegerDt;

@ResourceDef(name = "Patient")
public class ExtPatient extends Patient {

	@Extension(url = "urn:ext", isModifier = false, definedLocally = false)
	@Child(name = "ext")
	private IntegerDt myExt;

	@Extension(url = "urn:modExt", isModifier = false, definedLocally = false)
	@Child(name = "modExt")
	private IntegerDt myModExt;

	public IntegerDt getExt() {
		if (myExt == null) {
			myExt = new IntegerDt();
		}
		return myExt;
	}

	public void setExt(IntegerDt theExt) {
		myExt = theExt;
	}

	public IntegerDt getModExt() {
		if (myModExt == null) {
			myModExt = new IntegerDt();
		}
		return myModExt;
	}

	public void setModExt(IntegerDt theModExt) {
		myModExt = theModExt;
	}

}
