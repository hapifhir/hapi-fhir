package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiProviderQueryLinkR4Test extends BaseLinkR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiProviderQueryLinkR4Test.class);
	private StringType myLinkSource;
	private StringType myPerson1Id;
	private StringType myPerson2Id;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());

		// Add a possible duplicate
		myLinkSource = new StringType(EmpiLinkSourceEnum.AUTO.name());
		Patient sourcePatient1 = createSourceResourcePatient();
		myPerson1Id = new StringType(sourcePatient1.getIdElement().toVersionless().getValue());
		Long sourcePatient1Pid = myIdHelperService.getPidOrNull(sourcePatient1);
		Patient sourcePatient2 = createSourceResourcePatient();
		myPerson2Id = new StringType(sourcePatient2.getIdElement().toVersionless().getValue());
		Long sourcePatient2Pid = myIdHelperService.getPidOrNull(sourcePatient2);

		EmpiLink possibleDuplicateEmpiLink = myEmpiLinkDaoSvc.newEmpiLink().setSourceResourcePid(sourcePatient1Pid).setTargetPid(sourcePatient2Pid).setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(EmpiLinkSourceEnum.AUTO);
		saveLink(possibleDuplicateEmpiLink);
	}

	@Test
	public void testQueryLinkOneMatch() {

		Parameters result = myEmpiProviderR4.queryLinks(mySourcePatientId, myPatientId, null, null, myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertEmpiLink(7, part, mySourcePatientId.getValue(), myPatientId.getValue(), EmpiMatchResultEnum.POSSIBLE_MATCH, "false", "true", null);
	}

	@Test
	public void testQueryLinkThreeMatches() {
		// Add a third patient
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		IdType patientId = patient.getIdElement().toVersionless();
		IAnyResource person = getSourceResourceFromTargetResource(patient);
		IIdType personId = person.getIdElement().toVersionless();

		Parameters result = myEmpiProviderR4.queryLinks(null, null, null, myLinkSource, myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(3));
		List<Parameters.ParametersParameterComponent> part = list.get(2).getPart();
		assertEmpiLink(7, part, personId.getValue(), patientId.getValue(), EmpiMatchResultEnum.MATCH, "false", "false", "2");
	}

	@Test
	public void testQueryPossibleDuplicates() {
		Parameters result = myEmpiProviderR4.getDuplicateGoldenResources(myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertEmpiLink(2, part, myPerson1Id.getValue(), myPerson2Id.getValue(), EmpiMatchResultEnum.POSSIBLE_DUPLICATE, "false", "false", null);
	}

	@Test
	public void testNotDuplicate() {
		{
			Parameters result = myEmpiProviderR4.getDuplicateGoldenResources(myRequestDetails);
			List<Parameters.ParametersParameterComponent> list = result.getParameter();
			assertThat(list, hasSize(1));
		}
		{
			Parameters result = myEmpiProviderR4.notDuplicate(myPerson1Id, myPerson2Id, myRequestDetails);
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
			assertEquals("success", result.getParameterFirstRep().getName());
			assertTrue(((BooleanType) (result.getParameterFirstRep().getValue())).booleanValue());
		}
		Parameters result = myEmpiProviderR4.getDuplicateGoldenResources(myRequestDetails);
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(0));
	}

	@Test
	public void testNotDuplicateBadId() {
		try {
			myEmpiProviderR4.notDuplicate(myPerson1Id, new StringType("Person/notAnId123"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Person/notAnId123 is not known", e.getMessage());
		}
	}

	private void assertEmpiLink(int theExpectedSize, List<Parameters.ParametersParameterComponent> thePart, String thePersonId, String theTargetId, EmpiMatchResultEnum theMatchResult, String theEidMatch, String theNewPerson, String theScore) {
		assertThat(thePart, hasSize(theExpectedSize));
		assertThat(thePart.get(0).getName(), is("goldenResourceId"));
		assertThat(thePart.get(0).getValue().toString(), is(removeVersion(thePersonId)));
		assertThat(thePart.get(1).getName(), is("targetResourceId"));
		assertThat(thePart.get(1).getValue().toString(), is(removeVersion(theTargetId)));
		if (theExpectedSize > 2) {
			assertThat(thePart.get(2).getName(), is("matchResult"));
			assertThat(thePart.get(2).getValue().toString(), is(theMatchResult.name()));
			assertThat(thePart.get(3).getName(), is("linkSource"));
			assertThat(thePart.get(3).getValue().toString(), is("AUTO"));

			assertThat(thePart.get(4).getName(), is("eidMatch"));
			assertThat(thePart.get(4).getValue().primitiveValue(), is(theEidMatch));

			assertThat(thePart.get(5).getName(), is("hadToCreateNewResource"));
			assertThat(thePart.get(5).getValue().primitiveValue(), is(theNewPerson));

			assertThat(thePart.get(6).getName(), is("score"));
			assertThat(thePart.get(6).getValue().primitiveValue(), is(theScore));
		}
	}

	private String removeVersion(String theId) {
		return new IdDt(theId).toVersionless().getValue();
	}
}
