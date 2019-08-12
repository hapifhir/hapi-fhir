package ca.uhn.fhir.parser;

import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.Address.AddressUse;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "Patient")
public class MyPatientWithOneDeclaredEnumerationExtension extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(order = 0, name = "foo")
	@ca.uhn.fhir.model.api.annotation.Extension(url = "urn:foo", definedLocally = true, isModifier = false)
	private Enumeration<AddressUse> myFoo;

	public Enumeration<AddressUse> getFoo() {
		return myFoo;
	}

	public void setFoo(Enumeration<AddressUse> theFoo) {
		myFoo = theFoo;
	}

}
