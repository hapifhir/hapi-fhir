package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderQueryLinkR4Test extends BaseLinkR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmProviderQueryLinkR4Test.class);
	private StringType myLinkSource;
	private StringType myGoldenResource1Id;
	private StringType myGoldenResource2Id;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());

		// Add a possible duplicate
		myLinkSource = new StringType(MdmLinkSourceEnum.AUTO.name());
		Patient sourcePatient1 = createGoldenPatient();
		myGoldenResource1Id = new StringType(sourcePatient1.getIdElement().toVersionless().getValue());
		Long sourcePatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(sourcePatient1));
		Patient sourcePatient2 = createGoldenPatient();
		myGoldenResource2Id = new StringType(sourcePatient2.getIdElement().toVersionless().getValue());
		Long sourcePatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(sourcePatient2));

		MdmLink possibleDuplicateMdmLink = myMdmLinkDaoSvc.newMdmLink().setGoldenResourcePid(sourcePatient1Pid).setSourcePid(sourcePatient2Pid).setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(MdmLinkSourceEnum.AUTO);
		saveLink(possibleDuplicateMdmLink);
	}

	@Test
	public void testQueryLinkOneMatch() {
		Parameters result = (Parameters) myMdmProvider.queryLinks(mySourcePatientId, myPatientId, null, null, new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(7, part, mySourcePatientId.getValue(), myPatientId.getValue(), MdmMatchResultEnum.POSSIBLE_MATCH, "false", "true", null);
	}

	@Test
	public void testQueryLinkPages() {
		for (int i = 0; i < 10; i++) {
		 	createPatientAndUpdateLinks(buildJanePatient());
		}

		int offset = 0;
		int count = 2;
		StopWatch sw = new StopWatch();
		while (true)  {
			Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, myLinkSource, new UnsignedIntType(offset), new UnsignedIntType(count), myRequestDetails);
			List<Parameters.ParametersParameterComponent> parameter = result.getParameter();


			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
			List<Parameters.ParametersParameterComponent> previousUrl = getParametersByName(result, "prev");
			if (offset == 0) {
				assertThat(previousUrl, hasSize(0));
			} else {
				assertThat(previousUrl, hasSize(1));
			}

			String sourceResourceIds = parameter.stream().flatMap(p -> p.getPart().stream()).filter(part -> part.getName().equals("sourceResourceId")).map(part -> part.getValue().toString()).collect(Collectors.joining(","));
			ourLog.warn("Search at offset {} took {} ms",offset, sw.getMillisAndRestart());
			ourLog.warn("Found source resource IDs: {}", sourceResourceIds);
			List<Parameters.ParametersParameterComponent> mdmLink = getParametersByName(result, "link");
			assertThat(mdmLink.size(), is(lessThanOrEqualTo(2)));

			List<Parameters.ParametersParameterComponent> selfUrl = getParametersByName(result, "self");
			assertThat(selfUrl.size(), is(equalTo(1)));
			//We have stopped finding patients, make sure theres no next page
			if (StringUtils.isEmpty(sourceResourceIds)) {
				List<Parameters.ParametersParameterComponent> nextUrl= getParametersByName(result, "next");
				assertThat(nextUrl.size(), is(equalTo(0)));
				break;
			}
			offset += count;
		}
	}

	private List<Parameters.ParametersParameterComponent> getParametersByName(Parameters theParams, String theName) {
		return theParams.getParameter().stream().filter(p -> p.getName().equals(theName)).collect(Collectors.toList());
	}

	@Test
	public void testQueryWithIllegalPagingValuesFails() {
		//Given
		int count = 0;
		int offset = 0;
		try {
		 //When
		 myMdmProvider.queryLinks(
				null, null,
				null, myLinkSource,
				new UnsignedIntType(offset),
				new UnsignedIntType(count),
				myRequestDetails);
		} catch (InvalidRequestException e) {
			//Then
			assertThat(e.getMessage(), is(equalTo(Msg.code(1524) + "_count must be greater than 0.")));
		}

		//Given
		count = 1;
		offset= -1;
		try {
			//When
			myMdmProvider.queryLinks(
				null, null,
				null, myLinkSource,
				new UnsignedIntType(offset),
				new UnsignedIntType(count),
				myRequestDetails);
		} catch (InvalidRequestException e) {
			//Then
			assertThat(e.getMessage(), is(equalTo(Msg.code(1524) + "_offset must be greater than or equal to 0. ")));
		}

		//Given
		count = 0;
		offset= -1;
		try {
			//When
			myMdmProvider.queryLinks(
				null, null,
				null, myLinkSource,
				new UnsignedIntType(offset),
				new UnsignedIntType(count),
				myRequestDetails);
		} catch (InvalidRequestException e) {
			//Then
			assertThat(e.getMessage(), is(equalTo(Msg.code(1524) + "_offset must be greater than or equal to 0. _count must be greater than 0.")));
		}
	}

	@Test
	public void testQueryLnkThreeMatches() {
		// Add a third patient
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		IdType patientId = patient.getIdElement().toVersionless();
		IAnyResource goldenResource = getGoldenResourceFromTargetResource(patient);
		IIdType goldenResourceId = goldenResource.getIdElement().toVersionless();

		Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, myLinkSource, new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list, hasSize(4));
		List<Parameters.ParametersParameterComponent> part = list.get(3).getPart();
		assertMdmLink(7, part, goldenResourceId.getValue(), patientId.getValue(), MdmMatchResultEnum.MATCH, "false", "false", "2");
	}

	@Test
	public void testQueryPossibleDuplicates() {
		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10),myRequestDetails);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list, hasSize(1));
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(2, part, myGoldenResource1Id.getValue(), myGoldenResource2Id.getValue(), MdmMatchResultEnum.POSSIBLE_DUPLICATE, "false", "false", null);
	}

	@Test
	public void testNotDuplicate() {
		{
			Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10),myRequestDetails);
			List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");

			assertThat(list, hasSize(1));
		}
		{
			Parameters result = (Parameters) myMdmProvider.notDuplicate(myGoldenResource1Id, myGoldenResource2Id, myRequestDetails);
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
			assertEquals("success", result.getParameterFirstRep().getName());
			assertTrue(((BooleanType) (result.getParameterFirstRep().getValue())).booleanValue());
		}

		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10),myRequestDetails);
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list, hasSize(0));
	}

	@Test
	public void testNotDuplicateBadId() {
		try {
			myMdmProvider.notDuplicate(myGoldenResource1Id, new StringType("Patient/notAnId123"), myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(2001) + "Resource Patient/notAnId123 is not known", e.getMessage());
		}
	}

	private void assertMdmLink(int theExpectedSize, List<Parameters.ParametersParameterComponent> thePart, String theGoldenResourceId, String theTargetId, MdmMatchResultEnum theMatchResult, String theEidMatch, String theNewGoldenResource, String theScore) {
		assertThat(thePart, hasSize(theExpectedSize));
		assertThat(thePart.get(0).getName(), is("goldenResourceId"));
		assertThat(thePart.get(0).getValue().toString(), is(removeVersion(theGoldenResourceId)));
		assertThat(thePart.get(1).getName(), is("sourceResourceId"));
		assertThat(thePart.get(1).getValue().toString(), is(removeVersion(theTargetId)));
		if (theExpectedSize > 2) {
			assertThat(thePart.get(2).getName(), is("matchResult"));
			assertThat(thePart.get(2).getValue().toString(), is(theMatchResult.name()));
			assertThat(thePart.get(3).getName(), is("linkSource"));
			assertThat(thePart.get(3).getValue().toString(), is("AUTO"));

			assertThat(thePart.get(4).getName(), is("eidMatch"));
			assertThat(thePart.get(4).getValue().primitiveValue(), is(theEidMatch));

			assertThat(thePart.get(5).getName(), is("hadToCreateNewResource"));
			assertThat(thePart.get(5).getValue().primitiveValue(), is(theNewGoldenResource));

			assertThat(thePart.get(6).getName(), is("score"));
			assertThat(thePart.get(6).getValue().primitiveValue(), is(theScore));
		}
	}

	private String removeVersion(String theId) {
		return new IdDt(theId).toVersionless().getValue();
	}
}
