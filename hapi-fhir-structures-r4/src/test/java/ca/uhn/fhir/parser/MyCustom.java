package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.UuidType;

@ResourceDef(name = "Basic", profile = "http://mycustom.url")
public class MyCustom extends Basic {
	public static final String PROFILE = "http://mycustom.url";

	@Child(name = "myValue")
	@Extension(url = "http://myValue.url", definedLocally = false, isModifier = false)
	@Description(shortDefinition = "")
	private UuidType myValue;

	public MyCustom() {
		super();
	}

	public UuidType getValue() {
		if (myValue == null) {
			myValue = new UuidType();
		}
		return myValue;
	}
}
