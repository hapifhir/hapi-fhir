package ca.uhn.fhir.jpa.dao.r4;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FhirResourceDaoR4ValidateTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValidateTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@Test
	public void testValidateStructureDefinition() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/r4/sd-david-dhtest7.json"), StandardCharsets.UTF_8);
		StructureDefinition sd = myFhirCtx.newJsonParser().parseResource(StructureDefinition.class, input);
		
		
		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		ourLog.info("Done validation");
		
		StopWatch sw = new StopWatch();
		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			// ok
		}
		ourLog.info("Done validation in {}ms", sw.getMillis());

	}
	
	@Test
	public void testValidateDocument() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/r4/document-bundle.json"), StandardCharsets.UTF_8);
		Bundle document = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		
		
		ourLog.info("Starting validation");
		try {
			MethodOutcome outcome = myBundleDao.validate(document, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		ourLog.info("Done validation");
		
//		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome()));
	}

	@Test
	@Ignore
	public void testValidateResourceContainingProfileDeclarationJson() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationJson";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.JSON);

		String ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Element '.subject': minimum required = 1, but only found 0"));
		assertThat(ooString, containsString("Element encounter @ : max allowed = 0, but found 1"));
		assertThat(ooString, containsString("Element '.device': minimum required = 1, but only found 0"));
	}

	@Test
	@Ignore
	public void testValidateResourceContainingProfileDeclarationXml() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationXml";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.XML);

		String ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Element '/f:Observation.subject': minimum required = 1, but only found 0"));
		assertThat(ooString, containsString("Element encounter @ /f:Observation: max allowed = 0, but found 1"));
		assertThat(ooString, containsString("Element '/f:Observation.device': minimum required = 1, but only found 0"));
	}

	private OperationOutcome doTestValidateResourceContainingProfileDeclaration(String methodName, EncodingEnum enc) throws IOException {
		Bundle vss = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/r4/model/valueset/valuesets.xml");
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-status"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-category"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-codes"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-methods"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-valueabsentreason"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-interpretation"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "body-site"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "referencerange-meaning"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-relationshiptypes"), mySrd);

		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/org/hl7/fhir/r4/model/profile/devicemetricobservation.profile.xml");
		sd.setId(new IdType());
		sd.setUrl("http://example.com/foo/bar/" + methodName);
		myStructureDefinitionDao.create(sd, mySrd);

		Observation input = new Observation();
		input.getMeta().getProfile().add(new CanonicalType(sd.getUrl()));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		String encoded = null;
		MethodOutcome outcome = null;
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		switch (enc) {
		case JSON:
			encoded = myFhirCtx.newJsonParser().encodeResourceToString(input);
			try {
				myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
				fail();
			} catch (PreconditionFailedException e) {
				return (OperationOutcome) e.getOperationOutcome();
			}
			break;
		case XML:
			encoded = myFhirCtx.newXmlParser().encodeResourceToString(input);
			try {
				myObservationDao.validate(input, null, encoded, EncodingEnum.XML, mode, null, mySrd);
				fail();
			} catch (PreconditionFailedException e) {
				return (OperationOutcome) e.getOperationOutcome();
			}
			break;
		}

		throw new IllegalStateException(); // shouldn't get here
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationInvalid() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationInvalid";

		Observation input = new Observation();
		String profileUri = "http://example.com/StructureDefinition/" + methodName;
		input.getMeta().getProfile().add(new CanonicalType(profileUri));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		String encoded = myFhirCtx.newJsonParser().encodeResourceToString(input);
		MethodOutcome outcome = myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);

		String ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(ooString);
		assertThat(ooString, containsString("StructureDefinition reference \\\"" + profileUri + "\\\" could not be resolved"));

	}

	@Test
	public void testValidateForCreate() {
		String methodName = "testValidateForCreate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must not be populated"));
		}

		pat.setId("");
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);

	}

	@Test
	public void testValidateForUpdate() {
		String methodName = "testValidateForUpdate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must be populated"));
		}

	}

	@Test
	public void testValidateForUpdateWithContained() {
		String methodName = "testValidateForUpdate";

		Organization org = new Organization();
		org.setId("#123");

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must be populated"));
		}

	}

	@Test
	public void testValidateForDelete() {
		String methodName = "testValidateForDelete";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		pat.getManagingOrganization().setReference(orgId.getValue());
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		OperationOutcome outcome = null;
		try {
			myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			outcome = (OperationOutcome) e.getOperationOutcome();
		}

		String ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Unable to delete Organization"));

		pat.setId(patId);
		pat.getManagingOrganization().setReference("");
		myPatientDao.update(pat, mySrd);

		outcome = (OperationOutcome) myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd).getOperationOutcome();
		ooString = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Ok to delete"));

	}

	@Test
	public void testValidateForDeleteWithReferentialIntegrityDisabled() {
		myDaoConfig.setEnforceReferentialIntegrityOnDelete(false);
		String methodName = "testValidateForDelete";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		pat.getManagingOrganization().setReference(orgId.getValue());
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);

		myDaoConfig.setEnforceReferentialIntegrityOnDelete(true);
		try {
			myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		myDaoConfig.setEnforceReferentialIntegrityOnDelete(false);


		myOrganizationDao.read(orgId);

		myOrganizationDao.delete(orgId);

		try {
			myOrganizationDao.read(orgId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	private IBaseResource findResourceByIdInBundle(Bundle vss, String name) {
		IBaseResource retVal = null;
		for (BundleEntryComponent next : vss.getEntry()) {
			if (next.getResource().getIdElement().getIdPart().equals(name)) {
				retVal = next.getResource();
				break;
			}
		}
		if (retVal == null) {
			fail("Can't find VS: " + name);
		}
		return retVal;
	}
	
	/**
	 * Format has changed, this is out of date
	 */
	@Test
	@Ignore
	public void testValidateNewQuestionnaireFormat() throws Exception {
		String input =IOUtils.toString(FhirResourceDaoR4ValidateTest.class.getResourceAsStream("/questionnaire_r4.xml"));
		try {
		MethodOutcome results = myQuestionnaireDao.validate(null, null, input, EncodingEnum.XML, ValidationModeEnum.UPDATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) results.getOperationOutcome();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		} catch (PreconditionFailedException e) {
			// this is a failure of the test
			ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			throw e;
		}
	}

}
