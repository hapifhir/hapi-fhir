package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.RangeTestHelper;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class MdmProviderQueryLinkR4Test extends BaseLinkR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmProviderQueryLinkR4Test.class);
	public static final double THOUSANDTH = .001d;
	private static final int MDM_LINK_PROPERTY_COUNT = 9;
	private static final StringType RESOURCE_TYPE_PATIENT = new StringType("Patient");
	private static final StringType RESOURCE_TYPE_OBSERVATION = new StringType("Observation");

	private StringType myLinkSource;
	private StringType myGoldenResource1Id;
	private StringType myGoldenResource2Id;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		// Add a second patient
		createPatientAndUpdateLinks(buildJanePatient());

		// Add a possible duplicate
		myLinkSource = new StringType(MdmLinkSourceEnum.AUTO.name());
		Patient sourcePatient1 = createGoldenPatient();
		myGoldenResource1Id = new StringType(sourcePatient1.getIdElement().toVersionless().getValue());
		JpaPid sourcePatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient1));
		Patient sourcePatient2 = createGoldenPatient();
		myGoldenResource2Id = new StringType(sourcePatient2.getIdElement().toVersionless().getValue());
		JpaPid sourcePatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient2));

		createPossibleDuplicateLinkByPid(sourcePatient2Pid, sourcePatient1Pid);
	}

	private void createPossibleDuplicateLinkByPid(JpaPid theSourcePid, JpaPid theGoldenPid) {
		MdmLink possibleDuplicateMdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		possibleDuplicateMdmLink.setGoldenResourcePersistenceId(theGoldenPid)
			.setSourcePersistenceId(theSourcePid)
			.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.setLinkSource(MdmLinkSourceEnum.AUTO)
			.setScore(1.0)
			.setRuleCount(1L);
		saveLink(possibleDuplicateMdmLink);
	}

	@Test
	public void testQueryLinkOneMatch() {
		Parameters result = (Parameters) myMdmProvider.queryLinks(mySourcePatientId, myPatientId, null, null, new UnsignedIntType(0), new UnsignedIntType(10), new StringType(), myRequestDetails, null);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(1);
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(MDM_LINK_PROPERTY_COUNT, part, mySourcePatientId.getValue(), myPatientId.getValue(), MdmMatchResultEnum.POSSIBLE_MATCH, "false", "true", "1");
	}

	@Test
	public void testQueryLinkWithResourceType() {
		Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null, new UnsignedIntType(0), new UnsignedIntType(10), new StringType(), myRequestDetails, RESOURCE_TYPE_PATIENT);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).as("All resources with Patient type found").hasSize(3);
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(MDM_LINK_PROPERTY_COUNT, part, mySourcePatientId.getValue(), myPatientId.getValue(), MdmMatchResultEnum.POSSIBLE_MATCH, "false", "true", "1");
	}


	@Nested
	public class QueryLinkWithSort {
		@Test
		public void byCreatedDescending() {
			Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null,
				new UnsignedIntType(0), new UnsignedIntType(10), new StringType("-myCreated"),
				myRequestDetails, new StringType("Patient"));

			List<Parameters.ParametersParameterComponent> linkList = getParametersByName(result, "link");
			assertThat(linkList).hasSize(3);

			List<Long> createdDates = linkList.stream().map(this::extractCreated).collect(Collectors.toList());

			// by created descending
			Comparator<Long> comp = Comparator.comparingLong( (Long e) -> e).reversed();
			List<Long> expected = createdDates.stream().sorted(comp).collect(Collectors.toList());

			assertEquals(expected, createdDates);
		}

		@Test
		public void byScoreDescending() {
			addScoresToLinksInCreationOrder(List.of(.5d, .1d, .3d));

			Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null,
				new UnsignedIntType(0), new UnsignedIntType(10), new StringType("-myScore"),
				myRequestDetails, new StringType("Patient"));

			List<Parameters.ParametersParameterComponent> linkList = getParametersByName(result, "link");
			assertThat(linkList).hasSize(3);

			List<Double> scores = linkList.stream().map(this::extractScore).collect(Collectors.toList());

			// by score descending
			Comparator<Double> comp = Comparator.comparingDouble( (Double e) -> e).reversed();
			List<Double> expected = scores.stream().sorted(comp).collect(Collectors.toList());

			assertEquals(expected, scores);
		}


		@Test
		public void byScoreDescendingAndThenCreated() {
			addScoresToLinksInCreationOrder(List.of(.4d, .4d, .3d));

			Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null,
				new UnsignedIntType(0), new UnsignedIntType(10), new StringType("-myScore,-myCreated"),
				myRequestDetails, new StringType("Patient"));

			List<Parameters.ParametersParameterComponent> linkList = getParametersByName(result, "link");
			assertThat(linkList).hasSize(3);

			List<Pair<Long, Double>> resultUpdatedScorePairs = linkList.stream()
				.map(l -> Pair.of(extractCreated(l), extractScore(l))).collect(Collectors.toList());

			// by desc score then desc created
			Comparator<Pair<Long, Double>> comp = Comparator
				.comparing( (Pair<Long, Double> p) -> p.getRight(), Comparator.reverseOrder() )
				.thenComparing(Pair::getLeft, Comparator.reverseOrder() );

			List<Pair<Long, Double>> expected = resultUpdatedScorePairs.stream().sorted(comp).collect(Collectors.toList());

			assertEquals(expected, resultUpdatedScorePairs);
		}

		@Test
		public void testPaginationWhenSorting() {
			addTwoMoreMdmLinks();
			addScoresToLinksInCreationOrder(List.of(.5d, .1d, .3d, .8d, .6d));
			List<Double> expectedScoresPage1 = List.of(.8d, .6d, .5d);
			List<Double> expectedScoresPage2 = List.of(.3d, .1d);

			int pageSize = 3;

			// first page

			Parameters page1 = (Parameters) myMdmProvider.queryLinks(null, null, null, null,
				new UnsignedIntType(0), new UnsignedIntType(pageSize), new StringType("-myScore"),
				myRequestDetails, new StringType("Patient"));

			List<Parameters.ParametersParameterComponent> linkListPage1 = getParametersByName(page1, "link");
			assertThat(linkListPage1).hasSize(pageSize);

			List<Double> scoresPage1 = linkListPage1.stream().map(this::extractScore).collect(Collectors.toList());
			assertEquals(expectedScoresPage1, scoresPage1);

			// second page

			Parameters page2 = (Parameters) myMdmProvider.queryLinks(null, null, null, null,
				new UnsignedIntType(pageSize), new UnsignedIntType(pageSize), new StringType("-myScore"),
				myRequestDetails, new StringType("Patient"));

			List<Parameters.ParametersParameterComponent> linkListPage2 = getParametersByName(page2, "link");
			assertThat(linkListPage2).hasSize(2);

			List<Double> scoresPage2 = linkListPage2.stream().map(this::extractScore).collect(Collectors.toList());
			assertEquals(expectedScoresPage2, scoresPage2);
		}


		private Long extractCreated(Parameters.ParametersParameterComponent theParamComponent) {
			Optional<IBase> opt = ParametersUtil.getParameterPartValue(myFhirContext, theParamComponent, "linkUpdated");
			assertThat(opt).isPresent();
			DecimalType createdDateDt = (DecimalType) opt.get();
			return createdDateDt.getValue().longValue();
		}


		private Double extractScore(Parameters.ParametersParameterComponent theParamComponent) {
			Optional<IBase> opt = ParametersUtil.getParameterPartValue(myFhirContext, theParamComponent, "score");
			assertThat(opt).isPresent();
			DecimalType scoreIntegerDt = (DecimalType) opt.get();
			assertNotNull(scoreIntegerDt.getValue());
			return scoreIntegerDt.getValue().doubleValue();
		}

	}

	private void addTwoMoreMdmLinks() {
		createPatientAndUpdateLinks(buildFrankPatient());

		// Add a possible duplicate
		myLinkSource = new StringType(MdmLinkSourceEnum.AUTO.name());
		Patient sourcePatient1 = createGoldenPatient();
		myGoldenResource1Id = new StringType(sourcePatient1.getIdElement().toVersionless().getValue());
		JpaPid sourcePatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient1));
		Patient sourcePatient2 = createGoldenPatient();
		myGoldenResource2Id = new StringType(sourcePatient2.getIdElement().toVersionless().getValue());
		JpaPid sourcePatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient2));

		MdmLink possibleDuplicateMdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		possibleDuplicateMdmLink.setGoldenResourcePersistenceId(sourcePatient1Pid);
		possibleDuplicateMdmLink.setSourcePersistenceId(sourcePatient2Pid);
		possibleDuplicateMdmLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE).setLinkSource(MdmLinkSourceEnum.AUTO);
		saveLink(possibleDuplicateMdmLink);
	}


	private void addScoresToLinksInCreationOrder(List<Double> theScores) {
		List<MdmLink> links = myMdmLinkDao.findAll();
		assertThat(links).hasSize(theScores.size());

		links.sort( Comparator.comparing(MdmLink::getCreated) );

		for (int i = 0; i < links.size(); i++) {
			MdmLink link = links.get(i);
			link.setScore( theScores.get(i) );
			myMdmLinkDao.save(link);
		}
	}


	@Test
	public void testQueryLinkWithResourceTypeNoMatch() {
		Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, null, new UnsignedIntType(0), new UnsignedIntType(10), new StringType(), myRequestDetails, RESOURCE_TYPE_OBSERVATION);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(0);
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
			Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, myLinkSource, new UnsignedIntType(offset), new UnsignedIntType(count), new StringType(), myRequestDetails, null);
			List<Parameters.ParametersParameterComponent> parameter = result.getParameter();


			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
			List<Parameters.ParametersParameterComponent> previousUrl = getParametersByName(result, "prev");
			if (offset == 0) {
				assertThat(previousUrl).hasSize(0);
			} else {
				assertThat(previousUrl).hasSize(1);
			}

			String sourceResourceIds = parameter.stream().flatMap(p -> p.getPart().stream()).filter(part -> part.getName().equals("sourceResourceId")).map(part -> part.getValue().toString()).collect(Collectors.joining(","));
			ourLog.warn("Search at offset {} took {} ms",offset, sw.getMillisAndRestart());
			ourLog.warn("Found source resource IDs: {}", sourceResourceIds);
			List<Parameters.ParametersParameterComponent> mdmLink = getParametersByName(result, "link");
			assertThat(mdmLink.size()).isLessThanOrEqualTo(2);

			List<Parameters.ParametersParameterComponent> selfUrl = getParametersByName(result, "self");
			assertThat(selfUrl).hasSize(1);
			//We have stopped finding patients, make sure theres no next page
			if (StringUtils.isEmpty(sourceResourceIds)) {
				List<Parameters.ParametersParameterComponent> nextUrl= getParametersByName(result, "next");
				assertThat(nextUrl).isEmpty();
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
			 	new StringType(),
			 	myRequestDetails,
			 	null);
		} catch (InvalidRequestException e) {
			//Then
			assertEquals(Msg.code(1524) + "_count must be greater than 0.", e.getMessage());
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
				new StringType(),
				myRequestDetails,
				null);
		} catch (InvalidRequestException e) {
			//Then
			assertEquals(Msg.code(1524) + "_offset must be greater than or equal to 0. ", e.getMessage());
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
				new StringType(),
				myRequestDetails,
				null);
		} catch (InvalidRequestException e) {
			//Then
			assertEquals(Msg.code(1524) + "_offset must be greater than or equal to 0. _count must be greater than 0.", e.getMessage());
		}
	}

	@Test
	public void testQueryLnkThreeMatches() {
		// Add a third patient
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		IdType patientId = patient.getIdElement().toVersionless();
		IAnyResource goldenResource = getGoldenResourceFromTargetResource(patient);
		IIdType goldenResourceId = goldenResource.getIdElement().toVersionless();

		Parameters result = (Parameters) myMdmProvider.queryLinks(null, null, null, myLinkSource, new UnsignedIntType(0), new UnsignedIntType(10), new StringType(), myRequestDetails, null);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(4);
		List<Parameters.ParametersParameterComponent> part = list.get(3).getPart();
		assertMdmLink(MDM_LINK_PROPERTY_COUNT, part, goldenResourceId.getValue(), patientId.getValue(), MdmMatchResultEnum.MATCH, "false", "false", ".666");
	}

	@Test
	public void testQueryPossibleDuplicates() {
		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails, null);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(1);
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(2, part, myGoldenResource1Id.getValue(), myGoldenResource2Id.getValue(), MdmMatchResultEnum.POSSIBLE_DUPLICATE, "false", "false", null);
		assertResponseDuplicateCount(list.size(), result);
	}

	@Test
	public void testQueryPossibleDuplicates_withCountLessThanTotal_returnsCorrectTotal() {
		// Given: create second possible duplicate
		JpaPid sourcePatient1Pid = runInTransaction(()->myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), new IdType(myGoldenResource1Id.toString())));
		Patient sourcePatient3 = createGoldenPatient();
		JpaPid sourcePatient3Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient3));

		createPossibleDuplicateLinkByPid(sourcePatient1Pid, sourcePatient3Pid);

		// When
		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(1), myRequestDetails, null);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));

		// Then: parameters should have 1 link (since count = 1), total should be 2
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(1);
		assertResponseDuplicateCount(2, result);
	}

	private void assertResponseDuplicateCount(int expectedSize, Parameters result) {
		List<Parameters.ParametersParameterComponent> count = getParametersByName(result, "total");
		assertThat(count).hasSize(1);
		assertEquals(String.valueOf(expectedSize), count.get(0).getValue().primitiveValue());
	}

	@Test
	public void testQueryPossibleDuplicatesWithResourceType() {
		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails, RESOURCE_TYPE_PATIENT);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).as("All duplicate resources with " + RESOURCE_TYPE_PATIENT + " type found").hasSize(1);
		List<Parameters.ParametersParameterComponent> part = list.get(0).getPart();
		assertMdmLink(2, part, myGoldenResource1Id.getValue(), myGoldenResource2Id.getValue(), MdmMatchResultEnum.POSSIBLE_DUPLICATE, "false", "false", null);
		assertThat(myGoldenResource1Id.toString()).contains("Patient");
		assertResponseDuplicateCount(list.size(), result);
	}

	@Test
	public void testQueryPossibleDuplicatesWithResourceTypeNoMatch() {
		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails, RESOURCE_TYPE_OBSERVATION);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");

		assertThat(list).hasSize(0);
		assertResponseDuplicateCount(list.size(), result);
	}

	@Test
	public void testNotDuplicate() {
		{
			Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails, null);
			List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");

			assertThat(list).hasSize(1);
		}
		{
			Parameters result = (Parameters) myMdmProvider.notDuplicate(myGoldenResource1Id, myGoldenResource2Id, myRequestDetails);
			ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(result));
			assertEquals("success", result.getParameterFirstRep().getName());
			assertTrue(((BooleanType) (result.getParameterFirstRep().getValue())).booleanValue());
		}

		Parameters result = (Parameters) myMdmProvider.getDuplicateGoldenResources(new UnsignedIntType(0), new UnsignedIntType(10), myRequestDetails, null);
		List<Parameters.ParametersParameterComponent> list = getParametersByName(result, "link");
		assertThat(list).hasSize(0);
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

	private void assertMdmLink(int theExpectedSize, List<Parameters.ParametersParameterComponent> thePart, String theGoldenResourceId, String theTargetId, MdmMatchResultEnum theMatchResult, String theEidMatch, String theNewGoldenResource, String theExpectedScore) {
		assertThat(thePart).hasSize(theExpectedSize);
		assertEquals("goldenResourceId", thePart.get(0).getName());
		assertEquals(removeVersion(theGoldenResourceId), thePart.get(0).getValue().toString());
		assertEquals("sourceResourceId", thePart.get(1).getName());
		assertEquals(removeVersion(theTargetId), thePart.get(1).getValue().toString());
		if (theExpectedSize > 2) {
			assertEquals("matchResult", thePart.get(2).getName());
			assertEquals(theMatchResult.name(), thePart.get(2).getValue().toString());
			assertEquals("linkSource", thePart.get(3).getName());
			assertEquals("AUTO", thePart.get(3).getValue().toString());

			assertEquals("eidMatch", thePart.get(4).getName());
			assertEquals(theEidMatch, thePart.get(4).getValue().primitiveValue());

			assertEquals("hadToCreateNewResource", thePart.get(5).getName());
			assertEquals(theNewGoldenResource, thePart.get(5).getValue().primitiveValue());

			assertEquals("score", thePart.get(6).getName());
			double expectedScore = Double.parseDouble(theExpectedScore);
			double actualScore = Double.parseDouble(thePart.get(6).getValue().primitiveValue());
			assertThat(actualScore).isBetween(expectedScore - THOUSANDTH, expectedScore + THOUSANDTH);
		}
	}

	private String removeVersion(String theId) {
		return new IdDt(theId).toVersionless().getValue();
	}
}
