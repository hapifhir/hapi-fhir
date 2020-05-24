package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class EmpiProviderQueryLinkR4Test extends BaseLinkR4Test {
private static final Logger ourLog = LoggerFactory.getLogger(EmpiProviderQueryLinkR4Test.class);
	private StringType myLinkSource;
	private IdType myPerson1Id;
	private IdType myPerson2Id;

	@Before
	public void before() {
		super.before();

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());

		// Add a possible duplicate
		myLinkSource = new StringType(EmpiLinkSourceEnum.AUTO.name());
		Person person1 = createPerson();
		myPerson1Id = person1.getIdElement().toVersionless();
		Long person1Pid = myIdHelperService.getPidOrNull(person1);
		Person person2 = createPerson();
		myPerson2Id = person2.getIdElement().toVersionless();
		Long person2Pid = myIdHelperService.getPidOrNull(person2);
		EmpiLink empiLink = new EmpiLink().setPersonPid(person1Pid).setTargetPid(person2Pid).setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(EmpiLinkSourceEnum.AUTO);
		myEmpiLinkDaoSvc.save(empiLink);
	}

	@Test
	public void testQueryLinkOneMatch() {

		Parameters result = myEmpiProviderR4.queryLinks(myPersonId, myPatientId, null, null, myRequestDetails);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertEmpiLink(4, part, myPersonId.getValue(), myPatientId.getValue(), EmpiMatchResultEnum.MATCH);
	}

	@Test
	public void testQueryLinkThreeMatches() {
		// Add a second patient
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		IdType patientId = patient.getIdElement().toVersionless();
		Person person = getPersonFromTarget(patient);
		IdType personId = person.getIdElement().toVersionless();

		Parameters result = myEmpiProviderR4.queryLinks(null, null, null, myLinkSource, myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(3));
		List<Parameters.ParametersParameterComponent> part = list.get(2).getPart();
		assertEmpiLink(4, part, personId.getValue(), patientId.getValue(), EmpiMatchResultEnum.MATCH);
	}

	@Test
	public void testQueryPossibleDuplicates() {
		Parameters result = myEmpiProviderR4.getDuplicatePersons(myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertEmpiLink(2, part, myPerson1Id.getValue(), myPerson2Id.getValue(), EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
	}


	private void assertEmpiLink(int theExpectedSize, List<Parameters.ParametersParameterComponent> thePart, String thePersonId, String theTargetId, EmpiMatchResultEnum theMatchResult) {
		assertThat(thePart, hasSize(theExpectedSize));
		assertThat(thePart.get(0).getName(), is("personId"));
		assertThat(thePart.get(0).getValue().toString(), is(thePersonId));
		assertThat(thePart.get(1).getName(), is("targetId"));
		assertThat(thePart.get(1).getValue().toString(), is(theTargetId));
		if (theExpectedSize > 2) {
			assertThat(thePart.get(2).getName(), is("matchResult"));
			assertThat(thePart.get(2).getValue().toString(), is(theMatchResult.name()));
			assertThat(thePart.get(3).getName(), is("linkSource"));
			assertThat(thePart.get(3).getValue().toString(), is("AUTO"));
		}
	}
}
