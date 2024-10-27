package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaBulkExportProcessorTest {

	private static class ListResultIterator implements IResultIterator<JpaPid> {

		private final List<JpaPid> myList;

		private int index;

		public ListResultIterator(List<JpaPid> theList) {
			myList = theList;
		}

		@Override
		public int getSkippedCount() {
			return 0;
		}

		@Override
		public int getNonSkippedCount() {
			return 0;
		}

		@Override
		public Collection<JpaPid> getNextResultBatch(long theBatchSize) {
			return null;
		}

		@Override
		public void close() {

		}

		@Override
		public boolean hasNext() {
			return index < myList.size();
		}

		@Override
		public JpaPid next() {
			return myList.get(index++);
		}
	}

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private BulkExportHelperService myBulkExportHelperService;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@Mock
	private IMdmLinkDao<JpaPid, MdmLink> myMdmLinkDao;

	@Mock
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	@Mock
	private ISearchParamRegistry mySearchParamRegistry;

	@Spy
	private IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();

	@InjectMocks
	private JpaBulkExportProcessor myProcessor;

	@BeforeEach
	public void init() {
		myProcessor.mySearchBuilderFactory = mySearchBuilderFactory;
		myProcessor.myMdmLinkDao = myMdmLinkDao;
	}

	private ExportPIDIteratorParameters createExportParameters(BulkExportJobParameters.ExportStyle theExportStyle) {
		ExportPIDIteratorParameters parameters = new ExportPIDIteratorParameters();
		parameters.setInstanceId("instanceId");
		parameters.setExportStyle(theExportStyle);
		if (theExportStyle == BulkExportJobParameters.ExportStyle.GROUP) {
			parameters.setGroupId("123");
		}
		parameters.setStartDate(new Date());
		return parameters;
	}

	private List<IIdType> createPatientTypes() {
		long id1 = 123;
		long id2 = 456;
		String patient1Id = "Patient/" + id1;
		String patient2Id = "Patient/" + id2;

		return Arrays.asList(
			new IdDt(patient1Id),
			new IdDt(patient2Id)
		);
	}

	private MdmPidTuple<JpaPid> createTuple(long theGroupId, long theGoldenId) {
		return MdmPidTuple.fromGoldenAndSourceAndPartitionIds(JpaPid.fromId(theGoldenId), null, JpaPid.fromId(theGroupId), null);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void getResourcePidIterator_paramsWithPatientExportStyle_returnsAnIterator(boolean thePartitioned) {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.PATIENT);
		parameters.setResourceType("Patient");

		parameters.setPartitionId(getPartitionIdFromParams(thePartitioned));

		SearchParameterMap map = new SearchParameterMap();
		List<SearchParameterMap> maps = new ArrayList<>();
		maps.add(map);

		JpaPid pid = JpaPid.fromId(123L);
		JpaPid pid2 = JpaPid.fromId(456L);
		ListResultIterator resultIterator = new ListResultIterator(
			Arrays.asList(pid, pid2)
		);

		// extra mocks
		IFhirResourceDao<Patient> mockDao = mock(IFhirResourceDao.class);
		ISearchBuilder<JpaPid> searchBuilder = mock(ISearchBuilder.class);

		// when
		when(myStorageSettings.getIndexMissingFields())
			.thenReturn(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		when(myBulkExportHelperService.createSearchParameterMapsForResourceType(any(RuntimeResourceDefinition.class), eq(parameters), any(boolean.class)))
			.thenReturn(maps);
		// from getSearchBuilderForLocalResourceType
		when(myDaoRegistry.getResourceDao(anyString()))
			.thenReturn(mockDao);
		when(mySearchBuilderFactory.newSearchBuilder(eq(mockDao), eq(parameters.getResourceType()), any()))
			.thenReturn(searchBuilder);
		// ret
		when(searchBuilder.createQuery(
			any(),
			any(),
			any(),
			any()))
			.thenReturn(resultIterator);

		// test
		Iterator<JpaPid> pidIterator = myProcessor.getResourcePidIterator(parameters);

		// verify
		assertNotNull(pidIterator);
		assertTrue(pidIterator.hasNext());
		assertEquals(pid, pidIterator.next());
		assertTrue(pidIterator.hasNext());
		assertEquals(pid2, pidIterator.next());
		assertFalse(pidIterator.hasNext());
		verify(searchBuilder, times(1)).createQuery(
			eq(map),
			any(SearchRuntimeDetails.class),
			nullable(RequestDetails.class),
			eq(getPartitionIdFromParams(thePartitioned)));

	}

	private RequestPartitionId getPartitionIdFromParams(boolean thePartitioned) {
		if (thePartitioned) {
			return RequestPartitionId.fromPartitionName("Partition-A");
		} else {
			return RequestPartitionId.allPartitions();
		}
	}

	@Test
	public void getResourcePidIterator_patientStyleWithIndexMissingFieldsDisabled_throws() {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.PATIENT);
		parameters.setResourceType("Patient");

		// when
		when(myStorageSettings.getIndexMissingFields())
			.thenReturn(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		// test
		try {
			myProcessor.getResourcePidIterator(parameters);
			fail();
		} catch (InternalErrorException ex) {
			assertThat(ex.getMessage()).contains("You attempted to start a Patient Bulk Export,");
		}
	}

	@ParameterizedTest
	@CsvSource({"false, false", "false, true", "true, true", "true, false"})
	public void getResourcePidIterator_groupExportStyleWithPatientResource_returnsIterator(boolean theMdm, boolean thePartitioned) {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.GROUP);
		parameters.setResourceType("Patient");

		JpaPid groupId = JpaPid.fromId(Long.parseLong(parameters.getGroupId()));
		long groupGoldenPid = 4567;

		Group groupResource = new Group();
		groupResource.setId(parameters.getGroupId());

		List<IIdType> patientTypes = createPatientTypes();
		List<JpaPid> pids = new ArrayList<>();
		for (IIdType type : patientTypes) {
			pids.add(JpaPid.fromId(type.getIdPartAsLong()));
		}

		MdmPidTuple<JpaPid> tuple = createTuple(groupId.getId(), groupGoldenPid);

		IFhirResourceDao<Group> groupDao = mock(IFhirResourceDao.class);
		parameters.setExpandMdm(theMdm); // set mdm expansion
		parameters.setPartitionId(getPartitionIdFromParams(thePartitioned));

		// extra mocks
		IFhirResourceDao<?> mockDao = mock(IFhirResourceDao.class);
		ISearchBuilder<JpaPid> searchBuilder = mock(ISearchBuilder.class);

		// from getMembersFromGroupWithFilter
		when(myBulkExportHelperService.createSearchParameterMapsForResourceType(any(RuntimeResourceDefinition.class), eq(parameters), any(boolean.class)))
			.thenReturn(Collections.singletonList(new SearchParameterMap()));
		// from getSearchBuilderForLocalResourceType
		when(myDaoRegistry.getResourceDao(not(eq("Group"))))
			.thenReturn(mockDao);
		when(mySearchBuilderFactory.newSearchBuilder(eq(mockDao), eq(parameters.getResourceType()), any()))
			.thenReturn(searchBuilder);
		// ret
		when(searchBuilder.createQuery(
			any(SearchParameterMap.class),
			any(SearchRuntimeDetails.class),
			any(),
			eq(getPartitionIdFromParams(thePartitioned))))
			.thenReturn(new ListResultIterator(pids));

		// mdm expansion stuff
		if (theMdm) {
			when(myDaoRegistry.getResourceDao(eq("Group")))
				.thenReturn(groupDao);
			when(groupDao.read(eq(new IdDt(parameters.getGroupId())), any(SystemRequestDetails.class)))
				.thenReturn(groupResource);
			when(myIdHelperService.translatePidsToForcedIds(any(Set.class)))
				.thenAnswer(params -> {
					Set<JpaPid> uniqPids = params.getArgument(0);
					HashMap<JpaPid, Optional<String>> answer = new HashMap<>();
					for (JpaPid l : uniqPids) {
						answer.put(l, Optional.empty());
					}
					return new PersistentIdToForcedIdMap<>(answer);
				});
			when(myIdHelperService.getPidOrNull(eq(getPartitionIdFromParams(thePartitioned)), any(Group.class)))
				.thenReturn(groupId);
			when(myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(any(JpaPid.class), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(Collections.singletonList(tuple));
			when(myMdmExpansionCacheSvc.hasBeenPopulated())
				.thenReturn(false); // does not matter, since if false, it then goes and populates
		}

		// test
		Iterator<JpaPid> pidIterator = myProcessor.getResourcePidIterator(parameters);

		// verify
		assertNotNull(pidIterator);
		int count = 0;
		assertTrue(pidIterator.hasNext());
		while (pidIterator.hasNext()) {
			JpaPid pid = pidIterator.next();
			long idAsLong = pid.getId();
			boolean existing = pids.contains(JpaPid.fromId(idAsLong));
			if (!existing) {
				assertTrue(theMdm);
				assertEquals(groupGoldenPid, idAsLong);
			} else {
				count++;
			}
		}
		int total = pids.size();
		assertEquals(total, count);
		if (theMdm) {
			ArgumentCaptor<SystemRequestDetails> requestDetailsCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
			verify(groupDao).read(eq(new IdDt(parameters.getGroupId())), requestDetailsCaptor.capture());
			validatePartitionId(thePartitioned, requestDetailsCaptor.getValue().getRequestPartitionId());
		}
	}

	private void validatePartitionId(boolean thePartitioned, RequestPartitionId thePartitionId) {

		if (thePartitioned) {
			assertNotNull(thePartitionId.getPartitionNames());
			assertEquals("Partition-A", thePartitionId.getPartitionNames().get(0));
		} else {
			assertEquals(RequestPartitionId.allPartitions(), thePartitionId);
		}

	}

	// source is: "isExpandMdm,(whether or not to test on a specific partition)
	@ParameterizedTest
	@CsvSource({"false, false", "false, true", "true, true", "true, false"})
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getResourcePidIterator_groupExportStyleWithNonPatientResource_returnsIterator(boolean theMdm, boolean thePartitioned) {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.GROUP);
		parameters.setResourceType("Observation");

		JpaPid groupId = JpaPid.fromId(Long.parseLong(parameters.getGroupId()));
		Group groupResource = new Group();
		groupResource.setId(parameters.getGroupId());
		long groupGoldenPid = 4567;

		JpaPid patientPid = JpaPid.fromId(123L);
		JpaPid patientPid2 = JpaPid.fromId(456L);
		ListResultIterator patientResultsIterator = new ListResultIterator(
			Arrays.asList(patientPid, patientPid2)
		);

		JpaPid observationPid = JpaPid.fromId(234L);
		JpaPid observationPid2 = JpaPid.fromId(567L);
		ListResultIterator observationResultsIterator = new ListResultIterator(
			Arrays.asList(observationPid, observationPid2)
		);

		HashSet<JpaPid> observationPidSet = new HashSet<>();
		observationPidSet.add(observationPid);
		observationPidSet.add(observationPid2);


		MdmPidTuple<JpaPid> tuple = createTuple(groupId.getId(), groupGoldenPid);

		IFhirResourceDao<Patient> patientDao = mock(IFhirResourceDao.class);
		IFhirResourceDao<Group> groupDao = mock(IFhirResourceDao.class);
		IFhirResourceDao<Observation> observationDao = mock(IFhirResourceDao.class);
		parameters.setExpandMdm(theMdm); // set mdm expansion

		parameters.setPartitionId(getPartitionIdFromParams(thePartitioned));

		// extra mocks
		ISearchBuilder<JpaPid> patientSearchBuilder = mock(ISearchBuilder.class);
		ISearchBuilder<JpaPid> observationSearchBuilder = mock(ISearchBuilder.class);

		// when
		RuntimeSearchParam searchParam = new RuntimeSearchParam(new IdType("1"), "", "", "", "", RestSearchParameterTypeEnum.STRING, Collections.singleton(""), Collections.singleton(""), RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, Collections.singleton(""));
		when(mySearchParamRegistry.getActiveSearchParam(any(), any())).thenReturn(searchParam);
		// expandAllPatientPidsFromGroup
		when(myDaoRegistry.getResourceDao(eq("Group")))
			.thenReturn(groupDao);
		when(groupDao.read(any(IIdType.class), any(SystemRequestDetails.class)))
			.thenReturn(groupResource);
		when(myIdHelperService.getPidOrNull(eq(getPartitionIdFromParams(thePartitioned)), eq(groupResource)))
			.thenReturn(groupId);
		// getMembersFromGroupWithFilter
		when(myDaoRegistry.getResourceDao(eq("Patient")))
			.thenReturn(patientDao);
		when(mySearchBuilderFactory.newSearchBuilder(eq(patientDao), eq("Patient"), eq(Patient.class)))
			.thenReturn(patientSearchBuilder);
		RuntimeResourceDefinition patientDef = myFhirContext.getResourceDefinition("Patient");
		SearchParameterMap patientSpMap = new SearchParameterMap();
		when(myBulkExportHelperService.createSearchParameterMapsForResourceType(eq(patientDef), eq(parameters), any(boolean.class)))
			.thenReturn(Collections.singletonList(patientSpMap));
		when(patientSearchBuilder.createQuery(eq(patientSpMap), any(), any(), eq(getPartitionIdFromParams(thePartitioned))))
			.thenReturn(patientResultsIterator);
		// queryResourceTypeWithReferencesToPatients
		SearchParameterMap observationSpMap = new SearchParameterMap();
		RuntimeResourceDefinition observationDef = myFhirContext.getResourceDefinition("Observation");
		when(myBulkExportHelperService.createSearchParameterMapsForResourceType(eq(observationDef), eq(parameters), any(boolean.class)))
			.thenReturn(Collections.singletonList(observationSpMap));
		when(myDaoRegistry.getResourceDao((eq("Observation"))))
			.thenReturn(observationDao);
		when(mySearchBuilderFactory.newSearchBuilder(eq(observationDao), eq("Observation"), eq(Observation.class)))
			.thenReturn(observationSearchBuilder);
		when(observationSearchBuilder.loadIncludes(
			any(SearchBuilderLoadIncludesParameters.class)
		))
			.thenReturn(new HashSet<>());

		// ret
		when(observationSearchBuilder.createQuery(
			eq(observationSpMap),
			any(SearchRuntimeDetails.class),
			any(),
			eq(getPartitionIdFromParams(thePartitioned))))
			.thenReturn(observationResultsIterator);

		if (theMdm) {
			when(myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(any(JpaPid.class), eq(MdmMatchResultEnum.MATCH)))
				.thenReturn(Collections.singletonList(tuple));
			when(myIdHelperService.translatePidsToForcedIds(any(Set.class)))
				.thenAnswer(params -> {
					Set<JpaPid> uniqPids = params.getArgument(0);
					HashMap<JpaPid, Optional<String>> answer = new HashMap<>();
					for (JpaPid l : uniqPids) {
						answer.put(l, Optional.empty());
					}
					return new PersistentIdToForcedIdMap<>(answer);
				});
		}

		// test
		Iterator<JpaPid> pidIterator = myProcessor.getResourcePidIterator(parameters);

		// verify
		assertThat(pidIterator).as("PID iterator null for mdm = " + theMdm).isNotNull();
		assertThat(pidIterator.hasNext()).as("PID iterator empty for mdm = " + theMdm).isTrue();
		ArgumentCaptor<SystemRequestDetails> groupDaoReadSystemRequestDetailsCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(groupDao).read(any(IIdType.class), groupDaoReadSystemRequestDetailsCaptor.capture());
		validatePartitionId(thePartitioned, groupDaoReadSystemRequestDetailsCaptor.getValue().getRequestPartitionId());
		ArgumentCaptor<SearchBuilderLoadIncludesParameters> searchBuilderLoadIncludesRequestDetailsCaptor = ArgumentCaptor.forClass(SearchBuilderLoadIncludesParameters.class);
		verify(observationSearchBuilder).loadIncludes(searchBuilderLoadIncludesRequestDetailsCaptor.capture());
		SearchBuilderLoadIncludesParameters param = searchBuilderLoadIncludesRequestDetailsCaptor.getValue();
		assertTrue(param.getRequestDetails() instanceof SystemRequestDetails);
		SystemRequestDetails details = (SystemRequestDetails) param.getRequestDetails();
		validatePartitionId(thePartitioned, details.getRequestPartitionId());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void getResourcePidIterator_systemExport_returnsIterator(boolean thePartitioned) {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.SYSTEM);
		parameters.setResourceType("Patient");

		parameters.setPartitionId(getPartitionIdFromParams(thePartitioned));

		JpaPid pid = JpaPid.fromId(123L);
		JpaPid pid2 = JpaPid.fromId(456L);
		ListResultIterator resultIterator = new ListResultIterator(
			Arrays.asList(pid, pid2)
		);

		// extra mocks
		IFhirResourceDao<Patient> dao = mock(IFhirResourceDao.class);
		ISearchBuilder<JpaPid> searchBuilder = mock(ISearchBuilder.class);

		// when
		when(myBulkExportHelperService.createSearchParameterMapsForResourceType(
			any(RuntimeResourceDefinition.class),
			any(ExportPIDIteratorParameters.class),
			any(boolean.class)
		)).thenReturn(Collections.singletonList(new SearchParameterMap()));
		when(myDaoRegistry.getResourceDao(eq("Patient")))
			.thenReturn(dao);
		when(mySearchBuilderFactory.newSearchBuilder(
			any(IFhirResourceDao.class),
			anyString(),
			any()
		)).thenReturn(searchBuilder);
		when(searchBuilder.createQuery(
			any(SearchParameterMap.class),
			any(SearchRuntimeDetails.class),
			any(),
			eq(getPartitionIdFromParams(thePartitioned))
		)).thenReturn(resultIterator);

		// test
		Iterator<JpaPid> iterator = myProcessor.getResourcePidIterator(parameters);

		// verify
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		int count = 0;
		while (iterator.hasNext()) {
			JpaPid ret = iterator.next();
			assertTrue(ret.equals(pid) || ret.equals(pid2));
			count++;
		}
		assertEquals(2, count);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void getResourcePidIterator_groupExportStyleWithGroupResource_returnsAnIterator(boolean thePartitioned) {
		// setup
		ExportPIDIteratorParameters parameters = createExportParameters(BulkExportJobParameters.ExportStyle.GROUP);
		parameters.setResourceType("Group");

		parameters.setPartitionId(getPartitionIdFromParams(thePartitioned));

		Long groupId = Long.parseLong(parameters.getGroupId());
		JpaPid pid = JpaPid.fromId(groupId);
		Group groupResource = new Group();
		groupResource.setId(parameters.getGroupId());

		// extra mocks
		IFhirResourceDao<Group> mockDao = mock(IFhirResourceDao.class);

		// when
		when(myDaoRegistry.getResourceDao(eq("Group")))
			.thenReturn(mockDao);
		when(mockDao.read(any(IdDt.class), any(SystemRequestDetails.class)))
			.thenReturn(groupResource);

		// ret
		when(myIdHelperService.getPidOrNull(eq(getPartitionIdFromParams(thePartitioned)), eq(groupResource)))
			.thenReturn(pid);

		// test
		Iterator<JpaPid> pidIterator = myProcessor.getResourcePidIterator(parameters);

		// verify
		assertNotNull(pidIterator);
		assertTrue(pidIterator.hasNext());
		assertEquals(pid, pidIterator.next());
		assertFalse(pidIterator.hasNext());
		ArgumentCaptor<SystemRequestDetails> resourceDaoServletRequestDetailsCaptor = ArgumentCaptor.forClass(SystemRequestDetails.class);
		verify(mockDao).read(any(IdDt.class), resourceDaoServletRequestDetailsCaptor.capture());
		validatePartitionId(thePartitioned, resourceDaoServletRequestDetailsCaptor.getValue().getRequestPartitionId());
	}

}
