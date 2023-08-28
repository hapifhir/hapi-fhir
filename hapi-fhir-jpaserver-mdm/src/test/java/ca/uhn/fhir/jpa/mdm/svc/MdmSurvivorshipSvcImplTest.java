package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MdmSurvivorshipSvcImplTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private IMdmLinkQuerySvc myMdmLinkQuerySvc;
	@Mock
	private DaoRegistry myDaoRegistry;

	private GoldenResourceHelper myGoldenResourceHelper;

	// mocks for our GoldenResourceHelper
	@Mock
	private IMdmSettings myMdmSettings;
	@Mock
	private EIDHelper myEIDHelper;
	@Mock
	private MdmPartitionHelper myMdmPartitionHelper;

	private MdmSurvivorshipSvcImpl mySvc;

	@BeforeEach
	public void before() {
		myGoldenResourceHelper = spy(new GoldenResourceHelper(
			myFhirContext,
			myMdmSettings,
			myEIDHelper,
			myMdmPartitionHelper
		));

		mySvc = new MdmSurvivorshipSvcImpl(
			myFhirContext,
			myDaoRegistry,
			myGoldenResourceHelper,
			myMdmLinkQuerySvc
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void rebuildGoldenResourceCurrentLinksUsingSurvivorshipRules_withManyLinks_rebuildsInUpdateOrder() {
		// setup
		// create resources
		Patient goldenPatient = new Patient();
		goldenPatient.addAddress()
			.setCity("Toronto")
			.addLine("200 fake st");
		goldenPatient.addName()
			.setFamily("Doe")
			.addGiven("Jane");
		goldenPatient.setId("Patient/777");
		MdmResourceUtil.setMdmManaged(goldenPatient);
		MdmResourceUtil.setGoldenResource(goldenPatient);

		List<IBaseResource> resources = new ArrayList<>();
		List<MdmLinkWithRevisionJson> links = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			// we want our resources to be slightly different
			Patient patient = new Patient();
			patient.addName()
				.setFamily("Doe")
				.addGiven("John" + i);
			patient.addAddress()
				.setCity("Toronto")
				.addLine(String.format("11%d fake st", i));
			patient.addIdentifier()
				.setSystem("http://example.com")
				.setValue("Value" + i);
			patient.setId("Patient/" + i);
			resources.add(patient);

			MdmLink link = createLinkWithoutUpdateDate(patient, goldenPatient);

			links.add(createMdmLinkWithRevisionJsonFromMdmLink(
				link,
				Date.from(Instant.now().minus(i, ChronoUnit.HOURS)))
			);
		}

		IFhirResourceDao resourceDao = mock(IFhirResourceDao.class);

		// when
		when(myDaoRegistry.getResourceDao(eq("Patient")))
			.thenReturn(resourceDao);
		AtomicInteger getCall = new AtomicInteger();
		when(resourceDao.readByPid(any(IResourcePersistentId.class)))
			.thenAnswer(t -> {
				return resources.get(getCall.getAndIncrement());
			});
		when(myMdmLinkQuerySvc.queryLinkHistory(any(MdmHistorySearchParameters.class)))
			.thenReturn(links);
		when(myMdmSettings.getMdmRules())
			.thenReturn(new MdmRulesJson());
		// we will return a non-empty list to reduce mocking
		when(myEIDHelper.getExternalEid(any()))
			.thenReturn(Collections.singletonList(new CanonicalEID("example", "value", "use")));

		// test
		Patient goldenPatientRebuilt = mySvc.rebuildGoldenResourceCurrentLinksUsingSurvivorshipRules(
			goldenPatient,
			createTransactionContext()
		);

		// verify
		assertNotNull(goldenPatientRebuilt);
		// make sure it doesn't match the previous golden resource
		assertNotEquals(goldenPatient, goldenPatientRebuilt);
		assertNotEquals(goldenPatient.getName().get(0).getGiven(), goldenPatientRebuilt.getName().get(0).getGiven());
		assertNotEquals(goldenPatient.getAddress().get(0).getLine().get(0), goldenPatientRebuilt.getAddress().get(0).getLine().get(0));
		// make sure it's still a golden resource
		assertTrue(MdmResourceUtil.isGoldenRecord(goldenPatientRebuilt));

		verify(resourceDao)
			.update(eq(goldenPatientRebuilt), any(RequestDetails.class));
	}

	private MdmLink createLinkWithoutUpdateDate(Patient theSource, Patient theGoldenResource) {
		MdmLink link = new MdmLink();
		link.setCreated(Date.from(
			Instant.now().minus(2, ChronoUnit.DAYS)
		));
		link.setLinkSource(MdmLinkSourceEnum.AUTO);
		link.setMatchResult(MdmMatchResultEnum.MATCH);
		link.setSourcePersistenceId(JpaPid.fromId(theSource.getIdElement().getIdPartAsLong()));
		link.setGoldenResourcePersistenceId(JpaPid.fromId(theGoldenResource.getIdElement().getIdPartAsLong()));

		return link;
	}

	private MdmTransactionContext createTransactionContext() {
		MdmTransactionContext context = new MdmTransactionContext();
		context.setRestOperation(MdmTransactionContext.OperationType.UPDATE_LINK);
		context.setResourceType("Patient");
		return context;
	}

	private MdmLinkWithRevisionJson createMdmLinkWithRevisionJsonFromMdmLink(MdmLink theLink, Date theTimestamp) {
		MdmLinkJson linkJson = new MdmLinkJson();
		linkJson.setSourceId(theLink.getSourcePersistenceId().toString());
		linkJson.setGoldenResourceId(theLink.getGoldenResourcePersistenceId().toString());
		linkJson.setMatchResult(theLink.getMatchResult());
		linkJson.setLinkSource(theLink.getLinkSource());

		return new MdmLinkWithRevisionJson(
			linkJson,
			1L,
			theTimestamp
		);
	}
}
