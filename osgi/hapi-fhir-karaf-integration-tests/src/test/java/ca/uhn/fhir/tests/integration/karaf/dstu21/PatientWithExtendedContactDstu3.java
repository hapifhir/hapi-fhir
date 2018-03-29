package ca.uhn.fhir.tests.integration.karaf.dstu21;

import java.util.ArrayList;
import java.util.List;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu2016may.model.Identifier;
import org.hl7.fhir.dstu2016may.model.Patient;

@ResourceDef(name = "Patient")
public class PatientWithExtendedContactDstu3 extends Patient {

	private static final long serialVersionUID = 1L;

	/**
	 * A contact party (e.g. guardian, partner, friend) for the patient.
	 */
	@Child(name = "contact", type = {}, order = Child.REPLACE_PARENT, min = 0, max = Child.MAX_UNLIMITED, modifier = false, summary = false)
	@Description(shortDefinition = "A contact party (e.g. guardian, partner, friend) for the patient", formalDefinition = "A contact party (e.g. guardian, partner, friend) for the patient.")
	protected List<CustomContactComponent> customContact;

	public List<CustomContactComponent> getCustomContact() {
		if (customContact == null) {
			customContact = new ArrayList<CustomContactComponent>();
		}
		return customContact;
	}

	@Block()
	public static class CustomContactComponent extends ContactComponent {

		private static final long serialVersionUID = 1L;
		@Child(name = "contact-eyecolour", type = { Identifier.class }, modifier = true)
		@Description(shortDefinition = "Application ID")
		@Extension(url = "http://foo.com/contact-eyecolour", definedLocally = false, isModifier = false)
		private Identifier myEyeColour;

		/*
		 * Get messageHeaderApplicationId
		 */
		public Identifier getEyeColour() {
			if (myEyeColour == null) {
				myEyeColour = new Identifier();
			}
			return myEyeColour;
		}

		/*
		 * Set messageHeaderApplicationId
		 */
		public void setEyeColour(Identifier messageHeaderApplicationId) {
			this.myEyeColour = messageHeaderApplicationId;
		}

	}

}
