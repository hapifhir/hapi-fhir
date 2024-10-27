package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.patch.FhirPatchApplyR4Test;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class DiffProviderR4Test extends BaseResourceProviderR4Test {


	private static final Logger ourLog = LoggerFactory.getLogger(DiffProviderR4Test.class);

	@Test
	public void testMetaIgnoredByDefault() {
		// Create and 2 updates
		IIdType id = createPatient(withActiveFalse()).toUnqualifiedVersionless();
		createPatient(withId(id), withActiveTrue());
		createPatient(withId(id), withActiveTrue(), withFamily("SMITH"));

		Parameters diff = myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(2);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.text.div", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><table class=\"hapiPropertyTable\"><tbody/></table></div>", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"><b>SMITH </b></div><table class=\"hapiPropertyTable\"><tbody/></table></div>", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

		assertEquals("insert", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 1, "operation", "type"));
		assertEquals("Patient.name", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 1, "operation", "path"));
		assertEquals("0", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 1, "operation", "index"));
		assertEquals("SMITH", FhirPatchApplyR4Test.extractPartValue(diff, 1, "operation", "value", HumanName.class).getFamily());
	}


	@Test
	public void testLatestVersion_2_to_3() {
		// Create and 2 updates
		IIdType id = createPatient(withActiveFalse()).toUnqualifiedVersionless();
		createPatient(withId(id), withActiveTrue());
		createPatient(withId(id), withActiveTrue(), withFamily("SMITH"));

		Parameters diff = myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_INCLUDE_META_PARAMETER, new BooleanType(true))
			.useHttpGet()
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(4);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.meta.versionId", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("2", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("3", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 1, "operation", "type"));
		assertEquals("Patient.meta.lastUpdated", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 1, "operation", "path"));

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 2, "operation", "type"));
		assertEquals("Patient.text.div", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 2, "operation", "path"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><table class=\"hapiPropertyTable\"><tbody/></table></div>", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 2, "operation", "previousValue"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"><b>SMITH </b></div><table class=\"hapiPropertyTable\"><tbody/></table></div>", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 2, "operation", "value"));

		assertEquals("insert", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 3, "operation", "type"));
		assertEquals("Patient.name", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 3, "operation", "path"));
		assertEquals("0", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 3, "operation", "index"));
		assertEquals("SMITH", FhirPatchApplyR4Test.extractPartValue(diff, 3, "operation", "value", HumanName.class).getFamily());
	}


	@Test
	public void testLatestVersion_PreviousVersionExpunged() {
		// Create and 2 updates
		IIdType id = createPatient(withActiveFalse()).toUnqualifiedVersionless();
		createPatient(withId(id), withActiveTrue());
		createPatient(withId(id), withActiveTrue(), withFamily("SMITH"));

		runInTransaction(() -> {
			ResourceHistoryTable version2 = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(id.getIdPartAsLong(), 2);
			myResourceHistoryTableDao.deleteByPid(version2.getId());
		});

		Parameters diff = myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_INCLUDE_META_PARAMETER, new BooleanType(true))
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(5);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.meta.versionId", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("1", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("3", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

	}


	@Test
	public void testLatestVersion_OnlyOneVersionExists() {
		// Create only
		IIdType id = createPatient(withActiveTrue()).toUnqualifiedVersionless();

		Parameters diff = myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withNoParameters(Parameters.class)
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(1);

		assertEquals("insert", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals(true, FhirPatchApplyR4Test.extractPartValue(diff, 0, "operation", "value", Patient.class).getActive());

	}


	@Test
	public void testExplicitFromVersion() {
		// Create and 2 updates
		IIdType id = createPatient(withActiveFalse()).toUnqualifiedVersionless();
		createPatient(withId(id), withActiveTrue());
		createPatient(withId(id), withActiveTrue(), withFamily("SMITH"));

		Parameters diff = myClient
			.operation()
			.onInstance(id)
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_VERSION_PARAMETER, new StringType("1"))
			.andParameter(ProviderConstants.DIFF_INCLUDE_META_PARAMETER, new BooleanType(true))
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(5);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.meta.versionId", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("1", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("3", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

	}


	@Test
	public void testDifferentResources_Versionless() {
		// Create and 2 updates
		IIdType id1 = createPatient(withId("A"), withActiveFalse()).toUnqualifiedVersionless();
		IIdType id2 = createPatient(withId("B"), withActiveTrue()).toUnqualifiedVersionless();

		Parameters diff = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_PARAMETER, id1)
			.andParameter(ProviderConstants.DIFF_TO_PARAMETER, id2)
			.andParameter(ProviderConstants.DIFF_INCLUDE_META_PARAMETER, new BooleanType(true))
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(3);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.id", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("A", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("B", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

	}

	@Test
	public void testDifferentResources_Versioned() {
		// Create and 2 updates
		IIdType id1 = createPatient(withId("A"), withActiveTrue()).toUnqualifiedVersionless();
		id1 = createPatient(withId(id1), withActiveTrue(), withFamily("SMITH")).toUnqualified();

		IIdType id2 = createPatient(withId("B"), withActiveFalse()).toUnqualifiedVersionless();
		id2 = createPatient(withId(id2), withActiveTrue(), withFamily("JONES")).toUnqualified();

		Parameters diff = myClient
			.operation()
			.onServer()
			.named(ProviderConstants.DIFF_OPERATION_NAME)
			.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_PARAMETER, id1.withVersion("1"))
			.andParameter(ProviderConstants.DIFF_TO_PARAMETER, id2.withVersion("1"))
			.andParameter(ProviderConstants.DIFF_INCLUDE_META_PARAMETER, new BooleanType(true))
			.execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(diff));

		assertThat(diff.getParameter()).hasSize(3);

		assertEquals("replace", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "type"));
		assertEquals("Patient.id", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "path"));
		assertEquals("A", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "previousValue"));
		assertEquals("B", FhirPatchApplyR4Test.extractPartValuePrimitive(diff, 0, "operation", "value"));

	}

	@Test
	public void testDifferentResources_DifferentTypes() {
		try {
			myClient
				.operation()
				.onServer()
				.named(ProviderConstants.DIFF_OPERATION_NAME)
				.withParameter(Parameters.class, ProviderConstants.DIFF_FROM_PARAMETER, new IdType("Patient/123"))
				.andParameter(ProviderConstants.DIFF_TO_PARAMETER, new IdType("Observation/456"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1129) + "Unable to diff two resources of different types", e.getMessage());
		}
	}

}
