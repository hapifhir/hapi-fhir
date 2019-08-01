package ca.uhn.fhir.tests.integration.karaf.dstu2hl7org;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu2.model.Address.AddressUse;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Patient;

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
