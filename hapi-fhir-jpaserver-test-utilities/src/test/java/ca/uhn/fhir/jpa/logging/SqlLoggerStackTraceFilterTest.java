package ca.uhn.fhir.jpa.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SqlLoggerStackTraceFilterTest {

	private final SqlLoggerStackTraceFilter myTested = new SqlLoggerStackTraceFilter();
	private SqlLoggerStackTraceFilter mySpiedTested;

	@BeforeEach
	void setUp() {
		mySpiedTested = spy(myTested);
	}

	@Test
	void noMatch() {
		mySpiedTested.setFilterDefinitions(List.of(
			"ca.cdr.clustermgr.svc.impl.DatabaseBackedHttpSessionStorageSvcImpl",
			"ca.uhn.fhir.jpa.cache.CdrResourceChangeListenerCache",
			"ca.cdr.clustermgr.svc.impl.ModuleStatusControllerSvcImpl",
			"ca.cdr.clustermgr.svc.impl.StatsHeartbeatSvcImpl"
		));

		Stream<StackTraceElement> stackTraceStream = Stream.of(
			stElement("ca.cdr.api.camel.ICamelRouteEndpointSvc"),
			stElement("ca.cdr.api.transactionlog.ITransactionLogFetchingSvc"),
			stElement("ca.cdr.cdaxv2.impl.CdaDocumentSvcImpl"),
			stElement("ca.cdr.endpoint.cdshooks.svc.prefetch.CdsHooksDaoAuthorizationSvc"),
			stElement("ca.cdr.endpoint.hl7v2.in.converter.Hl7V2InboundConverter")
		);
		doReturn(stackTraceStream).when(mySpiedTested).getStackTraceStream();

		// execute
		boolean matched = mySpiedTested.match("not-used");

		assertFalse(matched);
	}
	
	@Test
	void match() {
		mySpiedTested.setFilterDefinitions(List.of(
			"ca.cdr.clustermgr.svc.impl.DatabaseBackedHttpSessionStorageSvcImpl",
			"ca.uhn.fhir.jpa.cache.CdrResourceChangeListenerCache",
			"ca.cdr.clustermgr.svc.impl.ModuleStatusControllerSvcImpl",
			"ca.cdr.clustermgr.svc.impl.StatsHeartbeatSvcImpl"
		));

		Stream<StackTraceElement> stackTraceStream = Stream.of(
			stElement("ca.uhn.fhir.jpa.cache.CdrResourceChangeListenerCache"),
			stElement("ca.cdr.api.camel.ICamelRouteEndpointSvc"),
			stElement("ca.cdr.api.transactionlog.ITransactionLogFetchingSvc"),
			stElement("ca.cdr.api.transactionlog.ITransactionLogFetchingSvc"),
			stElement("ca.cdr.cdaxv2.impl.CdaDocumentSvcImpl"),
			stElement("ca.cdr.endpoint.cdshooks.svc.prefetch.CdsHooksDaoAuthorizationSvc"),
			stElement("ca.cdr.endpoint.hl7v2.in.converter.Hl7V2InboundConverter"),
			stElement("ca.cdr.clustermgr.svc.impl.StatsHeartbeatSvcImpl") 				// <== match
		);
		doReturn(stackTraceStream).when(mySpiedTested).getStackTraceStream();

		// execute
		boolean matched = mySpiedTested.match("not-used");

		assertTrue(matched);
	}


	private StackTraceElement stElement(String theClassName) {
		return new StackTraceElement(theClassName, "", null, 0);
	}
}
