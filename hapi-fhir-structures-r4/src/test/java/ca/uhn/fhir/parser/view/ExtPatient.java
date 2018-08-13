package ca.uhn.fhir.parser.view;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;

@ResourceDef(name = "Patient")
public class ExtPatient extends Patient {

	@Extension(url = "urn:ext", isModifier = false, definedLocally = false)
	@Child(name = "ext")
	private IntegerType myExt;

	@Extension(url = "urn:modExt", isModifier = false, definedLocally = false)
	@Child(name = "modExt")
	private IntegerType myModExt;

	public IntegerType getExt() {
		if (myExt == null) {
			myExt = new IntegerType();
		}
		return myExt;
	}

	public void setExt(IntegerType theExt) {
		myExt = theExt;
	}

	public IntegerType getModExt() {
		if (myModExt == null) {
			myModExt = new IntegerType();
		}
		return myModExt;
	}

	public void setModExt(IntegerType theModExt) {
		myModExt = theModExt;
	}

}
