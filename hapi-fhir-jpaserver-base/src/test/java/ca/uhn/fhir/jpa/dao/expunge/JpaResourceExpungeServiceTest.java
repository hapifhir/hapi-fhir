package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTablePk;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaResourceExpungeServiceTest {
	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(JpaResourceExpungeService.class);

	@InjectMocks
	@Spy
	private JpaResourceExpungeService myService = spy(new JpaResourceExpungeService());

	@Mock
	private IResourceTableDao myResourceTableDao;

	@Mock
	private IResourceHistoryTableDao myResourceHistoryTableDao;

	@Mock
	private RequestDetails myRequestDetails;

	@Mock
	private ResourceTable resourceTable;

	@Mock
	private JpaStorageSettings myStorageSettings;

	@Test
	public void testExpungeDoesNotDeleteAllSearchParams() {
		when(myResourceTableDao.findById(any(JpaPid.class))).thenReturn(Optional.of(resourceTable));
		when(resourceTable.getIdDt()).thenReturn(new IdDt());
		when(resourceTable.getId()).thenReturn(new JpaPid());
		myService.expungeCurrentVersionOfResource(myRequestDetails, JpaPid.fromId(1L), new AtomicInteger(1));
		verify(myService, never()).deleteAllSearchParams(any());
	}


	@Test
	public void testExpungeHistoricalVersions_missingEntry_throwsWithContextualMessage() throws NoSuchFieldException, IllegalAccessException {
		// given a primary key with no existing history
		ResourceHistoryTablePk pk = new ResourceHistoryTablePk();
		pk.setPartitionIdValue(42);
		// set versionId via reflection to avoid null in message
		Field versionIdField = ResourceHistoryTablePk.class.getDeclaredField("myVersionId");
		versionIdField.setAccessible(true);
		versionIdField.set(pk, 123L);

		when(myResourceHistoryTableDao.findById(pk)).thenReturn(Optional.empty());

		AtomicInteger remaining = new AtomicInteger(1);
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> myService.expungeHistoricalVersions(myRequestDetails, Collections.singletonList(pk), remaining)
		);

		String msg = ex.getMessage();
		assertTrue(msg.contains("No historical version found"),
			"Expected exception message to contain 'No historical version found'");
		assertTrue(msg.contains("ResourceHistoryTablePk: 123"),
			"Expected message to include 'ResourceHistoryTablePk: 123'");
		assertTrue(msg.contains("partition 42"),
			"Expected message to include partition id '42'");
	}
}
