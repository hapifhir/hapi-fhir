package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
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

	@Before
	public void before() {
		super.before();

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());
		myLinkSource = new StringType(EmpiLinkSourceEnum.AUTO.name());
	}

	@Test
	public void testQueryLinkOneMatch() {

		Parameters result = myEmpiProviderR4.queryLinks(myPersonId, myPatientId, null, null, myRequestDetails);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = result.getParameter();
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertThat(part.get(0).getName(), is("personId"));
		assertThat(part.get(0).getValue().toString(), is(myPersonId.getValue()));
		assertThat(part.get(1).getName(), is("targetId"));
		assertThat(part.get(1).getValue().toString(), is(myPatientId.getValue()));
		assertThat(part.get(2).getName(), is("matchResult"));
		assertThat(part.get(2).getValue().toString(), is("MATCH"));
		assertThat(part.get(3).getName(), is("linkSource"));
		assertThat(part.get(3).getValue().toString(), is("AUTO"));	}

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
		assertThat(part.get(0).getName(), is("personId"));
		assertThat(part.get(0).getValue().toString(), is(personId.getValue()));
		assertThat(part.get(1).getName(), is("targetId"));
		assertThat(part.get(1).getValue().toString(), is(patientId.getValue()));
		assertThat(part.get(2).getName(), is("matchResult"));
		assertThat(part.get(2).getValue().toString(), is("MATCH"));
		assertThat(part.get(3).getName(), is("linkSource"));
		assertThat(part.get(3).getValue().toString(), is("AUTO"));
	}
}
