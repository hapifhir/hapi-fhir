package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSqlBuilder;
import ca.uhn.fhir.jpa.delete.batch2.DeleteExpungeSvcImpl;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteExpungeSvcImplTest {
	@Mock
	private EntityManager myEntityManager;
	@Mock
	private DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilder;
	@Mock
	private IFulltextSearchSvc myFulltextSearchSvc;
	@InjectMocks
	private DeleteExpungeSvcImpl myDeleteExpungeSvc;

	@Mock
	private Appender<ILoggingEvent> myAppender;
	@Captor
	ArgumentCaptor<ILoggingEvent> myLoggingEvent;

	@Test
	public void testChunkIdIncludedInNumRecordsDeletedMessage() {
		Logger logger = (Logger) LoggerFactory.getLogger(DeleteExpungeSvcImpl.class);
		logger.addAppender(myAppender);

		when(myDeleteExpungeSqlBuilder.convertPidsToDeleteExpungeSql(Collections.emptyList(), false, 1)).thenReturn(mock(DeleteExpungeSqlBuilder.DeleteExpungeSqlResult.class));
		WorkChunk workChunk = mock(WorkChunk.class);
		when(workChunk.getId()).thenReturn("abc-123");

		myDeleteExpungeSvc.deleteExpunge(Collections.emptyList(), false, 1, workChunk);
		verify(myAppender, atLeastOnce()).doAppend(myLoggingEvent.capture());
		List<ILoggingEvent> events = myLoggingEvent.getAllValues();
		assertEquals(Level.INFO, events.get(0).getLevel());
		assertEquals("Chunk[abc-123] - Delete expunge sql commands affected 0 rows", events.get(0).getFormattedMessage());
	}

}
