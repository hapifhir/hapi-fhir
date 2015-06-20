package ca.uhn.fhir.context;

import junit.framework.TestCase;

import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ProvidesResources;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * Created by Bill de Beaubien on 12/10/2014.
 */
public class DuplicateExtensionTest extends TestCase {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DuplicateExtensionTest.class);
	
	@Test
	public void testScannerShouldAddProvidedResources() {
		FhirContext ctx = FhirContext.forDstu1();
		RuntimeResourceDefinition patientDef = ctx.getResourceDefinition(CustomPatient.class);
		Profile profile = (Profile) patientDef.toProfile("http://foo.org/fhir");

		String res = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(profile);
		ourLog.info(res);
		
		RuntimeResourceDefinition observationDef = ctx.getResourceDefinition(CustomObservation.class);
		profile = (Profile) observationDef.toProfile("http://foo.org/fhir");
	}

	@ResourceDef(name = "Observation", id = "CustomObservation")
	public static class CustomObservation extends Observation {
		@Child(name = "idExt", order = 0)
		@Extension(url = "http://foo.org#id", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "Contains the id of the resource")
		private StringDt myIdExt;

		public StringDt getIdExt() {
			if (myIdExt == null) {
				myIdExt = new StringDt();
			}
			return myIdExt;
		}

		public void setIdExt(StringDt theIdExt) {
			myIdExt = theIdExt;
		}
	}

	@ProvidesResources(resources = CustomObservation.class)
	public static class CustomObservationProvider {
	}

	@ResourceDef(name = "Patient", id = "CustomPatient")
	public static class CustomPatient extends Patient {
		@Child(name = "idExt", order = 0)
		@Extension(url = "http://foo.org#id", definedLocally = true, isModifier = false)
		@Description(shortDefinition = "Contains the id of the resource")
		private StringDt myIdExt;

		public StringDt getIdExt() {
			if (myIdExt == null) {
				myIdExt = new StringDt();
			}
			return myIdExt;
		}

		public void setIdExt(StringDt theIdExt) {
			myIdExt = theIdExt;
		}
	}

	@ProvidesResources(resources = CustomPatient.class)
	public static class CustomPatientProvider {
	}
}
