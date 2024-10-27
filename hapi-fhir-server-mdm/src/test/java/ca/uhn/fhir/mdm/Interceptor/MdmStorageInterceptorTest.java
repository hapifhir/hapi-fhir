package ca.uhn.fhir.mdm.Interceptor;

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MdmStorageInterceptorTest {

	private static final Logger ourLog = (Logger) LoggerFactory.getLogger(MdmStorageInterceptor.class);
	private ListAppender<ILoggingEvent> myListAppender;

	@InjectMocks
	private MdmStorageInterceptor myMdmStorageInterceptor;
	@Mock
	private IMdmSettings myMdmSettings;

	@BeforeEach
	public void beforeEach(){
		myListAppender = new ListAppender<>();
		myListAppender.start();
		ourLog.addAppender(myListAppender);
	}

	@AfterEach
	public void afterEach(){
		myListAppender.stop();
	}

	@Test
	public void testBlockManualGoldenResourceManipulationOnUpdate_nullOldResource_noLogs() {
		Patient updatedResource = new Patient();
		RequestDetails requestDetails = new ServletRequestDetails();

		myMdmStorageInterceptor.blockManualGoldenResourceManipulationOnUpdate(null, updatedResource, requestDetails, null);

		List<ILoggingEvent> warningLogs = myListAppender
			.list
			.stream()
			.filter(event -> Level.WARN.equals(event.getLevel()))
			.toList();
		assertThat(warningLogs).isEmpty();

	}

}
