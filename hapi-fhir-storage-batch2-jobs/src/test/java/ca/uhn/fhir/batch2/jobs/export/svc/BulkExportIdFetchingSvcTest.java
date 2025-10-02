package ca.uhn.fhir.batch2.jobs.export.svc;


import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.FetchResourceIdsStep;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.test.concurrency.PointcutLatch;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BulkExportIdFetchingSvcTest {
	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(BulkExportIdFetchingSvc.class);

	@Mock
	private ListAppender<ILoggingEvent> myAppender;

	@Mock
	private IBulkExportProcessor myBulkExportProcessor;

	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	@Mock
	private IResourceSupportedSvc myIResourceSupportedSvc;

	@InjectMocks
	private BulkExportIdFetchingSvc mySvc;

	@BeforeEach
	public void init() {
		ourLog.addAppender(myAppender);
	}

	@AfterEach
	public void after() {
		ourLog.detachAppender(myAppender);
	}

	@Test
	public void fetchIds_happyPath_test() throws InterruptedException {
		// setup
		ExportPIDIteratorParameters parameters = new ExportPIDIteratorParameters();
		parameters.setRequestedResourceTypes(List.of("Patient", "Observation"));
		parameters.setExportStyle(BulkExportJobParameters.ExportStyle.PATIENT);

		List<IResourcePersistentId<?>> patientIds = new ArrayList<>();
		List<IResourcePersistentId<?>> observationIds = new ArrayList<>();

		patientIds.add(JpaPid.fromIdAndVersionAndResourceType(1L, 1L, "Patient"));
		patientIds.add(JpaPid.fromIdAndVersionAndResourceType(2L, 1L, "Patient"));

		observationIds.add(JpaPid.fromIdAndVersionAndResourceType(3L, 1L, "Observation"));
		observationIds.add(JpaPid.fromIdAndVersionAndResourceType(4L, 1L, "Observation"));

		List<ResourceIdList> resourceIdLists = new ArrayList<>();
		PointcutLatch latch = new PointcutLatch("latch");
		Consumer<ResourceIdList> consumer = resourceIdList -> {
			resourceIdLists.add(resourceIdList);
			latch.call(1);
		};

		// when
		when(myIResourceSupportedSvc.isSupported(anyString()))
			.thenReturn(true);
		when(myBulkExportProcessor.getResourcePidIterator(any(ExportPIDIteratorParameters.class)))
			.thenReturn(patientIds.iterator())
			.thenReturn(observationIds.iterator());

		// test
		latch.setExpectedCount(2);
		int submission = mySvc.fetchIds(parameters, consumer);

		// wait
		latch.awaitExpected();

		// verify
		assertEquals(2, submission);
		assertEquals(submission, resourceIdLists.size());
		resourceIdLists.forEach(rid -> {
			assertTrue(rid.getResourceType().equals("Observation") || rid.getResourceType().equals("Patient"));

			switch (rid.getResourceType()) {
				case "Observation" -> {
					assertEquals(observationIds.size(), rid.getIds().size());
					Set<String> strPid = new HashSet<>();
					for (IResourcePersistentId<?> id : observationIds) {
						assertTrue(id instanceof JpaPid);
						JpaPid jpid = (JpaPid) id;
						strPid.add(jpid.getId().toString());
					}
					for (TypedPidJson id : rid.getIds()) {
						assertTrue(strPid.contains(id.getPid()));
					}
				}
				case "Patient" -> {
					assertEquals(patientIds.size(), rid.getIds().size());
					Set<String> strPid = new HashSet<>();
					for (IResourcePersistentId<?> id : patientIds) {
						assertTrue(id instanceof JpaPid);
						JpaPid jpid = (JpaPid) id;
						strPid.add(jpid.getId().toString());
					}
					for (TypedPidJson id : rid.getIds()) {
						assertTrue(strPid.contains(id.getPid()));
					}
				}
			}
		});

		ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myAppender, times(submission)).doAppend(captor.capture());
		captor.getAllValues()
			.forEach(logEvent -> {
				assertEquals(Level.INFO, logEvent.getLevel());
				assertTrue(logEvent.getMessage()
					.contains("Running FetchIds for resource type"));
			});
	}
}
