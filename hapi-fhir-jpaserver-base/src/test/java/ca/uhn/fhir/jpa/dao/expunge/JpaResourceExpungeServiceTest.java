package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
		when(myResourceTableDao.findById(any())).thenReturn(Optional.of(resourceTable));
		when(resourceTable.getIdDt()).thenReturn(new IdDt());
		myService.expungeCurrentVersionOfResource(myRequestDetails, 1L, new AtomicInteger(1));
		verify(myService, never()).deleteAllSearchParams(any());
	}
}
