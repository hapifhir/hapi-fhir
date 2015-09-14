package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class FhirResourceDaoDstu2ValidateTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2ValidateTest.class);

	@Test
	public void testValidateResourceContainingProfileDeclaration() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclaration";

		Bundle vss = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/instance/model/valueset/valuesets.xml");
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-status"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-category"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-codes"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-methods"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-valueabsentreason"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-interpretation"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "body-site"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "referencerange-meaning"));
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-relationshiptypes"));

		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/org/hl7/fhir/instance/model/profile/devicemetricobservation.profile.xml");
		sd.setId(new IdDt());
		sd.setUrl("http://example.com/foo/bar/" + methodName);
		myStructureDefinitionDao.create(sd);

		Observation input = new Observation();
		ResourceMetadataKeyEnum.PROFILES.put(input, Arrays.asList(new IdDt(sd.getUrl())));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatusEnum.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		String encoded = myFhirCtx.newJsonParser().encodeResourceToString(input);
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		MethodOutcome outcome = myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null);

		String ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(ooString);
		assertThat(ooString, containsString("Element '/f:Observation.subject': minimum required = 1, but only found 0"));
		assertThat(ooString, containsString("Element encounter @ /f:Observation: max allowed = 0, but found 1"));
		assertThat(ooString, containsString("Element '/f:Observation.device': minimum required = 1, but only found 0"));

	}

	private IResource findResourceByIdInBundle(Bundle vss, String name) {
		IResource retVal = null;
		for (Entry next : vss.getEntry()) {
			if (next.getResource().getId().getIdPart().equals(name)) {
				retVal = next.getResource();
				break;
			}
		}
		if (retVal == null) {
			fail("Can't find VS: " + name);
		}
		return retVal;
	}

}
