package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.svc.MdmSurvivorshipSvcImpl;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MdmSurvivorshipSvcImplTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

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

	@Mock
	private IdHelperService myIIdHelperService;

	@Mock
	private IMdmLinkQuerySvc myMdmLinkQuerySvc;

	@Mock
	private HapiTransactionService myTransactionService;

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
			myGoldenResourceHelper,
			myDaoRegistry,
			myMdmLinkQuerySvc,
			myIIdHelperService,
			myTransactionService
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@ParameterizedTest
	@ValueSource(booleans = {true,false})
	public void rebuildGoldenResourceCurrentLinksUsingSurvivorshipRules_withManyLinks_rebuildsInUpdateOrder(boolean theIsUseNonNumericId) {
		// setup
		// create resources
		int goldenId = 777;
		Patient goldenPatient = new Patient();
		goldenPatient.addAddress()
			.setCity("Toronto")
			.addLine("200 fake st");
		goldenPatient.addName()
			.setFamily("Doe")
			.addGiven("Jane");
		goldenPatient.setId("Patient/" + goldenId);
		MdmResourceUtil.setMdmManaged(goldenPatient);
		MdmResourceUtil.setGoldenResource(goldenPatient);

		List<IBaseResource> resources = new ArrayList<>();
		List<MdmLinkJson> links = new ArrayList<>();
		Map<String, IResourcePersistentId> sourceIdToPid = new HashMap<>();
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
			patient.setId("Patient/" + (theIsUseNonNumericId ? "pat" + i : Integer.toString(i)));
			resources.add(patient);

			MdmLinkJson link = createLinkJson(
				patient,
				goldenPatient
			);
			link.setSourcePid(JpaPid.fromId((long)i));
			link.setGoldenPid(JpaPid.fromId((long)goldenId));
			link.setSourceId(patient.getId());
			link.setGoldenResourceId(goldenPatient.getId());
			links.add(link);
			sourceIdToPid.put(patient.getId(), link.getSourcePid());
		}

		IFhirResourceDao resourceDao = mock(IFhirResourceDao.class);

		// when
		IHapiTransactionService.IExecutionBuilder executionBuilder = mock(IHapiTransactionService.IExecutionBuilder.class);
		when(myTransactionService.withRequest(any())).thenReturn(executionBuilder);
		doAnswer(a -> {
			Runnable callback = a.getArgument(0);
			callback.run();
			return 0;
		}).when(executionBuilder).execute(any(Runnable.class));
		when(myDaoRegistry.getResourceDao(eq("Patient")))
			.thenReturn(resourceDao);
		AtomicInteger counter = new AtomicInteger();
		when(resourceDao.readByPid(any()))
			.thenAnswer(params -> resources.get(counter.getAndIncrement()));
		Page<MdmLinkJson> linkPage = new PageImpl<>(links);
		when(myMdmLinkQuerySvc.queryLinks(any(), any()))
			.thenReturn(linkPage);
		when(myMdmSettings.getMdmRules())
			.thenReturn(new MdmRulesJson());
		doReturn(sourceIdToPid).when(myIIdHelperService)
			.resolveResourcePersistentIds(any(RequestPartitionId.class), anyString(), any(List.class));
		// we will return a non-empty list to reduce mocking
		when(myEIDHelper.getExternalEid(any()))
			.thenReturn(Collections.singletonList(new CanonicalEID("example", "value", "use")));

		// test
		Patient goldenPatientRebuilt = mySvc.rebuildGoldenResourceWithSurvivorshipRules(
			goldenPatient,
			createTransactionContext()
		);

		// verify
		assertNotNull(goldenPatientRebuilt);
		// make sure it doesn't match the previous golden resource
		assertThat(goldenPatientRebuilt).isNotEqualTo(goldenPatient);
		assertThat(goldenPatientRebuilt.getName().get(0).getGiven()).isNotEqualTo(goldenPatient.getName().get(0).getGiven());
		assertThat(goldenPatientRebuilt.getAddress().get(0).getLine().get(0)).isNotEqualTo(goldenPatient.getAddress().get(0).getLine().get(0));
		// make sure it's still a golden resource
		assertTrue(MdmResourceUtil.isGoldenRecord(goldenPatientRebuilt));

		verify(resourceDao)
			.update(eq(goldenPatientRebuilt), any(RequestDetails.class));

		ArgumentCaptor<List<String>> idsCaptor = ArgumentCaptor.forClass(List.class);
		verify(myIIdHelperService).resolveResourcePersistentIds(any(RequestPartitionId.class), anyString(), idsCaptor.capture());
		assertNotNull(idsCaptor.getValue());
		assertFalse(idsCaptor.getValue().isEmpty());
		for (String id : idsCaptor.getValue()) {
			assertFalse(id.contains("/"));
			assertFalse(id.contains("Patient"));
		}
	}

	private MdmTransactionContext createTransactionContext() {
		MdmTransactionContext context = new MdmTransactionContext();
		context.setRestOperation(MdmTransactionContext.OperationType.UPDATE_LINK);
		context.setResourceType("Patient");
		return context;
	}

	private MdmLinkJson createLinkJson(
		IBaseResource theSource,
		IBaseResource theGolden
	) {
		MdmLinkJson linkJson = new MdmLinkJson();
		linkJson.setLinkSource(MdmLinkSourceEnum.AUTO);
		linkJson.setGoldenResourceId(theGolden.getIdElement().getValueAsString());
		linkJson.setSourceId(theSource.getIdElement().getValueAsString());
		linkJson.setMatchResult(MdmMatchResultEnum.MATCH);

		return linkJson;
	}
}
