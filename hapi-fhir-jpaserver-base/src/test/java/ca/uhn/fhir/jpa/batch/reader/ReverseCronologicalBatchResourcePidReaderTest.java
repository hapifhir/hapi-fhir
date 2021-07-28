package ca.uhn.fhir.jpa.batch.reader;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jsonldjava.shaded.com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReverseCronologicalBatchResourcePidReaderTest {
	private static final int BATCH_SIZE = 3;
	static FhirContext ourFhirContext = FhirContext.forR4Cached();
	static String URL_A = "a";
	static String URL_B = "b";
	static String URL_C = "c";
	static Set<ResourcePersistentId> emptySet = Collections.emptySet();
	static RequestPartitionId partId = RequestPartitionId.defaultPartition();

	Patient myPatient;

	@Mock
	MatchUrlService myMatchUrlService;
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	IFhirResourceDao<Patient> myPatientDao;
	private final RequestPartitionId myDefaultPartitionId = RequestPartitionId.defaultPartition();
	@Mock
	private IResultIterator myResultIter;

	@InjectMocks
	ReverseCronologicalBatchResourcePidReader myReader = new ReverseCronologicalBatchResourcePidReader();
	@Mock
	private BatchResourceSearcher myBatchResourceSearcher;

	@BeforeEach
	public void before() throws JsonProcessingException {
		RequestListJson requestListJson = new RequestListJson();
		requestListJson.setPartitionedUrls(Lists.newArrayList(new PartitionedUrl(URL_A, partId), new PartitionedUrl(URL_B, partId), new PartitionedUrl(URL_C, partId)));
		ObjectMapper mapper = new ObjectMapper();
		String requestListJsonString = mapper.writeValueAsString(requestListJson);
		myReader.setRequestListJson(requestListJsonString);
		myReader.setBatchSize(BATCH_SIZE);

		SearchParameterMap map = new SearchParameterMap();
		RuntimeResourceDefinition patientResDef = ourFhirContext.getResourceDefinition("Patient");
		when(myMatchUrlService.getResourceSearch(URL_A, myDefaultPartitionId)).thenReturn(new ResourceSearch(patientResDef, map, myDefaultPartitionId));
		when(myMatchUrlService.getResourceSearch(URL_B, myDefaultPartitionId)).thenReturn(new ResourceSearch(patientResDef, map, myDefaultPartitionId));
		when(myMatchUrlService.getResourceSearch(URL_C, myDefaultPartitionId)).thenReturn(new ResourceSearch(patientResDef, map, myDefaultPartitionId));
		when(myDaoRegistry.getResourceDao("Patient")).thenReturn(myPatientDao);
		myPatient = new Patient();
		when(myPatientDao.readByPid(any())).thenReturn(myPatient);
		Calendar cal = new GregorianCalendar(2021, 1, 1);
		myPatient.getMeta().setLastUpdated(cal.getTime());

		when(myBatchResourceSearcher.performSearch(any(), any())).thenReturn(myResultIter);
	}

	private Set<ResourcePersistentId> buildPidSet(Integer... thePids) {
		return Arrays.stream(thePids)
			.map(Long::new)
			.map(ResourcePersistentId::new)
			.collect(Collectors.toSet());
	}

	@Test
	public void test3x1() throws Exception {
		when(myResultIter.getNextResultBatch(BATCH_SIZE))
			.thenReturn(buildPidSet(1, 2, 3))
			.thenReturn(emptySet)
			.thenReturn(buildPidSet(4, 5, 6))
			.thenReturn(emptySet)
			.thenReturn(buildPidSet(7, 8))
			.thenReturn(emptySet);

		assertListEquals(myReader.read(), 1, 2, 3);
		assertListEquals(myReader.read(), 4, 5, 6);
		assertListEquals(myReader.read(), 7, 8);
		assertNull(myReader.read());
	}

	@Test
	public void testReadRepeat() throws Exception {
		when(myResultIter.getNextResultBatch(BATCH_SIZE))
			.thenReturn(buildPidSet(1, 2, 3))
			.thenReturn(buildPidSet(1, 2, 3))
			.thenReturn(buildPidSet(2, 3, 4))
			.thenReturn(buildPidSet(4, 5))
			.thenReturn(emptySet);

		when(myResultIter.hasNext())
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(false);

		assertListEquals(myReader.read(), 1, 2, 3);
		assertListEquals(myReader.read(), 4, 5);
		assertNull(myReader.read());
	}

	@Test
	public void test1x3start() throws Exception {
		when(myResultIter.getNextResultBatch(BATCH_SIZE))
			.thenReturn(buildPidSet(1, 2, 3))
			.thenReturn(buildPidSet(4, 5, 6))
			.thenReturn(buildPidSet(7, 8))
			.thenReturn(emptySet)
			.thenReturn(emptySet)
			.thenReturn(emptySet);

		assertListEquals(myReader.read(), 1, 2, 3);
		assertListEquals(myReader.read(), 4, 5, 6);
		assertListEquals(myReader.read(), 7, 8);
		assertNull(myReader.read());
	}

	@Test
	public void test1x3end() throws Exception {
		when(myResultIter.getNextResultBatch(BATCH_SIZE))
			.thenReturn(emptySet)
			.thenReturn(emptySet)
			.thenReturn(buildPidSet(1, 2, 3))
			.thenReturn(buildPidSet(4, 5, 6))
			.thenReturn(buildPidSet(7, 8))
			.thenReturn(emptySet);

		assertListEquals(myReader.read(), 1, 2, 3);
		assertListEquals(myReader.read(), 4, 5, 6);
		assertListEquals(myReader.read(), 7, 8);
		assertNull(myReader.read());
	}

	private void assertListEquals(List<Long> theList, Integer... theValues) {
		assertThat(theList, hasSize(theValues.length));
		for (int i = 0; i < theList.size(); ++i) {
			assertEquals(theList.get(i), Long.valueOf(theValues[i]));
		}
	}
}
